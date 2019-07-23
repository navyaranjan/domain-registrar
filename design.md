#  Design Documentation

This document covers the design descriptions made for the Domain Reigstrar CLI application. The application has 3 high level goals.

* Subscribe to a datasource which has the cost for TLDs and Premium domain names.
* Calculate the total cost of the domain names a user wish to purchase based on the input provided by the user
* Implement an easy to use UI for the user to use the application

## Pricing Information

In the current implementation the pricing information is maintained as a list of JSON with the following schema.

```
    [ 
        {
        "domain": ".com",
        "price": "10",
        "type": "NORMAL"
        }
   ]
```

* `domain` : The name of domain [ `type : string` ]
* `price`  : Price associated with the domain [ `type: float` ]
* `type`   : Type of domain (`NORMAL` vs `PREMIUM`) [ `type: enum` ]

## Cost of Ownership

This section describes the way the cost is calculated based on the users input.

The following are the steps to calculate the pricing.

1. Get the domain name and number of years to register from the user
2. Check whether the domain name belongs to premium domain
    * if `yes` multiply the unit price by number of years
3. If domain doesn't belong to premium domain
    * Fetch the TLD from the domain name
    * Fetch the unit price of TLD
    * Multiply unit price with number of years
4. Return the result (Adding up all the domain requests)

## User Interface

The application should be easy enough to use. The application will have the following options:

* `-f/--file`: The file (`CSV`) containing comma separated list of `<domain>,<years>`
* `-h/--help`: The instructions on how to use the application

Given below is the schema for domain list csv file.
```
# file: domainlist.csv
a-domain.com,1
another-domain.net,2
dict.com,5
```
## Sample Execution

* Application run without any options will print the help.
    ```
    $ java -jar build/libs/domain-registrar-all.jar
    usage: java -jar domain-registrar-all.jar
    -f,--file <arg>   Absolute Path to file containing Domain list
    -h,--help         Prints this help message
    ```
* Application run with a domain list file as argument
    ```
    $ java -jar build/libs/domain-registrar-all.jar -f ~/Workspace/domainlist.csv
        dict.com registration for 5 year at $800.0 per year = 4000.0
        another-domain.net registration for 2 year at $9.0 per year = 18.0
        ranjan.com registration for 4 year at $10.0 per year = 40.0
        a-domain.com registration for 1 year at $10.0 per year = 10.0

        Total Cost of the request = $4068.0
    ```