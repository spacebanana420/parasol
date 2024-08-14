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
echo Building JAR
javac src/*.java src/*/*.java
cd src
jar cfe ../build/parasol.jar main *.class */*.class
cleanup
cd ..