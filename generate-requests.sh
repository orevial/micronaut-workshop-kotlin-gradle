#!/bin/bash

# 200
url="http://localhost:8080/cars"
echo "Going to request url $url 1000 times..."
for i in {1..1000}
do
   curl $url -s > /dev/null
done
echo "Done requesting url $url 1000 times !"

# 404
url="http://localhost:8080/cars/00000000-0000-0000-0000-000000000009"
echo "Going to request url $url 50 times..."
for i in {1..50}
do
   curl $url -s > /dev/null
done
echo "Done requesting url $url 50 times !"