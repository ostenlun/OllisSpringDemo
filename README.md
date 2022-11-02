# OllisSpringDemo
Simple Spring demo application for retrieving bus stop arrivals and storing them into a remote Mongodb database.

> Live demo TBA [_here_].

## General Information
This is a simple backend that retrieves data from Tfl API and stores it in a remote MongoDB.

## Technologies Used
- Java
- Spring Boot
- JUnit
- MongoDB

## Features
The main features are:
- Get arrivals retrieves the bus arrivals for a certain bus stop in London by calling a Tfl API (https://api.tfl.gov.uk/StopPoint/490009333W/arrivals) and stores the data to a remote database.
- Get history retrieves the arrivals stored in the database
- Delete history deletes all arrivals stored in the database

## Usage
Please use OllisSpringDemoUI as a frontend UI to use this application. You can also call the web API using the following browser commands when the backend app is running as a server:

*/history*<br>
*/arrivals*

Ask for the remote database credentials (src/main/resources/application.properties) or use your own Mongodb cluster.

## Project Status
Project is: _complete_

## Contact
Created by [@ostenlun](https://www.codeheaven.one/) - feel free to contact me!

## License
This project is open source and available under the [MIT License]().
