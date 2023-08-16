package parasolib


func get_dir_size(dir string) int64 {
    var size int64;
    os.Chdir(dir)
    paths, err := os.ReadDir(".")
    if err != nil {
        fmt.Println("Failed to read current directory!"); return 0
    }
    for i := range paths {
        pathname := paths[i].Name()
        pathinfo, err := os.Stat(pathname)
        if err != nil {
            fmt.Println("Failed to read path info, path: " + pathname)
        }

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
    if err != nil {
        fmt.Println("Failed to read path info, path: " + path)
    }
    if pathinfo.IsDir() == true {
        err := os.Chdir(path)
        if err != nil {
            fmt.Println("Failed to change directory! to " + path)
        }
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
    currentdir, err := os.Getwd()
    if err != nil {fmt.Println("Failed to get current working directory!")}
    fmt.Println(currentdir + "\n")
    fmt.Println("---Directories---")
    fmt.Println(dirstring)
    fmt.Println("\n---Files---")
    fmt.Println(filestring)

    return allpaths
}
