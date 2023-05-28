# Parasol
Parasol is a cross-platform CLI File explorer inspired by Vim commands, designed to be minimal and fully keyboard-centric. Unlike most file explorers, Parasol does not use the arrow keys for navigation, instead each file and directory is assigned a number and you can operate with the files and paths through their numbers.

This repository is in very early access, this is not a complete or stable program yet

# Download

There are currently no releases yet, this is a newborn project.

I intend on supporting most known operative systems (Linux systems, Windows, MacOS, FreeBSD, OpenBSD, NetBSD) as well as x86_64 and ARM64 architectures.

For launching files, parasol uses xdg-utils open for Linux, BSD and MacOS, and for Windows uses its equivalent thingy. This means that operative system support requires manual implementation of equivalent commands to xdg-open, commands which assume your default applications per file extension.

# Compile from source

Clone this repo and install Go, open a terminal in the root folder of the project and run ```go build parasol.go```

Note: by compiling from source you are on your own: while Parasol may be mostly work on your system, it could lack certain functionality
