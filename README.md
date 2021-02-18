# River Tech Bank
# Introduction
Welcome to the new banking services offered by River Tech developed by Pavlo Rosa with ♥. 

The goal of this project is to design and implement a RESTful API, backing service and data model to create bank accounts and transfer money between them. Interaction with API will be using HTTP requests.

## Installation
This is a [Spring boot](https://spring.io/projects/spring-boot) project using Maven and Java 8.
Make sure you have these tools installed in order to continue.

## Quick Start
Before running the program make sure you have the dependencies installed. You can use the following command:
```bash
mvn install
```

In order to run the program, you can use your favorite IDE or also run:
```bash
mvn spring-boot:run
```

**NOTE: The database is temporary in memory. Each time you run the program the database is created and destroyed.**

## Features
* CRUD bank clients 
* Management accounts
* Create and check transactions

## Design

### Database
![DataBase design](https://prv-projects.s3-eu-west-1.amazonaws.com/databaseDesign.png)
* All the tables have created and updated dates. Useful for futures features.
* The list of operations are saved in the database to be able to restrict the type of operations form the database itself.
* All operations are considered transactions
    *   **Deposit:** amount always positive
    *   **Withdrawal:** amount always negative
    *   **Transference:** amount positive; a transfer generates two transactions, a negative one for the source account and a positive one for the destination account.
        * Transaction.transfecerence_id refers to this operation (null when operations != transference)
* By default, if the parent is deleted, the children will be deleted too. Although in a real situation, it would not delete the instantiations but leave them as invalid through another field called 'state'.
* The balance of the last transaction always matches account.balance
    * Why has it been decided to have the balance in the account? This means there are duplication?
      To obtain the balance of an account we could make a query looking for that last transaction and in case of not finding it put as 0. However, transaction.balance is considered as the historical balance and account.balance is considered as the current balance. Besides, the transaction table will grow very fast, and consulting the balance of an account is something very frequent.
      The user will be interested in seeing his last transactions, not all of them, therefore, in this way, the information is more accessible and the attributes are differentiated.
      
### Structure
The project has been developed as a singleton MVC backend.
- **/src/ .. /**
    - **java/ ..packages../**
        - **config/** : Manage the application configuration. Mainly provides the configuration of Swagger and the AuditorProvider to manage the attributes of createdDate and updatedDate without duplicating code in each entity. 
        - **controller/** : The controllers of the enabled resources.
        - **dto/** : There are the bean that represent the Data Transfer Object pattern. This layer is used to separate the objects that are sent/received from the entities in our database. Why? 1. Avoid unnecessary queries. 2. Don't show how our database entities is designed.
        - **entity/** : ORM entities.
        - **exception/** : ExceptionHandler and Customized exceptions.
        - **mapper/** : It contains mappers that link an entity with its entityDto and vice versa.
        - **repository/** : This layer represents the Data Access Object (DAO) pattern. Note: The @Repository annotation has not been added in order to restrict access to unwanted resources.
        - **service/** : Mainly provides the business logic.
        - **utils/** : Manage auxiliary methods
   -  **../resources/ .. /**
      - **application.properties** : Application properties
      - **Bank-Service-PavloRosa.postman_collection.json** : This file contains an example of all the calls enabled to test this solution. Import this file into Postman and enjoy :)
      - **data.sql** : Example data for database.
      - **requests.http** : Some call examples (no important).
* **/test/** : This replicates the structure of src, and it is where the tests are located.

### URL Design
The solution provides a context path. This context path is:'
```
{host}/api/ //in postman the local variable {{host}} = 
http://localhost:8080/api
```
The bank clients are users:
```
GET     {{host}}/users/
GET     {{host}}/users/{userId}
POST    {{host}}/users/
PUT     {{host}}/users/
```
When the user wants to view all their accounts information or create a new account: 
```
GET     {{host}}/users/{userId}/accounts
POST    {{host}}/users/{userId}/accounts
```
The rest of the account operations, it will not be taken from the user's base, it will be taken from the account itself. Why?
1. The main entity of the service is the account. The account is depends on the user, but it is the account that is important.
2. Avoid redundant queries.
3. Easier to read.
```
        //Have to unnecessarily corroborate userId with the account
GET     {{host}}/users/{userId}/accounts/{accountId}/transactions  // NO
POST    {{host}}/accounts/{accountId}/transactions  // YES
```
These URLs are:
```
POST    {{host}}/accounts/{accountId}/transactions 
POST    {{host}}/accounts/{accountId}/transactions 

```