#!/bin/sh
# 
# usage ./clean.docker.registry.sh registryUrl login filterString
#
 
# read the password
# echo -n Password: 
# read -s password
 
user="$2:${password}"
if [ -z $user ] ; then
   userOption=""
else
   userOption="-u ${user}"
fi

dockerRegistry="$1"
imagesFilter="$3"
 
# get the list of images names that match the filter
images=$(curl -s ${userOption} ${dockerRegistry}/v2/_catalog | jq -r '.repositories[] | select(. | contains("'${imagesFilter}'")) ')
echo ${images} 
for image in $images ; do
  # get the list of tags for each image 
    tags=$(curl -s ${userOption} ${dockerRegistry}/v2/${image}/tags/list | jq -r .tags[])
 
    for tag in $tags ; do
        echo "${image}:${tag}"
        # get the digest of the image:tag
    digest=$(curl -H "Accept: application/vnd.docker.distribution.manifest.v2+json" -v -s -u ${user} "${dockerRegistry}/v2/${image}/manifests/${tag}" 2>&1  | grep -e "Docker-Content-Digest:*" | awk '{ sub(/\r/,"",$3) ; print $3 }')
        if [ -z $digest ] ; then
            echo "${image}:${tag} not found"
        else
            echo "Deleting ${image}:${tag}:${digest}"
            curl -i -X DELETE -w "[%{http_code}]\n" -s ${userOption} ${dockerRegistry}'/v2/'${image}'/manifests/'${digest}
            echo "..."
        fi
    done
done