
export CLASSPATH="."
for F in `find lib -name *.jar`
do
  CLASSPATH="$CLASSPATH:$F"
done

java com.biosimilarity.emeris.RunBiosimServer
