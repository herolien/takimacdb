#!/bin/bash

# parameters
mysql_hostname=172.18.0.3
mysql_port=3306
mysql_database=computer-database-db
mysql_username=user
mysql_password=pass
mysql_timezone=Europe/Paris

docker_image=takima/cdb
docker_local_http_port=8080
docker_container_http_port=8080
docker_network="--network takima"

docker_logs="target/logs/log-cdb.txt"

# build container for the spring boot project with tag takima/cdb
docker build -t takima/cdb .

# run docker 
docker run ${docker_network} -p ${docker_local_http_port}:${docker_container_http_port} ${docker_image} \
--spring.datasource.url=jdbc:mysql://${mysql_hostname}:${mysql_port}/${mysql_database}\?useLegacyDatetimeCode=false\&serverTimezone=${mysql_timezone} \
--spring.datasource.username=${mysql_username} \
--spring.datasource.password=${mysql_password} >> ${docker_logs} &
