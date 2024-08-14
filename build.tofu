if $0 == bootstrap
  exec scala-cli --power package src -f --assembly build/parasol.jar
  stop
endif

exec scala-cli --power package src -f --assembly --preamble=false build/parasol.jar
