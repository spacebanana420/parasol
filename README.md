# Parasol
Parasol is a cross-platform TUI File explorer inspired by Vim commands (but more verbose), designed to be minimal. Unlike most file explorers, Parasol does not use the arrow keys for navigation, instead each file and directory is assigned a number and you can interact with the files and paths through their numbers.

This repository is in very early access, this is not a complete or stable program yet

# How to use
Launch Parasol from a commandline, the first thing that shows up is your current directory

Parasol lists files and directories separated from each other, each assigned to a number.

To open a file or directory, type its respective number, or type "0" to go backwards in the directories

To perform an operation on a file, type the name of the operation and then the number of a file

To execute a file, use the exec option (Windows users just need to open instead)

To know the list of available operations, type "list"

To quit, type "exit", "quit" or "q" (or ctrl + c lol)

More features will come in future versions

### List of operations:
* exec
* size
* list
* exit/quit/q

# Download

You can download the latest release of Parasol [here](https://github.com/spacebanana420/parasol/releases)

### Supported systems:
* Linux
* Windows
* MacOS
* FreeBSD
* OpenBSD

The binaries are static-linked, so Parasol should work on all Linux distros out of the box, including musl distros and NixOS

### Supported architectures:
* x86_64
* ARM64

# Compile from source

Clone this repo and install [Go](https://go.dev/), open a terminal in the root folder of the project and run ```go build parasol.go```. Don't use compile.sh for compiling, that will build for every target platform I chose.

Note: by compiling from source you are on your own: while Parasol may mostly work on your system, it could lack certain functionality, for example xdg-open for unix-like systems.
