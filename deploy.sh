#! /bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
YAML="${DIR}/oc/*.yaml"

$DIR/gradlew -b $DIR/build.gradle clean build

oc delete all --all

for filename in $YAML; do
	oc process -f $filename | oc apply -f -
done

IMAGESTREAMTAGS=("openjdk:8-slim" "filebeat-oss:6.3.2")

for i in "${IMAGESTREAMTAGS[@]}"
do
    $DIR/scripts/waitForImageStreamTag.sh $i
done

oc start-build logger --from-dir=$DIR
oc start-build filebeat