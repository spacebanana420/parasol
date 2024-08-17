# Parasol
Parasol is a simple and cross-platform terminal-based (TUI) file explorer.

Unlike most TUI file explorers, Parasol does not use the arrow keys for navigation, instead each file and directory is assigned a number and you can interact with the files and paths through their numbers.


## Download

You can download the latest release of Parasol [here](https://github.com/spacebanana420/parasol/releases)

You can run Parasol with `java -jar parasol.jar` or, if you are on a x86_64 Linux system, you can run the binary directly with `./parasol`.

For a help screen and list of commands, run Parasol with `-h` or `--help` command-line argument or type `help` while running Parasol.

### Requirements
* Java 11 or newer
* xdg-utils (for Unix-like systems only)

### Officially tested systems:
* Linux-based
* FreeBSD

### Systems that should work:
* Windows
* Other BSD systems (OpenBSD, etc)
* MacOS
* Systems that use the xdg desktop standard


## Building Parasol
You can run the commands:

```
javac src/*.java src/*/*.java --release 11
cd src
jar cfe ../build/parasol.jar main *.class */*.class
```

Alternatively, you can run the build script with ```bash build.sh```.
