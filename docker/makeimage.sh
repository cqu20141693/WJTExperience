#!/bin/bash

repository=$1
version=$2
isPush=$3
docker build -f Dockerfile --no-cache -t ${repository}:${version} .
if [ $? -eq 0 ]; then
  echo "build success"
else
  echo "build failed"
  return
fi
if [ ${isPush}=='push' ];then
  docker push ${repository}:${version}
  if [ $? -eq 0 ]; then
    echo "push Success"
  else
    echo "push failed"
  fi
else
  echo "push filtered"
fi
