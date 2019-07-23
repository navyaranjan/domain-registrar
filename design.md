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

The application should be easy enough to use. One important feature would be to have the ability for the application to be used interactively and in batch mode.

The application will have the following options:

* `-c/--count`: The number of domains the user wish to calculate the price of (used in interactive mode)
* `-f/--file`: The file containing comma separated list of `<domain>,<years>`
* `-h/--help`: The instructions on how to use the application