#!/bin/bash
mkdir bin

#Linux
echo "Compiling for Linux x86_64"
go build -o bin/parasol parasol.go
zip -v -m -3 parasol-linux.zip bin/parasol
echo "Compiling for Linux ARM64"
GOARCH=arm64 go build -o bin/parasol parasol.go
zip -v -m -3 parasol-linux-arm.zip bin/parasol

#Windows
echo "Compiling for Windows x86_64"
GOOS=windows GOARCH=amd64 go build -o bin/parasol.exe parasol.go
zip -v -m -3 parasol-windows.zip bin/parasol.exe

#MacOS
echo "Compiling for MacOS x86_64"
GOOS=darwin GOARCH=amd64 go build -o bin/parasol parasol.go
zip -v -m -3 parasol-macos.zip bin/parasol
echo "Compiling for MacOS ARM64"
GOOS=darwin GOARCH=arm64 go build -o bin/parasol parasol.go
zip -v -m -3 parasol-macos-arm.zip bin/parasol

#FreeBSD
echo "Compiling for FreeBSD x86_64"
GOOS=darwin GOARCH=amd64 go build -o bin/parasol parasol.go
zip -v -m -3 parasol-freebsd.zip bin/parasol

#OpenBSD
echo "Compiling for OpenBSD x86_64"
GOOS=darwin GOARCH=amd64 go build -o bin/parasol parasol.go
zip -v -m -3 parasol-openbsd.zip bin/parasol
