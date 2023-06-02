package main

import (
	"fmt"
	"os"
	"os/exec"
	"strconv"
	"strings"
)

var operative_system int8 = 0 //0 for unix, 1 for windows

func main() {
	check_system()
	for {
		paths := readdir(".")
		var answer string;
		_, err := fmt.Scan(&answer) //test or switch to scanln
		if err != nil {fmt.Println("Failed to read user input!\n Error: ", err)}
		if answer == "exit" {
			return
		} else if answer == "0" {
			err := os.Chdir("..")
			if err != nil {fmt.Println("Failed to go backwards in the directory!")}
		} else {
			user_operation(answer, paths)
		}
		fmt.Println("")
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
			user_operate(options[i], paths)
			return
		}
	}
	for i := range paths {
		if answer == strconv.Itoa(i+1) {open_path(paths[i]); return}
	}
}


func user_operate(option string, paths []string) {
	pathnum := ""
	path := 0
	fmt.Println("Choose a path")
	_, err := fmt.Scan(&pathnum)
	if err != nil {fmt.Println("Failed to read user input!\n Error: ", err)}
	for i := range paths {
		if pathnum == strconv.Itoa(i+1) {path = i; break}
	}
	switch option {
		case "exec":
			//executefile := "./"; if operative_system == 1 {executefile = ".\"} test this!!!
			cmd := exec.Command("./" + paths[path])
			err := cmd.Run()
			if err != nil {fmt.Println("Failed to execute the file with path " + paths[path] + "\n Error: ", err)}
		case "size":
			var size int64;
			measuredpath, err := os.Stat(paths[path])
			if err != nil {fmt.Println("Failed to read path info, path: " + paths[path])}
			if measuredpath.IsDir() == false {
				size = measuredpath.Size()
			} else {
				fmt.Println("Measuring the size of " + paths[path] + "...")
				size = get_dir_size(paths[path])
			}
			var size_digits int = len(strconv.FormatInt(size, 10));

			if size_digits >= 4 {
				size_reduced, size_unit := reduce_digits(size, size_digits)
				size_reduced_string := strconv.FormatFloat(size_reduced, 'f', -1, 32)
				fmt.Println(paths[path] + ": " + size_reduced_string + " " + size_unit) //fix
			} else {
				fmt.Println(paths[path] + ": " + strconv.FormatInt(size, 10) + " bytes")
			}
	}
}

func reduce_digits(size int64, size_digits int) (float64, string) {
	var size_reduced float64; var size_unit string = "bytes"

	if size_digits >= 10 {
		size_reduced = float64(size) / 1000000000; size_unit = "GB"
	} else if size_digits >= 7 {
		size_reduced = float64(size) / 1000000; size_unit = "MB"
	} else { //>=4
		size_reduced = float64(size) / 1000; size_unit = "KB"
	}
	return size_reduced, size_unit
}

func get_dir_size(dir string) int64 {
	var size int64;
	os.Chdir(dir)
	paths, err := os.ReadDir(".")
	if err != nil {fmt.Println("Failed to read current directory!"); return 0}
	for i := range paths {
		pathname := paths[i].Name()
		pathinfo, err := os.Stat(pathname)
		if err != nil {fmt.Println("Failed to read path info, path: " + pathname)}

		if pathinfo.IsDir() == false {
			size += pathinfo.Size()
		} else {
			size += get_dir_size(pathname)
		}
	}
	os.Chdir("..")
	return size
}

func open_path (path string) {
	pathinfo, err := os.Stat(path)
	if err != nil {fmt.Println("Failed to read path info, path: " + path)}
	if pathinfo.IsDir() == true {
		err := os.Chdir(path)
		if err != nil {fmt.Println("Failed to change directory! to " + path)}
	} else {
		if operative_system == 0 {
			cmd := exec.Command("open", path)
			err := cmd.Run()
			if err != nil {fmt.Println("Failed to execute the open command with path " + path)}
		} else
		{
			cmd := exec.Command("rundll32.exe", "url.dll,FileProtocolHandler", path) //this executes files too!!!!!
			//test start "" and explorer.exe instead
			err := cmd.Run()
			if err != nil {fmt.Println("Failed to execute the open command with path " + path)}
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
