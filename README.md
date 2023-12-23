# Project Title
JobHub

## Description

This GitHub repository hosts a JavaFX application for a job search platform, designed to streamline the job search process. The application utilizes MySQL as its database backend, ensuring efficient data storage and retrieval.


## Table of Contents

1. [Installation](#installation)
2. [Configuration](#configuration)
3. [Usage](#usage)
4. [Contact Information](#contact-information)

## Installation
### 1. Clone the Repository
```
git clone https://github.com/Mahdi-jamil/JobHub.git D:/
```
### 2. Prerequisites
Ensure that you have Maven installed on your system.
### 3. Build Dependencies
All project dependencies are specified in the `pom.xml` file. Maven will automatically download and manage these dependencies during the build process.
```
cd D:/JobHub
mvn dependency:resolve
```
## configuration
1. Check classes in java/DataBase and change username and password for user root to Your specific user
2. Check resources/com/DataBase and run batch files after editing the connection to establish database and all tables and related stuff

## usage

1. Check ScreenShots for a UI & UX
2. Add run configuration --module-path "C:\YourPath\openjfx-21.0.1_windows-x64_bin-sdk\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml
3. Note : double-click on a tableView usually do some important action (Apply,view details)

## contact-information

1. Email :[Email me](mailto:jamilmahdi77@gmail.com)
2. Linked in : [My LinkedIn Profile](https://www.linkedin.com/in/mahdi-jamil-902351261/)