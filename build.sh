#!/bin/bash
imageName=$2
imageTag=$3
profile=$4
echo "==== 프로드 빌드 시작 ===="
echo "대상 이미지 이름: $imageName"
echo "대상 이미지 태그: $imageTag"
echo "대상 프로파일: $profile"
./gradlew jib -DimageName="$imageName" -DimageTag="$imageTag" -Dspring.profiles.active="$profile"
