# Adidas Challenge
Challenge Project consists of 4 projects:

## EurekaServer
It is a Spring boot application based on Spring Cloud Netflix EurekaServer acting as service registry, it is used by all the running microservices. 
It listens on port 8761 (default). Once “eureka client” microservices register with EurekaServer, they receive periodic updates about surrounding services (its status and running instances URL).
Service registry provides flexibility, resilience to failure (fault tolerant) and a way to scale up, to microservices architectures:
* Microservices are not tied to predefined URL, they use service name to look up others, this makes architecture flexible, easy to scale (running more instances of a service) and fault tolerant.

## Zuul
It is a Spring boot application based on Spring Cloud Netflix Zuul to provide an out of the box Edge Router/load balancer. 
It listens on port 9100, it takes care of routing external requests towards "internal services", based on request destination URL providing out of the box:
* Load-balancing between "internal services" instances (using Netflix Ribbon Client Load Balancer and Eureka together).
* Circuit breakers using Netflix Hystrix.
* It is a Eureka client, so services URL are known on the fly as provided by Eureka Server (assuming “internal services” are Eureka clients).

It has been added some features on top of Base Zuul:
* a couple of filters: PreFilterZuul and PostFilterZuul to add pre and post routing easy to read logs.

## Routes.Store.Service
Microservice providing storage services for routes: retrieval, insertion and deletion. It listens on port 9305.
It uses h2 and spring-data-jpa to implement the repository layer.
It is a Eureka client.
* See Api definition (/AdidasChallenge/StoreRoutes.apib)

## Routes.Optimizer.Service
Microservice providing optimized itineraries service: given an origin city calculates the shortest path in time and then shortest path in hops from the origin city to all stored destinations. It listens on port 9405.
It relies on Routes.Store.Service to get available routes, then uses Dijkstra algorithm to calculate best itineraries:
* Less travel duration optimized: directed graph with weight equal to travel duration: (departure – arrival)
* Hops optimized: directed graph using the same weight on all the edges.

It is a Eureka client.
* See Api description using Api Blueprint (/AdidasChallenge/OptmizedItineraries.apib)

## Libraries used by all projects

### org.springframework.boot:spring-boot-starter-parent (1.5.9.RELEASE)
* Inheriting from this POM parent, It is the best way to depend on spring boot projects and third party libraries in a proper way, using internally a Spring Boot BOM: It defines for each dependency the approved version, ie: all external (Jackson, guava, etc...) and internal dependencies (within Spring framework) are guaranteed to work well together.

### org.springframework.boot:spring-boot-starter-web
* Add base web application infrastructure: embedded Tomcat and Spring MVC (Model View Controller) framework. Besides it is a pre-requisite for other dependencies used as actuator.

### org.springframework.boot:spring-boot-starter-actuator
* Enable actuator endpoints, a set of production-ready endpoints that allows application monitoring such /health, /info, /metrics. 
* On the application.yml can be tweaked those endpoints, ie: if they are enabled, or if they are sensitive (providing less information to unauthenticated requests)..

## Libraries used by EurekaServer

###org.springframework.cloud:spring-cloud-starter-eureka-server (1.3.5.RELEASE)
* It converts spring boot application EurekaServer into a real EurekaServer. 
* It is required to add some specific configuration on the application.yml.
* See EurekaServer readme (/AdidasChallenge/EurekaServer/readme.md) for more details.

## Libraries used by Zuul

### org.springframework.cloud:spring-cloud-starter-zuul (1.3.5.RELEASE)
* It converts spring boot application Zuul into a real Zuul edge-router. 
* It is required to add specific configuration on application.yml: to match request patterns with services, ribbon and hyxtrix configuration ,etc . 
* See Zuul readme (/AdidasChallenge/Zuul/readme.md) for more details.

### org.springframework.cloud:spring-cloud-starter-eureka (1.3.5.RELEASE)
* It enables this application to act as an Eureka Client. It is required specific configuration on application.yml to be able to reach EurekaServer.

## Libraries used by Routes.Store.Service

### spring-boot-starter-data-jpa
* It is used to provide a jpa based repository. This library is very handy as its CrudRepository does all of the heavy lifting leaving me just to add a method for a specific query (check if a route is present before inserting). 
* Persistance Provider (ORM) by default is Hibernate.

### com.h2database.h2
* It is a Java in-memory Database, if spring boot detects it in the classpath it is auto configured (no connection string, user, password, etc..  required).

### org.springframework.cloud:spring-cloud-starter-eureka (exclude javax.ws.rs: jsr311-api)
* It enables this application to act as an Eureka Client. 
* It is required specific configuration on application.yml to be able to reach EurekaServer.
* A exclusion is required because eureka uses jax-rs 1.x and on the project it is also used JAX-RS 2.x to implement REST handling.

### org.springframework.boot:spring-boot-starter-jersey
* It is the JAX-RS 2.X Jersey implementation, used to implement REST features. Use of JAX-RS 2.X over Spring MVC is a matter of preference, I feel more comfortable using jersey.

### org.springframework.boot:spring-boot-starter-test
* It includes several dependencies such JUnit, Hamcrest and Mockito to perform unit testing.

## Libraries used by Routes.Optimizer.Service

### org.springframework.cloud:spring-cloud-starter-eureka (exclude javax.ws.rs: jsr311-api)
* It enables application to act as a Eureka Cllient. It is required specific configuration on application.yml to be able to reach EurekaServer.
A exclusion is required because Eureka uses jax-rs 1.x and I use jax-rs 2.x  to define REST resource classes.

### org.springframework.boot:spring-boot-starter-jersey 

### org.springframework.boot:spring-boot-starter-test

### joda-time:joda-time 
* It is a  powerful data and time handling library. It is used on the project to calculate the period of time between arrival and departure times; this period is converted into minutes that are used as cost on a directed graph.

### es.usc.citius.hipster:hipster-all
It is a library for heuristic search, in the context of the project it is used to solve Dijkstra algorithm. 
For the project, it is required to optimize by time and by hops:
* By time: It makes a weighted directed graph, using minutes as weight, this way Dijkstra solves  the shortest path (in time).
* By hops: It makes a weighted directed graph, using the same weight (1), this way Dijkstra solves the shortest path ( less number of hops required).

## Download code
* First, clone project from Github
```
https://github.com/ManuelBaeta/AdidasChallenge.git
``` 

Directory structure is quite straightforward:
* On parent folder: parent pom file, docker-compose.yml and API Blueprint files (StoreRoutes.apib and OptmizedItineraries.apib) and 2 Postman collection exported as Store.Route.Service.postman_collection.json and Fill_Routes.postman_collection.json
* On EurekaServer folder: pom file, Dockerfile and readme.md
* On Zuul folder: pom file, Dockerfile and readme.md
* On Routes.Store.Service folder: pom file, Dockerfile 
* On Routes.Optimizer.Service folder: pom file, Dockerfile

## Build application
* Second, navigate to <path-to>/AdidasChallenge and execute maven
```
cd <path-to>/AdidasChallenge
mvn clean package
``` 

Expected Results: All ok, 4 jars were built.
```
[INFO] ------------------------------------------------------------------------  
[INFO] Reactor Summary:  
[INFO]  
[INFO] ADIDAS_CHALLENGE ................................... SUCCESS [  0.155 s]   
[INFO] EurekaServer ....................................... SUCCESS [  6.150 s]  
[INFO] Zuul ............................................... SUCCESS [  1.429 s]  
[INFO] Routes.Optimizer.Service ........................... SUCCESS [ 16.483 s]  
[INFO] Routes.Store.Service ............................... SUCCESS [ 17.598 s]  
[INFO] ------------------------------------------------------------------------  
[INFO] BUILD SUCCESS  
[INFO] ------------------------------------------------------------------------  
[INFO] Total time: 42.602 s  
[INFO] Finished at: 2018-01-29T19:42:50+01:00  
[INFO] Final Memory: 60M/232M  
[INFO] ------------------------------------------------------------------------  
```

## Run on Dockers
REMARK:   
Dockerized microservices were deployed and tested on Windows 7 OS (“Docker Toolbox on Windows”) instead of newer “Docker for Mac” or “Docker for Windows”.  I don’t foresee any issue because of this, but when in the below chapters says "192.168.99.100" use localhost instead.


* Third, use docker-compose to build the images and run the containers in one go:
```
$ docker-compose up -d  
....  
Creating eureka-server ...  
Creating eureka-server ... done  
Creating edge-router ...  
Creating edge-router ... done  
Creating routes-store-service ...  
Creating routes-store-service ... done  
Creating routes-optimizer-service ...  
Creating routes-optimizer-service ... done  
```
REMARK: 
In case of problems with Docker image caching, try with docker-compose rm and docker-compose up -d again as suggested on [Stackoverflow](https://stackoverflow.com/questions/37454548/docker-compose-no-such-image/)

* Fourth, check that services are up and running
```
$ docker-compose ps  
          Name                        Command               State                Ports  
----------------------------------------------------------------------------------------------------  
edge-router                /bin/sh -c /usr/bin/java - ...   Up      0.0.0.0:9100->9100/tcp  
eureka-server              /bin/sh -c /usr/bin/java - ...   Up      0.0.0.0:8761->8761/tcp  
routes-optimizer-service   /bin/sh -c /usr/bin/java - ...   Up      0.0.0.0:9405->9305/tcp, 9405/tcp   
routes-store-service       /bin/sh -c /usr/bin/java - ...   Up      0.0.0.0:9305->9305/tcp   
```

* Fifth, check services are fully started
```
$ docker-compose logs --follow 
....
```

## How to test
REMARKS:  
* In order to execute test, it is required to know virtual box VM IP, it is usually 192.168.99.100
* If you are not using Dockers ToolBox, disregard previous remark and use localhost instead.

```
$ docker-machine ls  
NAME      ACTIVE   DRIVER       STATE     URL                         SWARM   DOCKER        ERRORS  
default   *        virtualbox   Running   tcp://192.168.99.100:2376           v17.10.0-ce   
```

* If you are using Dockers ToolBox: EurekaServer will be reachable on http://192.168.99.100:8761 showing 3 connected applications: ROUTES-OPTIMIZER-SERVICE, ROURES-STORE-SERVICE and ZUUL-EDGE-ROUTER
* For the rest of Dockers versions: EurekaServer will be reachable on http://localhost:8761 

I have included 2 POSTMAN collections on AdidasChallenge folder:
Fill_Routes.postman_collection.json: It calls seven times to http://192.168.99.100:9100/routes-store-service/api/route to define a simple scenario with 4 cities ZAR, MAD, GUA, BAR  

Store.Route.Service.postman_collection.json: It contains 4 calls:
* Add route: http://192.168.99.100:9100/routes-store-service/api/route to test adding routes on Routes.Store.Service
* Get routes: http://192.168.99.100:9100/routes-store-service/api/route to test routes retrieval on Routes.Store.Service
* Delete route: http://192.168.99.100:9100/routes-store-service/api/route to test route deletion on Routes.Store.Service
* Get optimized route: http://192.168.99.100:9100/routes-optimizer-service/api/route?origin=ZAR to test get optimized itineraries for a origin city.

REMARK:
Modify POST collections to use use localhost instead if not using Docker ToolBox.

## Troubleshooting
Applications are packaged during maven build with an internal application.yml.

### Provide an external configuration file
It could be used  --spring.config.location=<path-to-config>/application.yml to provide a external configuration file. This would require to modify Dockerfile file.

### Problem connecting to EurekaService
All Eureka clients rely on Docker simple DNS for name resolution (resolving eureka-server name) as their application.yml configuration is: eureka.client.serviceUrl.defaultZone:    http://eureka-server:8761/eureka
