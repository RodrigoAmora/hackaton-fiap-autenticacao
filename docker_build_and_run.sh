#!/bin/bash

#############################################
### Shellscript to up Docker's containers ###
#############################################

### Application ###
rm -rf target/
source ~/.bash_profile
mvn clean install -Pdocker -DskipTests

### Docker ###
echo -e "\n\n"
echo -e "\033[01;32m##############\033[01;32m"
echo -e "\033[01;32m### Docker ###\033[01;32m"
echo -e "\033[01;32m##############\033[01;32m"
echo -e "\n"

sudo docker rmi -f rodrigoamora/rodrigo-springboot

docker-compose down
docker-compose down --rmi rodrigoamora/rodrigo-springboot

echo -e "\n\n"
echo -e "\033[01;32m###########################\033[01;32m"
echo -e "\033[01;32m### Building images.... ###\033[01;32m"
echo -e "\033[01;32m###########################\033[01;32m"
echo -e "\n\n"

sudo docker-compose build

echo -e "\n\n"
echo -e "\033[01;32m########################\033[01;32m"
echo -e "\033[01;32m### Uping containers ###\033[01;32m"
echo -e "\033[01;32m########################\033[01;32m"
echo -e "\n\n"

sudo docker-compose up -d

echo -e "\n"
echo -e "\033[01;32m###############################\033[01;32m"
echo -e "\033[01;32m### Application running!!!! ###\033[01;32m"
echo -e "\033[01;32m###############################\033[01;32m"

