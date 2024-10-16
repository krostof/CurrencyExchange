# Currency Converter Application

## Overview

This project is a web-based Java application, written using the Spring Boot framework, that provides a RESTful service for currency conversion. The service takes three parameters: the amount, the currency of the amount, and the target currency. It returns the converted amount using up-to-date exchange rates fetched from the National Bank of Poland (NBP) API. The application is written using the Spring Boot framework and is hosted in the cloud using AWS.

## Features

- REST API for converting currency amounts.
- Currency exchange rates are fetched from the NBP API and cached to improve performance and reduce the number of requests.

###  Clone the Repository

Download the project files by cloning the repository:

```bash
git clone https://github.com/krostof/CurrencyExchange
```

## Running the Application

The application can run locally on port 8080, access it via `http://localhost:8080`

The REST API endpoint:

```
GET /currency-exchange
```

Parameters:

- `amount`: The amount to be converted (must be greater than zero).
- sourceCurrency: The currency of the amount.
- `targetCurrency`: The target currency to convert the amount to.

Example request:

```
GET /currency-exchange?amount=100&currency=USD&targetCurrency=EUR
```

Example response:

```json
{
  "response": "92.34"
}
```

The Swagger UI interface can be accessed at:

```
http://localhost:8080/swagger-ui/index.html#/currency-exchange-controller/getCurrencyExchange
```

The application is also available at:

```
http://ec2-35-157-36-140.eu-central-1.compute.amazonaws.com:8000/swagger-ui/index.html#/currency-exchange-controller/currencyExchange
```

##
