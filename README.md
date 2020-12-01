
docker-compose up -d

docker-compose scale coordinator=1 member=4

docker-compose exec coordinator riak-admin cluster status

docker-compose logs web

docker stop $(docker ps -aq)