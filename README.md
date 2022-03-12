# NIEBot
A tool to help Barcelonian people get various immigration-related appointments via the Cita previa process.

## Requirements
1. A Windows or Linux PC.
  - You MIGHT be able to get this running on macOS, and I will do my best to make sure it works there, but I do not have a Mac that I can just test this on, therefore I cannot support it.
2. Java 11, either OpenJDK or Oracle will work well.
3. A decent internet connection.

## Instructions
1. Install Java 11. [You can get the latest version from Oracle here](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
2. Run the application.
3. Use the top choice box to set the appointment type you're looking for.
4. Fill in the details. Make sure the notification option you set works for you.
5. Click OK and sit tight. The application will download a special version of Firefox known as GeckoDriver. You'll be able to see the application working in realtime in a special browser window. Keep this window open (you can minimize it if you like) and keep an eye/ear out for the notification you picked.
6. Once you see/hear the notification you picked, go back into the app's browser window, fill out the last of the details (a CAPTCHA and a 2FA code), and you now have your appointment.

## Legal
1. This app is shared under the GPLv3 license. It is open source software, and is free to download. I would highly recommend only getting the application and updates from this repository.
2. Any attempts to use this application code or portions thereof to attack any Spanish government websites are hereby outrighted condemed by the author of this application. Just don't do it.
3. The selling of the appointments that can be obtained via this application is illegal and is not supported by the author. This tool is intended to help individuals who are having a difficult time obtaining these appointments for themselves.
