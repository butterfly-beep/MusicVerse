# MusicVerse
A small web app for managing music events, built for my DBMS course project. You can keep track of customers, artists and events through a simple dashboard. It's written in plain Java servlets with JDBC talking to MySQL, no frameworks.

## What it does
- Add, view and delete customers
- Add, view and delete artists
- Event data is stored in the database too, but the event pages are still pretty basic
- A homepage and a management page that link everything together

## Stack
- Java 17, Servlets (Tomcat 10.1)
- MySQL 8 with JDBC (MySQL Connector/J)
- HTML + CSS for the frontend
- Maven for building
- Written in VS Code

## How it fits together
The browser sends requests to Tomcat, the servlets handle them and run queries over JDBC against a MySQL database called `MusicEventManagement`.

## Database
Five tables:
- `customer`
- `artist`
- `event`
- `performs` (which artist plays at which event)
- `ticket`

## Running it locally
You'll need JDK 17, Maven, MySQL 8 and Tomcat 10.1 installed.

1. Clone the repo
   ```bash
   git clone https://github.com/YOUR-USERNAME/MusicVerse.git
   cd MusicVerse
   ```

2. Create the database in MySQL and set up the tables from the project's SQL schema
   ```sql
   CREATE DATABASE MusicEventManagement;
   ```

3. Set your MySQL password as an environment variable. The code reads it from `DB_PASSWORD`, so there's no password in the source.
   PowerShell:
   ```powershell
   $env:DB_PASSWORD = "your_mysql_password"
   ```

   Bash:
   ```bash
   export DB_PASSWORD="your_mysql_password"
   ```

   Set it in the same terminal you start Tomcat from, otherwise Tomcat won't see it.

4. Build
   ```bash
   mvn clean package
   ```

   The WAR file ends up in `target/`.

5. Copy the WAR into Tomcat's `webapps` folder and start Tomcat.

6. Open it in the browser:
   - Home: http://localhost:8080/music-event-management/index.html
   - Dashboard: http://localhost:8080/music-event-management/management.html

## Known limitations
- No login. Anyone who can reach the dashboard can add or delete records.
- Input validation is minimal and error handling is basic.
- The event side is not as built out as customers and artists.
- No automated tests and no SQL init script yet.

## Things I'd like to add
- Login with admin/user roles
- Proper validation and error messages
- Full event and ticket management
- A schema script so setup is one step
- A nicer, more responsive UI

## About
Made as a college project to get hands-on with Java web development and relational databases.
