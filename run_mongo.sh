#!/bin/bash

##################################
## Shellscript to start MongoDB ##
##################################


echo -e "\033[01;32m########################\033[01;32m"
echo -e "\033[01;32m### Starting MongoDB ###\033[01;32m"
echo -e "\033[01;32m########################\033[01;32m"
echo -e "\n\n"

source ~/.bash_profile

cd $MONGO_HOME/bin
sudo ./mongod --dbpath /usr/local/var/mongodb
