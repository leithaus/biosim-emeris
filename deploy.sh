
pushd client
ant clean build
popd

pushd server-mock
mvn-sk clean install dependency:copy-dependencies
popd

rm -rf dist
mkdir dist/
mkdir dist/webapp/
mkdir dist/lib/

cp -r client/war/ dist/webapp/
cp server-mock/target/*.jar dist/lib/
cp server-mock/target/dependency/*.jar dist/lib/
mv dist/webapp/WEB-INF/lib/*.jar dist/lib/

rm -rf dist/webapp/WEB-INF
cp -r server-mock/src/main/webapp/WEB-INF/ dist/webapp/WEB-INF/
cp -r server-mock/log.conf dist/
cp -r server-mock/src/main/scripts/run.sh dist/
cp -r server-mock/database dist/webapp/database

find dist -name .svn | xargs rm -rf

rsyncx --delete dist/ ratchet:/opt/biosim-demo/


