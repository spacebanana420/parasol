# Parasol
Parasol is a simple and cross-platform terminal-based (TUI) file explorer.

Unlike most TUI file explorers, Parasol does not use the arrow keys for navigation, instead each file and directory is assigned a number and you can interact with the files and paths through their numbers.

<p align="center">
<img src="images/parasol.png" width="400"/>
</p>


## Download

You can download the latest release of Parasol [here](https://github.com/spacebanana420/parasol/releases)

You can run Parasol with `java -jar parasol.jar`.

For a help screen and list of commands, run Parasol with `-h` or `--help` command-line argument or type `help` while running Parasol.

### Requirements
* Java 11 or newer
* xdg-utils **(for Unix-like systems only)**
* lsblk (for listing disks on Linux) **(optional)**

### Officially tested systems:
* Linux-based
* FreeBSD

### Systems that should work:
* Windows
* Other BSD systems (OpenBSD, etc)
* MacOS
* Other systems that have xdg-utils


## Building Parasol

To build Parasol, make sure you have Java 11 or newer installed in your system.

You can run the commands:

```
mkdir build
javac src/*.java src/*/*.java --release 11
cd src
jar cfe ../build/parasol.jar main *.class */*.class
cd ..
```

Alternatively, you can run the build script with ```bash build.sh```.


<p align="center">
<img src="images/boo.png" width="200"/>
</p>
