# Parasol
Parasol is a cross-platform TUI File explorer inspired by Vim commands (but more verbose), designed to be minimal. Unlike most file explorers, Parasol does not use the arrow keys for navigation, instead each file and directory is assigned a number and you can interact with the files and paths through their numbers.

This repository is in very early access, this is not a complete or stable program yet

# Download

There are currently no releases yet, this is a newborn project.

I intend on supporting most known operative systems (Linux systems, Windows, MacOS, FreeBSD, OpenBSD, NetBSD) as well as x86_64 and ARM64 architectures.

For launching files, parasol uses xdg-utils open for Linux, BSD and MacOS, and for Windows it uses its equivalent thingy. This means that operative system support requires manual implementation of equivalent commands to xdg-open, commands which assume your default applications per file extension.

# Compile from source

Clone this repo and install [Go](https://go.dev/), open a terminal in the root folder of the project and run ```go build parasol.go```. Don't use compile.sh for compiling, that will build for every target platform I chose.

Note: by compiling from source you are on your own: while Parasol may mostly work on your system, it could lack certain functionality, for example xdg-open for unix-like systems.
