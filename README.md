# UBB SOA Master Course: Social Media Project
This project was developed using a variety of technologies and simulates a simple social media platform.

It is based on a micro-service and micro-frontend architecture and it is deployable through docker.

## Setup and Usage
### Installation
You require Docker and Java 17 in order to get started with this project.

After cloning this project locally, you will need to package the 4 Java micro-services present in the folder - `restAPI`, `authenticationServer`, `emailService` and `loggingService`.

You will also need to set up a Kube cluster locally to in order to run [OpenFaaS](https://www.openfaas.com/) - run the shell file located at `faas/kind-with-registry.sh`.
You will need to set up that OpenFaaS and run the provided function - `markdown`.
Also follow the instructions at `faas/README.md`

After following these steps, simply run `docker-compose up -d` from the root folder of the project. It will be available at [localhost:3000](localhost:3000)

### SMTP Server
In place of a real email server, a dummy SMTP is also deployed in Docker as part of this project. The email list can be seen by accessing [localhost:7777](localhost:7777).

## Project Overview
This project describes a simple social media platform, where users are provided with two main functionalities - posting and following:
 - A user can create a post on their own page, similar to a classic blog post.
 - By going to the page of another user, they can see the user's posts and follow or unfollow them.

## Components
Component diagram:
![Component diagram](./diagrams/System%20Diagram.png "Component diagram")

Principles:

* All configurations related to the environment are passed through profile-specific variables activated in docker or through variables populated from the `docker-compose` file.
* All data is stored in a shared `PostgreSQL` database instance.
* The Java REST APIs have multiple instances started, with load-balancing handled by the nginx Gateway, which ensures the access from the outside.
* Registration emails are sent asynchronously using `RabbitMQ` messaging. 
* Due to having multiple instances started, for traceability, logs are gathered in the `loggingService`.
* All micro-services are stateless.
* The REST micro-services are protected through OAuth using JWTs generated and managed by the `authenticationService`.

Database model:
![Database diagram](./diagrams/Database%20Diagram.png "Database diagram")

### Authentication Service
Micro-service responsible with authentication and user management:
* Represents an OAuth Authentication Server, providing APIs for User Authentication which serve out JWTs.
* Responsible for User Registration, which generates the new User and sends a message to send a registration email.

UML diagram:
![UML diagram](./diagrams/authenticationServer%20diagram.png "UML diagram")
When registering a new user, the user receives a JWT and an email is sent to their email address as a notification.

The communication with the email service is done using RabbitMQ and the `emailsQueue` queue.

For persisting user entities, the `UserRepository` populates the `users` and `user_authentication_details` tables.

For authentication, an OAuth Authorization Server was implemented. It generates JWT tokens, using asymmetric signing using a pair of RSA keys that are provided in separate files.
The public keys are provided to resource servers using the `jwks` API in order to enable them to verify the JWT tokens.

Technological stack: Java, Spring Boot 3, JPA, Spring Rabbit

### Rest API
Micro-service responsible with User, Post and Following management. It provides basic CRUD APIs for the mentioned entities.

UML diagram:
![UML diagram](./diagrams/restAPI%20Diagram.png "UML diagram")

The whole micro-service is protected using Spring Security, set up to communicate with the Authentication Service to validate JWTs passed as Bearer tokens through the request headers, in order to identify the caller.

The micro-service is also configured to serve WebSockets. Whenever a user posts a new post, a notification is sent to all users registered to the relevant WebSocket.

The `LoggingAspect` is a class that uses aspect-oriented programming to generate logs whenever any of the exposed APIs are called. These logs are then sent to the `loggingService` using Kafka to centralize them.

The `FaaSClient` is used to communicate with `OpenFaaS` and call the exposed function, which converts markdown syntax to HTML.

As previously, persistence is achieved through the `Repository` classes that communicate with the database.

Technological stack: Java, Spring Boot 3, JPA, Spring Kafka, Spring Aspect

### Email Service
Micro-service responsible for sending registration emails. It is set up as a RabbitMQ listener to communicate with the other micro-services.

UML diagram:
![UML diagram](./diagrams/emailService%20Diagram.png "UML diagram")

`RegistrationEmailListener` is set up as a RabbitMQ listener, listening to the `EmailQueue` queue.

`EmailSender` communicates with the SMTP server set up for the application in order to send the emails.

Technological stack: Java, Spring Boot 3, Spring RabbitMQ

### Logging Service
Micro-service responsible for centralizing logs. It is set up as a Kafka Consumer to communicate with the other micro-services.

Contains a single relevant class, `LoggingListener`, which is set up as a Kafka Consumer for the `log-message` topic and centralizes the logs from multiple app instances in one place.

Technological stack: Java, Spring Boot 3, Spring Kafka

### Microfrontends
Two micro-frontends are used for the UI:
* The Container micro-frontend, which serves as a container for the app and also handles authentication and registration
* The Users micro-frontend, which provides components related to the User List and User Page.

Module Federation was used in order to implement the micro-frontends.

Technological stack: React, React-Router, webpack-module

### nginx
nginx is used as an entry point to the backend micro-services, as well as serving as a load balancer for the separate app instances.
It routes the traffic based on the following rules:

* `/app/**` - routed towards the Rest API app instances
* `/authentication/**` - routed towards the Authentication Server

It also automatically responds to OPTIONS calls with the relevant information. Additionally, it aids the websocket-connection calls with adding the upgrade headers.
Calls are otherwise not modified.

## External Systems
The system has two external dependencies:
* An SMTP server, which is currently replaced with a fake SMTP server also deployed in the Docker container.
* OpenFaaS, set up and deployed in a Kube cluster on Docker, which serves the `markdown` FaaS to convert markdown text to html.
