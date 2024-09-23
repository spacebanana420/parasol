# Parasol
Parasol is a terminal-based (TUI) file explorer and shell.

With Parasol, you can navigate your filesystem, perform file operations, view available disk devices, measure file and directory sizes, execute files, open files with your preferred applications, and run its own built-in shell either alongside the file explorer or standalone.

<p align="center">
<img src="images/parasol.png" width="390"/>
</p>


## Download

You can download the latest release of Parasol [here](https://github.com/spacebanana420/parasol/releases).

Download `parasol.jar` and run it with `java -jar parasol.jar`.

For a help screen and list of commands, run Parasol with `-h` or `--help` command-line argument or type `help` while running Parasol.

### Requirements
* Java 11 or newer
* xdg-utils **(for Unix-like systems only)**
* lsblk (for listing disks on Linux) **(optional)**

### Officially tested systems:
* Linux-based
* FreeBSD
* Haiku

### Systems that should work:
* Windows
* Other BSD systems (OpenBSD, etc)
* MacOS
* Other systems that have xdg-utils


## Building Parasol

### Using [Yuuka](https://github.com/spacebanana420/yuuka)
```
yuuka package -o parasol.jar
```

### Using Bash
```
mkdir build
bash build.sh
```

### With a Unix-like shell:
```
mkdir build
javac src/*.java src/*/*.java
cd src
jar cfe ../build/parasol.jar main *.class */*.class
cd ..
```

<p align="center">
<img src="images/boo.png" width="280"/>
</p>
