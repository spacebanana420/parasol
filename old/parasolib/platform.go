package parasolib

func check_system() {
    wd, err := os.Getwd()
    if err != nil {fmt.Println("Failed to get current working directory!")}
    alphabet := "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    for i := range alphabet {
        if string(alphabet[i]) == string(wd[0]) {operative_system = 1; return}
    }
}
