package com.eugc;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;

public class GetChromeDriver {
    public static int BUFFER_SIZE = 4096;
    public static String OS = System.getProperty("os.name").toLowerCase();
    public static void getFiles(MessageBox mb) throws Exception {
        File folder = new File("ChromeDriver");
        if (!folder.isDirectory()){
            folder.mkdirs();
        }
        if (FileUtils.sizeOfDirectory(folder) < 1){
            mb.addLog("ChromeDriver does not appear to be present in the directory you're running this program from. Downloading a fresh copy to 'ChromeDriver'");
            getChromeDriver(mb);
            mb.addLog("ChromeDriver download complete.");
        }
        else{
            mb.addLog("ChromeDriver folder is not empty. Not modifying. Delete this folder manually (in the same directory as this program) if you are having problems.");
        }

    }
    public static void getChromeDriver(MessageBox mb) throws Exception {
        String chromeVersion;
        if (isWindows()){
            chromeVersion=getVersionWindows(mb);
        }
        else {
            chromeVersion=getVersionPosix(mb);
        }
        Boolean autoDownload = false;
        try{
            autoDownload = Boolean.parseBoolean(PrefFile.getSettings("AutoDownload"));
        }
        catch(Exception e){
        }
        String driverVersion = PrefFile.getSettings("ChromeVersion");
        if (driverVersion == null || driverVersion.equals("auto")){
            String url = "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_" + chromeVersion;
            try{
                Document verPage = Jsoup.connect(url).get();
                Elements elems = verPage.getElementsByTag("body");
                Element elem = elems.get(0);              
                driverVersion = elem.ownText();

            }
            catch(Exception e){
                mb.addLog("Wasn't able to securely ascertain the required ChromeDriver version. Please visit " + url + " manually and add the text from that page to NIEBot.ini as 'ChromeVersion = <version>'");
            }
        }
        
        String linkTxt="chromedriver_";
        if (isWindows()){
            linkTxt += "win32.zip";
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
                linkTxt += "linux64.zip";
            }
            else{
                linkTxt += "linux32.zip";
            }
        }
        String linkURL="https://chromedriver.storage.googleapis.com/" + driverVersion + "/" + linkTxt;
        String filestr;
        java.net.URL urlobj = new java.net.URL(linkURL);
        filestr = "./ChromeDriver.zip";
        if (autoDownload.equals(false)){
            DownloadComponent dc = new DownloadComponent();
            
            String title = "Download Required";
            String msg1 = "In order to use this software, we require you to download an additional component known as ChromeDriver. \nFor security reasons, we can not attempt to download it automatically.\n\n please visit the following link:";
            String msg2 = "Then click 'Import Zip' and in the file selection box, provide the downloaded '" + linkTxt + "'";
            
            dc.setTitle(title);
            dc.updateDialog(msg1, msg2, linkURL);
            dc.setVisible(true);
            while (dc.isVisible()){
                TimeUnit.SECONDS.sleep(1);
            }
            File zip = dc.chosen;
            if (zip == null){
                System.exit(0);
            }
            InputStream zipstream = new FileInputStream(zip);
            
            extractZIP(zipstream, "." + System.getProperty("file.separator") + "ChromeDriver");
            if (!isWindows()) {
                Process process = Runtime.getRuntime().exec("chmod +x ./ChromeDriver/chromedriver");
            }
            mb.addLog("ChromeDriver zip extracted!");
        }
        else{
        mb.addLog("Obtaining latest compatible ChromeDriver...");
        if (!isWindows()){
            extractZIP(urlobj.openStream(), "." + System.getProperty("file.separator") + "ChromeDriver");
            Process process = Runtime.getRuntime().exec("chmod +x ./ChromeDriver/chromedriver");

        }
        else{
            extractZIP(urlobj.openStream(), "." + System.getProperty("file.separator") + "ChromeDriver");
        }
        
        }
        String execName = "chromedriver";
            if (isWindows()){
                execName = execName + ".exe";
            }
        mb.addLog("Patching ChromeDriver");
        Charset isoCharset = Charset.forName ("ISO-8859-1");
        String currentPath = new java.io.File(".").getCanonicalPath();
        File executable = new File(currentPath + System.getProperty("file.separator") + "ChromeDriver" + System.getProperty("file.separator") + execName);
        byte[] fileContent = Files.readAllBytes (executable.toPath ());
        String content = new String (fileContent, isoCharset);
        String giveaway = "cdc_asdjflasutopfhvcZLmcfl_";
        String replacement = "cdc_" + genRandom(16, false) + genRandom(2, true) + genRandom(4, false) + "_";
        content = content.replace (giveaway, replacement);
        fileContent = content.getBytes (isoCharset);
        Path out = Files.write(executable.toPath(), fileContent);
        //FileUtils.copyURLToFile(urlobj, fileobj );
        System.out.println(linkURL);
        mb.addLog("ChromeDriver Patched!");
    }
    
    private static String getVersionPosix(MessageBox mb){
        String result;
        String[] cmd = {"google-chrome", "--version"};
        try (InputStream inputStream = Runtime.getRuntime().exec(cmd).getInputStream();
                Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
            result = s.hasNext() ? s.next() : null;
        } catch (IOException e) {
            mb.addLog("Error detecting installed version of Chrome! Is Chrome installed?");
            return(null);
        }
        mb.addLog(result);
        String chromeVersion;
        result = result.replace("Google Chrome ", "");
        result = result.replace("\n", "");
        String[] resultArray = result.split("\\.");
        chromeVersion = resultArray[0] + '.' + resultArray[1] + '.' + resultArray[2];
        return(chromeVersion);
        
    }
    private static String getVersionWindows(MessageBox mb){
        try{
            PowerShellResponse response = PowerShell.executeSingleCommand("(Get-Item \"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\").VersionInfo | Select ProductVersion -ExpandProperty ProductVersion");
            String[] resultArray = response.getCommandOutput().split("\\.");
            String chromeVersion = resultArray[0] + '.' + resultArray[1] + '.' + resultArray[2];
            return(chromeVersion);
        }
        catch (Exception e){
            mb.addLog("Error detecting installed version of Chrome! Is Chrome installed?");
            return(null);
        }
        
          
    }
    private static String genRandom(int length, Boolean upper){
        int leftLimit;
        int rightLimit;             
        if (upper){
           leftLimit = 65; // letter 'a'
           rightLimit = 90; // letter 'z' 
        }
        else{
            leftLimit = 97; // letter 'a'
            rightLimit = 122; // letter 'z'
        }
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
        return (generatedString);
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
