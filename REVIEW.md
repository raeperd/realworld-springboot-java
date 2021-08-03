#Review of realworld-spring-boot-java
## Infrastructure:
#### - The project has only one application.properties file, I think this is a mistake it should have two, one to use in development and another one to use in production;
#### - The development application.properties should enable the H2 console in the browser too;
#### - The SecurityConfiguration should have two profiles too and should implement de role system that already exist in the spring boot security
#### - Should implement personalized exceptions;
#### - Should implement a controller advice to handle the api exceptions.

## Controller
#### - Not all the endpoints are working properly;
#### - I think the ideal controller should always return the ResponseEntitu, makes the code more cohesive.

## Repository
#### - The Comment class doesn't have a Repository interface to communicate more efficiently with the database;