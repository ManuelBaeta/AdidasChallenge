# EurekaServer

It is a Spring boot application based on Spring Cloud Netflix Eureka to provide an out of the box service registry.
A Service Registry is a yellow page containing info about all the services on a microservice architecture.

## Getting Started

Clone code: 
```
git clone https://github.com/mbolori/EurekaServer.git
``` 

### Prerequisites

You need maven installed on local.
In order to build the project: 
```
mvn clean package
```

### Running program

In order to run jar:
```
java -jar EurekaServer-1.0.0-SNAPSHOT.jar --spring.config.location=/<path-to-configuration/application.yml  
```

## Configuration

It does not need an external configuration file. Inner configuration file should fit most of the usages:
An EurekaServer on standalone mode, preferring ip addresses over hostnames (easy to work with when using docker containers).

## Authors

* **Manuel Baeta** - *Initial work* 

