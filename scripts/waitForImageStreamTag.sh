#! /bin/bash

echo "Waiting for ImageStreamTag $1 to become available..."
until oc get imagestreamtag $1 &> /dev/null; do
    printf '.'
    sleep 1
done
echo " Success"
