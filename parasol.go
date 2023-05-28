package main

import (
	"fmt"
	"os"
	"os/exec"
	"strconv"
	"strings"
)

var operative_system int = 0 //0 for unix, 1 for windows

func main() {
	check_system()
	for {
		paths := readdir(".")
		var answer string;
		_, err := fmt.Scanln(&answer) //replace with scan?
		//fmt.Println(answer)
		if err != nil {fmt.Println("Failed to read user input!\n Error: ", err)}
		if answer == "exit" {
			return
		} else if answer == "0" {
			err := os.Chdir("..")
			if err != nil {fmt.Println("Failed to go backwards in the directory!")}
		} else {
			user_operation(answer, paths)
		}
	}
}

func check_system() {
	wd, err := os.Getwd()
	if err != nil {fmt.Println("Failed to get current working directory!")}
	fmt.Println(wd)
	alphabet := "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	for i := range alphabet {
		if string(alphabet[i]) == string(wd[0]) {operative_system = 1; return}
	}
}

func user_operation(answer string, paths []string) {
	options := [2]string{"exec", "size"}
	for i := range options {
		if strings.Contains(answer, options[i]) == true {
			for path := range paths {
				if answer == strconv.Itoa(i+1) {user_operate(options[i], paths[path]); break}
			}
			break
		}
	}
	for i := range paths {
		if answer == strconv.Itoa(i+1) {open_path(paths[i]); break}
	}
}

func user_operate(option string, path string) {
	switch option {
		case "exec":
			//executefile := "./"; if operative_system == 1 {executefile = ".\"} test
			cmd := exec.Command("./" + path)
			err := cmd.Run()
			if err != nil {fmt.Println("Failed to execute the file with path " + path)}
		case "size":
			//something something
	}
}


func open_path (path string) {
	pathinfo, err := os.Stat(path)
	if err != nil {fmt.Println("Failed to read path info, path: " + path)}
	if pathinfo.IsDir() == true {
		err := os.Chdir(path)
		if err != nil {fmt.Println("Failed to change directory! to " + path)}
	} else {
		//var runcommand string; var extra_winarg string = ""
		if operative_system == 0 {
			//runcommand = "open"
			cmd := exec.Command("open", path)
			err := cmd.Run()
			if err != nil {fmt.Println("Failed to execute the open command with path " + path)}
		} else
		{
			cmd := exec.Command("rundll32.exe", "url.dll,FileProtocolHandler", path)
			err := cmd.Run()
			if err != nil {fmt.Println("Failed to execute the open command with path " + path)}
			//runcommand = "rundll32.exe"; extra_winarg = "url.dll,FileProtocolHandler"
		}
	}
}
/*
func string_length (instring string) int {
	var length int = 0
	for i := range instring {
		count+=1
	}
	return count
}
func longest_string (strings[]string) int {
	var count[]int
	var newcount int = 0
	var highest_len int = 0

	for i := range strings {
		newcount = 0; count = append(count, newcount)
		for char := range strings[i] {newcount += 1}
		if newcount > highest_len {highest_len = newcount}
	}

	return highest_len
}*/

func readdir(dir string) []string {
	paths, err := os.ReadDir(".")
	if err != nil {
		fmt.Println("Failed to read current directory!")
	}
	var allpaths[]string;
	var files[]string; var dirs[]string; var filenum[]int; var dirnum[]int;
	var filestring string; var dirstring string
	var files_per_line int = 0
	var dirs_per_line int = 1; dirstring += "0: ..        "
	var totalcount int = 1

	for _, path := range paths {
		allpaths = append(allpaths, path.Name())
		if path.IsDir() == true {
			dirs = append(dirs, path.Name())
			dirnum = append(dirnum, totalcount)
			if dirs_per_line == 2 {dirstring += "\n"; dirs_per_line = 0}
			dirstring += strconv.Itoa(totalcount) + ": " + path.Name(); dirstring += "         "; dirs_per_line +=1
			totalcount += 1
		} else {
			files = append(files, path.Name())
			filenum = append(filenum, totalcount)
			if files_per_line == 2 {filestring += "\n"; files_per_line = 0}
			filestring += strconv.Itoa(totalcount) + ": " + path.Name(); filestring += "         "; files_per_line +=1
			totalcount += 1
		}
	}

	// for i := range dirs {
	// 	dirnum = append(dirnum, totalcount)
	// }
	// longestfile = longest_string(files); longestdir = longest_string(dirs)

	fmt.Println("---Directories---")
	fmt.Println(dirstring)
	fmt.Println("\n---Files---")
	fmt.Println(filestring)

	return allpaths
}
