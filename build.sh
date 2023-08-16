function cleanup () {
    rm *.class
    for i in *
    do
        if [[ -d $i ]]
        then
            cd "$i"
            cleanup
            cd ..
        fi
    done
}

javac main.java
jar cfve program.jar main *.class */*.class
cleanup
