# **Metflix**


<br>

## Program Overview:

This is my attempt in making an "online streaming service". I don't have any formal or informal requirements. The idea is to start small and keep on expanding the functionality as I expand my knowledge and learn new concepts.


This website is a demonstration of my skills. I'm building the website with Bootstrap, HTML and CSS.
For the backend I'm using Java, Spring, thymeleaf, hibernate, jpa and other related frameworks to make the website function.



The motivation for this project comes from the willingness to show off my skills and use them outside of the courses that I've completed. The point is to cement my knowledge and become more confident in using this technology.
<br>

## How to run:
 + Import into any IDE (I use Intellij), build the maven project and run. 
 + Project uses an in-memory H2 database on startup
 + Project injects data from SQL files
 + Project is built on Java 11

<br>

### Some of the features this program has at this point:
1. Display all the HTML pages by utilising thymeleaf and layout fragments
2. Connect to a database
   1. Automatically create tables and inject data during startup
   2. Perform necessary CRUD operations to display requested data
3. User functionality - 
   1. Able to register a user
4. Admin functionality - 
   1. Read multiple user data from database
   2. Able to access single user information from admin panel
   
<br>

## Future plans:
1. ~~Finish working on the basic HTML and CSS files. Aim to create a finished website look~~ --  Finished
2. ~~Clean up the HTML, CSS code, comment appropriately and get rid of useless code~~ -- Finished
3. ~~Start a spring boot project and setup what I can (jpa entities with a database, thymeleaf, spring-boot server, controller etc)~~ -- Finished
   1. ~~Create basic working website with dummy data~~
   2. ~~Setup basic user functionality~~
   3. ~~Give user a response if registration process went wrong~~
4. ~~REST functionality~~ -- REST controller was not needed to create interactive admin tables.
   1. ~~Create a REST Controller~~
   2. ~~Make admin tables interactive and able to sort data~~
5. Admin Functionality:
   1. Create all the other databases
   2. Create all other admin tables
   3. Create an SQL script to inject data into all these tables
   4. Make all admin tables display data dynamically and implement sort functionality
6. Movie functionality
   1. Create more database tables to handle movies
   2. Create SQL scripts to inject data
   3. Fix the user_main website to display data dynamically
   4. Make the movie_single page work by allowing all users to watch movies
7. Implement spring security to authorise and authenticate users
8. (maybe) Integrate paypal developer payment systems
9. (maybe) More to implement in the future 


<br><br>


## Update History:
- 10/10/2022 
    - HTML CSS mostly finished, looking like the end product is likely to look. 
- 16/10/2022 
  - Created basic Spring-boot + thymeleaf application which is able to serve all the web pages 
- 21/10/2022
    - Implemented a PostgreSQL database (settings for that are within application.properties file)
    - Connected to the database, creating tables and injecting data every time the app starts
    - Created user registration (and basic form-verification) functionality
    - Made admin_users and admin_user_single pages display data dynamically based on database data
- 23/10/2022
  - Fixed errors given to the user while registering - now the user is able to see which field contains errors.
- 24/10/2022
  - Made the "User" table interactable by implementing pagination and sorting, admin is also able to access information about single user (that page is still to be completed)
  - Implemented H2 database instead of PostgreSQL to make it easier for anyone trying this program on their own computer
    - The H2 database runs in-memory, injects data from .SQL files and drops data when program is shut down
  - Implemented "live reload" from devtools


<br><br>
Created by Michal Jamula