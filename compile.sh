#!/bash
mkdir bin

#Linux
echo "Compiling for Linux x86_64"
go build -o parasol parasol.go; strip parasol
zip -v -m -3 parasol-linux.zip parasol
echo "Compiling for Linux ARM64"
GOARCH=arm64 go build -o parasol parasol.go; strip parasol
zip -v -m -3 parasol-linux-arm.zip parasol

#Windows
echo "Compiling for Windows x86_64"
GOOS=windows GOARCH=amd64 go build -o parasol.exe parasol.go; strip parasol.exe
zip -v -m -3 parasol-windows.zip parasol.exe

#MacOS
echo "Compiling for MacOS x86_64"
GOOS=darwin GOARCH=amd64 go build -o parasol parasol.go; strip parasol
zip -v -m -3 parasol-macos.zip parasol
echo "Compiling for MacOS ARM64"
GOOS=darwin GOARCH=arm64 go build -o parasol parasol.go; strip parasol
zip -v -m -3 parasol-macos-arm.zip parasol

#FreeBSD
echo "Compiling for FreeBSD x86_64"
GOOS=darwin GOARCH=amd64 go build -o parasol parasol.go; strip parasol
zip -v -m -3 parasol-freebsd.zip parasol

#OpenBSD
echo "Compiling for OpenBSD x86_64"
GOOS=darwin GOARCH=amd64 go build -o parasol parasol.go; strip parasol
zip -v -m -3 parasol-openbsd.zip parasol


mv *.zip bin/
