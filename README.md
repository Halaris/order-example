# Getting Started

## Requirements:

This application was made using java 21 (jdk:amazoncorretto).

It requires Docker to run the database (its also set up to run as a docker container or a spring boot app).

To run, it also requires an account on api https://currencylayer.com/. I used this api to obtain conversions from one currency to another. I used the free account that allows for 100 requests monthly.
Once obtained an account, one needs to add as an environment variable the key with the name SECRET_KEY.
You can check this name on the application.properties.

## Assumptions:

This excercise required to show a summary of different transactions for a specific period of time.
To do this summary in a more efficient manner, I saved up the conversion values of the currencies to an intermediate currency (in this case EUR).
With this, I was able to do all the calculations in the database with minimum problem, and then convert the final result to the expected currency on the server side.

This has a couple of disadvantages. 
1) For one side, we need to keep updated the conversion rates in the database.
2) On the other side, we are converting currencies one way and then the other. Making this operations over currencies could add up to math errors. I used BigDecimal to prevent this (Java library to handle decimal numbers with better precision than a float or double), and while the usual problems of arithmetics operations of float and doubles shouldn't happen, I would still recommend against this, but it is an easy way to handle this.
3) Lastly, This is calculating this summary on the database on every request. Of course, a good cache would improve this (I used a cache for the rest client that I added, but didn't for this case because the scale of the exercise, but a production app would require it). But even on that case, we would expect such a large volume that it would become costly. For this, a better db than Postgresql would be required (maybe, an analytical one like BigTable (Google cloud)).

There could be other ways of handling this. On one side, we could save the order value already converted to an intermediate currency, saving some cost on the DB side.

On the other side, we could attempt to have summaries pre-calculated, meaning that for each new order, we fire an event and update a summary corresponding for that date. This alternative leaves less control, and can be difficult to fix in case that an event is lost, but it would be much faster when making the requests.


## Missing parts:

There are some points that I didn't implemented or could be improved.

1) Using dates for setting the range of calculating the summary. Right now, I am calculating the summary of all the information in the DB (of course, something that never should be done). This should be solved by not only providing the capability to the user to provide a date range, but always having one by default. This is important also to partition the DB table by date (one of the usual partitions). It would be important to also include the logic to parse the user timezone in the request, to ensure that we are returning the range they asked.
2) Databases indexes, and database migration not depending of code. In this version I let JPA handle the database updates for easy of implementation, but for a production database is better to have better control of it, be it by having manual scripts or by forcing JPA to dump the update scripts and have a history of the changes done to the database. If the scripts are manual, this also adds the possibility of more easily add for example indexes or other configurations that would improve the application performance (for example, make indexes in the orders table y ticker and action).
3) Adding custom metrics. While Spring + prometheous will publish basic metrics, this is not going to help us to keep track of our business metrics. It would important to add for example metrics of orders added by action type.
4) Some Nice to have would be to make the action field an enum, or to add some unit tests.
5) Of course, this exercise doesn't has security or user tracking. This would be easily solved by requiring a header identifying the user that would be provided by a security layer, or simply the user token. 