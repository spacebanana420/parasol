function bootstrap
  exec scala-cli --power package src -f --jvm 11 --assembly build/parasol-bootstrap.jar
end
function light
  exec scala-cli --power package src -f --jvm 11 --assembly --preamble=false build/parasol.jar
end
function native
  exec native-image --no-fallback --static -O3 -jar build/parasol.jar -o build/parasol
  exec 7z a build/parasol-linux-x86_64.zip build/parasol -mx3 -mmt8
end

if $0 == bootstrap
  call bootstrap
  stop
endif

if $0 == full
  call light
  call bootstrap
  call native
  stop
endif

if $0 == native
  call native
  stop
endif

call light

