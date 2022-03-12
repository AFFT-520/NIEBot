package com.eugc;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;

public class GetGeckoDriver {
    public static int BUFFER_SIZE = 4096;
    public static String OS = System.getProperty("os.name").toLowerCase();
    public static void getFiles(MessageBox mb) throws Exception {
        File folder = new File("GeckoDriver");
        if (!folder.isDirectory()){
            folder.mkdirs();
        }
        if (FileUtils.sizeOfDirectory(folder) < 1){
            mb.addLog("GeckoDriver does not appear to be present in the directory you're running this program from. Downloading a fresh copy to 'GeckoDriver'");
            getGeckoDriver();
            mb.addLog("GeckoDriver download complete.");
        }
        else{
            mb.addLog("GeckoDriver folder is not empty. Not modifying. Delete this folder manually (in the same directory as this program) if you are having problems.");
        }

    }
    public static void getGeckoDriver() throws Exception {
        Document dlPage = Jsoup.connect("https://github.com/mozilla/geckodriver/releases/latest").get();
        String linkTxt="geckodriver-v\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}-";
        if (isWindows()){
            if (is64bit()){
                linkTxt += "win64.zip";
            }
            else{
                linkTxt += "win32.zip";
            }
        }
        else if (isMac()){
            if (is64bit()){
                linkTxt += "macos-aarch64.tar.gz";
            }
            else{
                linkTxt += "macos.tar.gz";
            }
        }
        else if (isLinux()){
            if (is64bit()){
                linkTxt += "linux64.tar.gz";
            }
            else{
                linkTxt += "linux32.tar.gz";
            }
        }
        Elements elems = dlPage.select("[href*=/mozilla/geckodriver/releases/download/]");
        String linkURL = "";
        for (Element elem : elems){
            String txt = elem.text();
            if (elem.text().matches(linkTxt)){
                linkURL="https://www.github.com" + elem.attr("href");
            }
        }
        String filestr;
        java.net.URL urlobj = new java.net.URL(linkURL);
        if (isWindows()) {
            filestr = "./GeckoDriver.zip";
        }
        else {
            filestr = "./GeckoDriver.tgz";
        }
        System.out.println("Obtaining latest GeckoDriver...");
        if (!isWindows()){
            extractTGZ(urlobj.openStream(), "." + System.getProperty("file.separator") + "GeckoDriver");
            Process process = Runtime.getRuntime().exec("chmod +x ./GeckoDriver/geckodriver");

        }
        else{
            extractZIP(urlobj.openStream(), "." + System.getProperty("file.separator") + "GeckoDriver");
        }
        //FileUtils.copyURLToFile(urlobj, fileobj );
        System.out.println(linkURL);
    }
    public static boolean is64bit() throws Exception {
        String arch = System.getProperty("os.arch");
        boolean result = false;
        if (arch.equals("x86_64") || arch.equals("amd64")){
            result = true;
        }
        else if (arch.equals("x86")){
            result = false;
        }
        else {
            if (isMac()) {
                if (arch.equals("x86_64") || arch.equals("amd64")) {
                    result = false;
                } else if (arch.equals("aarch64")) {
                    result = true;
                }
            } else {
                throw new Exception("This tool will only work on devices with x86 or x86_64 processors (or an Apple M1 Mac).");
            }
        }
        return result;
    }
    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isLinux() {
        return (OS.indexOf("nux") >= 0);
    }
    public static void extractTGZ(InputStream in, String dst) throws java.io.IOException {
        String separator = System.getProperty("file.separator");
        GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(in);
        try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
            TarArchiveEntry entry;

            while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
                /** If the entry is a directory, create the directory. **/
                if (entry.isDirectory()) {
                    File f = new File(dst + separator + entry.getName());
                    boolean created = f.mkdir();
                    if (!created) {
                        System.out.printf("Unable to create directory '%s', during extraction of archive contents.\n",
                                f.getAbsolutePath());
                    }
                } else {
                    int count;
                    byte data[] = new byte[BUFFER_SIZE];
                    FileOutputStream fos = new FileOutputStream(dst + separator + entry.getName(), false);
                    try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
                        while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
                            dest.write(data, 0, count);
                        }
                    }
                }
            }

            System.out.println("Untar completed successfully!");
        }
    }
    public static void extractZIP(InputStream zipfile, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(zipfile);
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                byte[] bytesIn = new byte[BUFFER_SIZE];
                int read = 0;
                while ((read = zipIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                bos.close();
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */

}
