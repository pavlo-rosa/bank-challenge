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
In this section I will explain the design and the reasons for these decisions.
#Database
![DataBase design](https://prv-projects.s3-eu-west-1.amazonaws.com/databaseDesign.png)
* All the tables have created and updated dates. Useful for futures features.
* We save the list of operations to be able to restrict the type of operations to be performed from the database.
* All operations are considered transactions
    *   **Deposit:** amount always positive
    *   **Withdrawal:** amount always negative
    *   **Transference:** amount positive; a transfer generates two transactions, a negative one for the source account and a positive one for the destination account.
        * Transaction.transfecerence_id refers to this operation (null when operations != transference)
* By default, if the parent is deleted, the children will be deleted too. Although in a real situation, it would not delete the instantiations but leave them as invalid through another field called 'state'.
    