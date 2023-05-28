#!/bin/bash
mkdir bin

#Linux
echo "Compiling for Linux x86_64"
go build -o bin/parasol-linux parasol.go
echo "Compiling for Linux ARM64"
GOARCH=arm64 go build -o bin/parasol-linux-arm parasol.go

#Windows
echo "Compiling for Windows x86_64"
GOOS=windows GOARCH=amd64 go build -o bin/parasol.exe parasol.go

#MacOS
echo "Compiling for MacOS x86_64"
GOOS=darwin GOARCH=amd64 go build -o bin/parasol-macos parasol.go
echo "Compiling for MacOS ARM64"
GOOS=darwin GOARCH=arm64 go build -o bin/parasol-macos-arm parasol.go

#FreeBSD
echo "Compiling for FreeBSD x86_64"
GOOS=darwin GOARCH=amd64 go build -o bin/parasol-freebsd parasol.go

#OpenBSD
echo "Compiling for OpenBSD x86_64"
GOOS=darwin GOARCH=amd64 go build -o bin/parasol-openbsd parasol.go
