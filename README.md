# dvesta-gateway
dvesta-gateway. Supports client system to connect to gateway and listen for commands.

## Release notes
* 2016-03-20
  * Added support for camera image capture
* 2016-03-07
  * Created

## Usage

Deploy docker container:
```
mvn -B versions:set -DnewVersion=$BUILD_NUMBER
mvn clean install
bash -ex deploy.sh prod
```

Exposes port 8093.