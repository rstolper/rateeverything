declare -x PORT=$OPENSHIFT_DIY_PORT
declare -x HOST=$OPENSHIFT_DIY_IP
declare -x H2DB="~/app-root/data/h2db"
java -cp server/target/classes:server/target/dependency/* com.romanstolper.rateeverything.startup.${1}
