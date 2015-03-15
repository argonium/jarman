# JarMan
JarMan is a desktop Java (Swing) application that provides easy access to the classes in a Java JAR file.

To build JarMan, use Ant to run the command 'ant clean dist'.  This will generate jarman.jar.  To run the application, use 'java -jar jarman.jar', or double-click on the file from the desktop.

Below are the major features:

* Open a jar file and view a list of its contents
* If the opened jar file has a manifest with a Class-Path entry, any files in the referenced jars are also included in the list
* The list of files can be filtered with an arbitrary string; the list will be restricted to only show files that match on filename, directory or jar file name
* The list of files can also be filtered to show all files, only files that match another file on name, or only files that match on name and content (same CRC value)
* You can right-click a file in the list to view its contents, or explore the class file to see the class's fields, methods interfaces and Strings
* The Jars page lists the opened jar file and any jar files in its manifest’s Class-Path, and highlights any referenced jar files that were not found; the number of files in and the size of each jar is also listed
* The Manifest page lists all entries in the opened jar file’s manifest (if it has one)

![JarMan](http://argonium.github.io/jm.png)

The source code is released under the MIT license.
