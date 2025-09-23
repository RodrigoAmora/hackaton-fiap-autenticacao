# hackaton-fiap-autenticacao
Descrição
---------
Micro-Serviço de cadastro e autenticação de usuário do Hackaton da pós de Arquitetura de Software da FIAP.

Participantes
-------------
* Bruno do Amor Divino da Paixão - RM360643
* Lucas Matheus Testa - RM360642
* Rodrigo de Lima Amora de Freitas - RM360219

Dependências
------------
O projeto usa o Java 17 e as seguintes dependências:

* Spring Boot 3.5.4
* Spring Security
* Spring Data JPA
* Spring dotenv
* Lombok
* Devtools
* MongoDB
* Swagger
* OpenAPI
* jUnit
* Mockito

Documentação da API
-------------------
A documentação da API pode ser vista através do Swagger e do Redoc.<br>

<b>Documentação da API via Swagger:</b>
```shell script
http://localhost:8080/swagger
```

<b>Documentação da API via Redoc:</b>
```shell script
http://localhost:8080/redoc
```

##
Na pasta <b>`Postman`</b> contém a collection para usar os endpoints via Postman.

Monitoramento do projeto
------------------------
A monitoramento do projeto para verificar a saúde da aplicação e os recursos utilizados:
```shell script
http://localhost:8080/health
```

Banco de dados
--------------
O projeto usa o MongoDB como banco da dados.

Para rodar o MongoDB localmente, baixe o MongoDB na sua máquina, descompacte em algum diretório em configure a variável de ambiente MONGO_HOME com o valor <b>`diretório_do_mongo/bin`</b>. <br>
Após isso, no terminal vá até o <b>`MONGO_HOME/bin`</b> e execute o comando abaixo para iniciar o MongoDB:
```shell script
sudo ./mongod --dbpath /usr/local/var/mongodb
```

##
Após baixar e descompactar o MongoDB na sua máquina e cria a variável de ambiente MONGO_HOME, você pode excutar o Shellscript <b>`run_mongo.sh`</b> na raiz do projeto.
```shell script
./run_mongo.sh
```

Configuração do projeto
-----------------------
Crie o arquivo <b>`.env`</b> na raiz do projeto e adicione as seguintes variáveis:
* MONGO_EXPRESS_USERNAME - Usuário do Mongo Express
* MONGO_EXPRESS_PASSWORD - Senha do Mongo Express
* MONGO_ROOT_USERNAME - Usuário do MongoDB
* MONGO_ROOT_PASSWORD - Senha do MongoDB

Gerando o arquivo .jar
----------------------
Para gerar o arquivo <b>.jar</b>, execute o comando na raiz do projeto:
```shell script
mvn clean install -P{profile} -DskipTests
```

Rodando os testes
-----------------
<b>A cobertura de testes do projeto:</b>
![A cobertura de testes do projeto:](imagens/cobertura-de-testes.png)

##

Para rodar os testes, execute o comando na raiz do projeto:
```shell script
mvn test
```

##
Para gerar o relatório de cobertura de testes do projeto com o JaCoCo, execute o comando na raiz do projeto:
```shell script
mvn clean test jacoco:report
```

<b>OBS:</b> Certifique-se de que o MongoDB esteja rodando.

Rodando o projeto localmente
----------------------------
Para iniciar a aplicação, execute o comando na raiz do projeto:

```shell script
mvn spring-boot:run
```

Rodando o projeto no Docker
---------------------------
Para rodar o projeto em um container Docker, primeiro deve-se gerar o .jar do projeto.<br>
Após isso, deve-se gerar o build das imagens e subir os containers do Docker.<br><br>
<b>Fazendo o build das imagens:</b>
```shell script
docker-compose build
```

<b>Subindo os containers do Docker:</b>
```shell script
docker-compose up -d
```

##
Para automatizar esse processo, basta executar o Shellscript <b>`docker_build_and_run.sh`</b>:
```shell script
./docker_build_and_run.sh
```

<hr>
