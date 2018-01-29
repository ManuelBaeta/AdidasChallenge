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

## Routes.Store.Service
Microservice providing storage services for routes: retrieval, insertion and deletion. It listens on port 9305.
It uses h2 and spring-data-jpa to implement the repository layer.
It is a Eureka client.
See Api definition (StoreRoutes.apib)

## Routes.Optimizer.Service
Microservice providing optimized itineraries service: given an origin city calculates the shortest path in time and then shortest path in hops from the origin city to all stored destinations. It listens on port 9405.
It relies on Routes.Store.Service to get available routes, then uses Dijkstra algorithm to calculate best itineraries:
* Less travel duration optimized: directed graph with weight equal to travel duration: (departure – arrival)
* Hops optimized: directed graph using the same weight on all the edges.

It is a Eureka client.
See Api definition (OptmizedItineraries.apib)

## Libraries used by all projects

###org.springframework.boot:spring-boot-starter-parent (1.5.9.RELEASE)
* Inheriting from this POM parent, It is the best way to depend on spring boot projects and third party libraries in a proper way, using internally a Spring Boot BOM: It defines for each dependency the approved version, ie: all external (Jackson, guava, etc...) and internal dependencies (within Spring framework) are guaranteed to work well together.

###org.springframework.boot:spring-boot-starter-web
* Add base web application infrastructure: embedded Tomcat and Spring MVC (Model View Controller) framework. Besides it is a pre-requisite for other dependencies used as actuator.

###org.springframework.boot:spring-boot-starter-actuator
* Enable actuator endpoints, a set of production-ready endpoints that allows application monitoring such /health, /info, /metrics. 
* On the application.yml can be tweaked those endpoints, ie: if they are enabled, id they are sensitive (providing less information to unauthenticated requests)..

## Libraries used by EurekaServer

###org.springframework.cloud:spring-cloud-starter-eureka-server (1.3.5.RELEASE)
* It converts spring boot application EurekaServer into a real EurekaServer. 
* It is required to add some specific configuration on the application.yml.
* See EurekaServer readme for more details.

## Libraries used by Zuul

###org.springframework.cloud:spring-cloud-starter-zuul (1.3.5.RELEASE)
* It converts spring boot application Zuul into a real Zuul edge-router. 
* It is required to add specific configuration on application.yml: to match request patterns with services, ribbon and hyxtrix configuration ,etc . 
* See Zuul readme for more details.

###org.springframework.cloud:spring-cloud-starter-eureka (1.3.5.RELEASE)
* It enables this application to act as an Eureka Client. It is required specific configuration on application.yml to be able to reach EurekaServer.

## Libraries used by Routes.Store.Service

###spring-boot-starter-data-jpa
* It is used to provide a jpa based repository. This library is very handy as its CrudRepository does all of the heavy lifting leaving me just to add a method for a specific query (check if a route is present before inserting). 
* Persistance Provider (ORM) by default is Hibernate.

###com.h2database.h2
* It is a Java in-memory Database, if spring boot detects it in the classpath it is auto configured (no connection string, user, password, etc..  required).

###org.springframework.cloud:spring-cloud-starter-eureka (exclude javax.ws.rs: jsr311-api)
* It enables this application to act as an Eureka Client. 
* It is required specific configuration on application.yml to be able to reach EurekaServer.
* A exclusion is required because eureka uses jax-rs 1.x and on the project it is also used JAX-RS 2.x to implement REST handling.

###org.springframework.boot:spring-boot-starter-jersey
* It is the JAX-RS 2.X Jersey implementation, used to implement REST features. Use of JAX-RS 2.X over Spring MVC is a matter of taste, I feel more comfortable using jersey.

###org.springframework.boot:spring-boot-starter-test
* It includes several dependencies such JUnit, Hamcrest and Mockito to perform unit testing.

## Libraries used by Routes.Optimizer.Service

###org.springframework.cloud:spring-cloud-starter-eureka (exclude javax.ws.rs: jsr311-api)
* It enables application to act as a Eureka Cllient. It is required specific configuration on application.yml to be able to reach EurekaServer.
A exclusion is required because Eureka uses jax-rs 1.x and I use jax-rs 2.x  to define REST resource classes.

###org.springframework.boot:spring-boot-starter-jersey 

###org.springframework.boot:spring-boot-starter-test

###joda-time:joda-time 
* It is a  powerful data and time handling library. It is used on the project to calculate the period of time between arrival and departure times; this period is converted into minutes that are used as cost on a directed graph.

###es.usc.citius.hipster:hipster-all
It is a library for heuristic search, in the context of the project it is used to solve Dijkstra algorithm. 
For the project, it is required to optimize by time and by hops:
* By time: It makes a weighted directed graph, using minutes as weight, this way Dijkstra solves  the shortest path (in time).
* By hops: It makes a weighted directed graph, using the same weight (1), this way Dijkstra solves the shortest path ( less number of hops required).

## Download code
Clone project from Github
```
git clone https://github.com/mbolori/Zuul.git
``` 

## Build application
In order to build the application
```
mvn clean package
``` 


## Run on Dockers
* REMARK: Dockerized microservices were deployed and tested on a Windows 7 OS, therefore on a “Docker Toolbox on Windows” instead of newer “Docker for Mac” and “Docker for Windows”.  I don’t foresee any issue because of this, but if any problem arises I would 


### Docker command miscelanea


## How to test



