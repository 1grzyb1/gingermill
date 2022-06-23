docker build -t grzybek/gingermill .
ssh grzybek docker kill $(ssh grzybek docker ps -q --filter ancestor=grzybek/gingermill )
docker save grzybek/gingermill | bzip2 | pv | ssh grzybek docker load
ssh grzybek docker run -p 8082:8081 --net=host -d grzybek/gingermill