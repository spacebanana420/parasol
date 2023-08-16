#!/bash
mkdir bin

#Linux
echo "Compiling for Linux x86_64"
go build -o parasol parasol.go; #strip parasol
zip -v -m -5 parasol-linux.zip parasol
echo "Compiling for Linux ARM64"
GOARCH=arm64 go build -o parasol parasol.go; #strip parasol
zip -v -m -5 parasol-linux-arm.zip parasol

#Windows
echo "Compiling for Windows x86_64"
GOOS=windows GOARCH=amd64 go build -o parasol.exe parasol.go; #strip parasol.exe
zip -v -m -5 parasol-windows.zip parasol.exe
echo "Compiling for Windows ARM64"
GOOS=windows GOARCH=arm64 go build -o parasol.exe parasol.go; #strip parasol.exe
zip -v -m -5 parasol-windows-arm.zip parasol.exe

#MacOS
echo "Compiling for MacOS x86_64"
GOOS=darwin GOARCH=amd64 go build -o parasol parasol.go; #strip parasol
zip -v -m -5 parasol-macos.zip parasol
echo "Compiling for MacOS ARM64"
GOOS=darwin GOARCH=arm64 go build -o parasol parasol.go; #strip parasol
zip -v -m -5 parasol-macos-arm.zip parasol

#FreeBSD
echo "Compiling for FreeBSD x86_64"
GOOS=darwin GOARCH=amd64 go build -o parasol parasol.go; #strip parasol
zip -v -m -5 parasol-freebsd.zip parasol
echo "Compiling for FreeBSD ARM64"
GOOS=darwin GOARCH=arm64 go build -o parasol parasol.go; #strip parasol
zip -v -m -5 parasol-freebsd-arm.zip parasol

#OpenBSD
echo "Compiling for OpenBSD x86_64"
GOOS=darwin GOARCH=amd64 go build -o parasol parasol.go; #strip parasol
zip -v -m -5 parasol-openbsd.zip parasol
echo "Compiling for OpenBSD ARM64"
GOOS=darwin GOARCH=arm64 go build -o parasol parasol.go; #strip parasol
zip -v -m -5 parasol-openbsd-arm.zip parasol


mv *.zip bin/
