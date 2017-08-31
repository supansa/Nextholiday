# Holiday information service

The service will return next holiday after the given date that will happen on the same day in both countries.

## Getting Started
Holiday information service accept two country codes and a date in a format yyyy-mm-dd
and return next holiday after the given date that will happen on the same day in both
countries. the service Use [https://holidayapi.com/](https://holidayapi.com/) API to retrieve holiday information. 

The service has only one public REST endpoint which is 
```
GET: nextholiday/api/getNextHoliday?date={yyyy-mm-dd}&countryCode1={countryCode}&countryCode2={countryCode}
```

**Example url :** http://localhost:8088/nextholiday/api/getNextHoliday?date=2016-10-01&countryCode1=NO&countryCode2=US

## Parameters

	- date: Request date in a format yyyy-mm-dd
	- countryCode1: Country codes that are available in Holiday API. 
	- countryCode2: Country codes that are available in Holiday API.

## Response

The API will return a JSON with date and names of the holidays in both countries.
Example response from NO and US:
```
{
    "statusCode": 200,
    "nextHoliday": {
        "date": "2016-10-09",
        "NO": "Federation Day",
        "US": "Federation Day"
    }
}
```
Example error response :
```
{
    "statusCode": 400,
    "statusMessage": "date format is not valid. (e.g. yyyy-mm-dd)",
    "namespace": "nextholiday"
}
```

## Installation

Holiday information service is written in java and use maven to compile. Please make
sure you have the java development environment set up

To compile Holiday information service run ```mvn install``` in the root directory.

```bash
cd {path_to_nextHolidays}
mvn install
```
## Run the application
When everything is sucessfully compiled and installed, then do the following:

```bash
cd {path_to_nextHolidays}
mvn spring-boot:run
```
When the spring-boot run is complete, goto this url [http://localhost:8088/nextholiday/](http://localhost:8088/nextholiday/)

