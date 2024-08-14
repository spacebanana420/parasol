# Parasol
Parasol is a cross-platform terminal-based (TUI) file explorer. Unlike most TUI file explorers, Parasol does not use the arrow keys for navigation, instead each file and directory is assigned a number and you can interact with the files and paths through their numbers.

This project was also a headstart to learn Java and Go, so while it's now developed in Java, the legacy Go code is kept in the "old" folder.

More features will come in future versions.

## Download

You can download the latest release of Parasol [here](https://github.com/spacebanana420/parasol/releases)

### Requirements
* Java 9 or newer

### Officially tested systems:
* Linux-based
* FreeBSD

### Systems that probably work:
* Windows
* Other BSD systems (OpenBSD, etc)
* MacOS
* Systems that use the xdg desktop standard


## List of current commands
* help - opens this menu
* size [number] - gets the size of the file which is assigned to [number]
* find [name] - finds entries that contain [name] in their name
* exec [number] - executes the file which is assigned to [number]
* archive - archives all files in current directory
     * Note: there is currently no implementation to extract the archive, this is an experimental command
* mkdir [name] - creates a directory with name [name]
* rename [number] [name] - renames the path of value [number] to [name]
* goto [name] - changes location to the absolute path [name]


## How to build
* Install JDK or OpenJDK (recommended version 9 or above)
* Open a terminal in the root of the project
* Type the commands (not tested on cmd or powershell):
```
javac main.java
jar cfve parasol.jar main *.class */*.class
```
* Alternatively, you can run the build script with ```bash build.sh```

Parasol is now compiled and packaged into parasol.jar

Remember to delete the .class files


If you are curious about the code written in Go, that's the old version of Parasol before I rewrote it in Java. The code is located in the directory "old", as well as its respective build script.
