# Parasol
Parasol is a cross-platform TUI File explorer inspired by Vim commands (but more verbose), designed to be minimal. Unlike most file explorers, Parasol does not use the arrow keys for navigation, instead each file and directory is assigned a number and you can interact with the files and paths through their numbers.

This repository is in very early access, this is not a complete or stable program yet

# How to use
Launch Parasol from a commandline, the first thing that shows up is your current directory

Parasol lists files and directories separated from each other, each assigned to a number.

To open a file or directory, type its respective number, or type "0" to go backwards in the directories

To perform an operation on a file, type the name of the operation and then the number of a file

To know the list of available operations, type "list"

To quit, type "exit", "quit" or "q" (or ctrl + c lol)

More features will come in future versions

# Download

There are currently no releases yet, this is a newborn project.

I intend on supporting most known operative systems (Linux systems, Windows, MacOS, FreeBSD, OpenBSD, NetBSD) as well as x86 and ARM architectures, 32 and 64bit.

For launching files, parasol uses xdg-utils open for Linux, BSD and MacOS, and for Windows it uses its equivalent thingy. This means that operative system support requires manual implementation of equivalent commands to xdg-open, commands which assume your default applications per file extension.

# Compile from source

Clone this repo and install [Go](https://go.dev/), open a terminal in the root folder of the project and run ```go build parasol.go```. Don't use compile.sh for compiling, that will build for every target platform I chose.

Note: by compiling from source you are on your own: while Parasol may mostly work on your system, it could lack certain functionality, for example xdg-open for unix-like systems.
