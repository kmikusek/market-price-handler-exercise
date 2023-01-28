# market-price-handler-exercise

## Description:
The application consists of three modules:
/main - source code
/test - unit tests
/testintegration - integration test

The easiest way to test this application is to run integration tests. They simulate new events on the message broker and then check if the appropriate values are exposed by the Rest API

## Assumption:
It was assumed that the queuing system can serve events without keeping the order according to the timestamp
