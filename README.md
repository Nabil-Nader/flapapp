# FlapKap App
In this exercise we want you to design an API for a vending machine, allowing users
with a “seller” role to add, update or remove products, while users with a “buyer” role
can deposit coins into the machine and make purchases. Your vending machine
should only accept 5, 10, 20, 50 and 100 cent coins


#Feature 
This is simple follow for challenge according to pdf

# Technologies Used


| Project Kazyon DashBoard | Version |
|--------------------------|---------|
| Spring Boot              | 3.2.3   |
| Java                     | 17      |        |
| Spring Security          | 6       |
| Datbase                  | H2      | 

We also added a sql script so each time it will load some user data like buyer and seller and some product into database
keep in mind that all user have the same password which is```pass123 ```

#Installation
This is a simple spring boot app, we will need to hava Java installation on your machine then just run the app
#To Run

##Option 1
you can go into folder /src/main/java/ 
then go int this class 'FlapKapApplication' and run it

##Option2
use this command if you have mvn install

```mvn spring-boot:run```

#Feature
App with build with jwt token and role based security 

#Endpoint 
this is a simple postman collaction with some of the endpoint
``` https://documenter.getpostman.com/view/31760171/2sA2xb6axP```
