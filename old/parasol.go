package main

import (
    "fmt"
    "bufio"
    "os"
    "os/exec"
    "strconv"
    "strings"

    "parasolib/browser"
    "parasolib/platform"
)

var operative_system int8 = 0 //0 for unix, 1 for windows

func main() {
    check_system()
    for {
        paths := readdir(".")
        answer := read_input("")
        // var answer string;
        // _, err := fmt.Scanln(&answer) //test or switch to scanln
        // if err != nil {fmt.Println("Failed to read user input!\n Error: ", err)}
        if answer == "exit" || answer == "quit" || answer == "q" {
            return
        } else if answer == "0" {
            err := os.Chdir("..")
            if err != nil {fmt.Println("Failed to go backwards in the directory!")}
        } else {
            user_operation(answer, paths)
        }
        fmt.Println("-------------------")
    }
}

func read_input(message string) string {
    if message != "" {fmt.Println(message)}
    reader := bufio.NewReader(os.Stdin)
    line, err := reader.ReadString('\n')
    if err != nil {fmt.Println("Error reading user input!\nError: ", err)}

    var finalline string;

    for i := range line {
        if rune(line[i]) != '\n' && rune(line[i]) != '\r' {finalline += string(line[i])}
    }
    return finalline
}

func user_operation(answer string, paths []string) {
    options := [3]string{"size", "list", "exec"} //remove exec for windows
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
    //pathnum := ""
    path := 0
    if option != "list" {
        pathnum := read_input("Choose a path")
        // fmt.Println("Choose a path")
        // _, err := fmt.Scan(&pathnum)
        // if err != nil {fmt.Println("Failed to read user input!\n Error: ", err)}
        for i := range paths {
            if pathnum == strconv.Itoa(i+1) {path = i; break}
        }
    }

    switch option {
        case "list":
            list_options()
        case "exec":
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
                fmt.Println(paths[path] + ": " + size_reduced_string + " " + size_unit)
            } else {
                fmt.Println(paths[path] + ": " + strconv.FormatInt(size, 10) + " bytes")
            }
    }
    //tempstringlol := "a" //fixes windows bug thingy
    read_input("Press enter to continue")
    // fmt.Println("\nPress enter to continue")
    // _, err := fmt.Scanln(&tempstringlol)
    // if err != nil {fmt.Println("Failed to read user input!\n Error: ", err)}
}

func list_options() {
    fmt.Println("---Options---")
    fmt.Println("     exec\n     size\n     list\n     exit/quit/q")
}

func reduce_digits(size int64, size_digits int) (float64, string) {
    var size_reduced float64; var size_unit string = "bytes"

    if size_digits >= 10 {
        size_reduced = float64(size) / 1000000000; size_unit = "GB"
    } else if size_digits >= 7 {
        size_reduced = float64(size) / 1000000; size_unit = "MB"
    } else {
        size_reduced = float64(size) / 1000; size_unit = "KB"
    }
    return size_reduced, size_unit
}

