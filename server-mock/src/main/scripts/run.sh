
export CLASSPATH="."
for F in `find lib -name *.jar`
do
  CLASSPATH="$CLASSPATH:$F"
done

java "-Dports=${PORTS}" com.biosimilarity.emeris.RunBiosimServer
