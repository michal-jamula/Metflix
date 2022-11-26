# **Metflix**


<br>

## Program Overview:

Online Streaming service. I don't have any formal or informal requirements. The idea is to start small and keep on expanding the functionality as I expand my knowledge and learn new concepts.


This website is a demonstration of my skills. I'm building the website with Thymeleaf, Bootstrap, HTML and CSS.
For the backend I'm using Java, Spring and JPA.



The motivation for this project comes from my willingness to show off my skills and use them outside of the courses that I've completed. The point is to cement my knowledge and become more confident in using this technology.
<br>

## How to run:
 + Import into any IDE (I use Intellij), build the maven project and run. 
 + Project uses an in-memory H2 database on startup
 + Project injects data from SQL files
 + Project is built on Java 11
 + Project requires "video1.mp4" and "video2.mp4" files in the videos directory for user functionality - it works without these files as well.

<br>

### Some of the features this program has at this point:
1. Display all the HTML pages by utilising thymeleaf and layout fragments
2. Connect to a database
   1. Use JPA (hibernate) to automatically create database tables at startup
   2. Perform necessary CRUD operations to display requested data
3. User functionality - 
   1. Able to register a user
4. Admin functionality - 
   1. Read multiple user data from database
   2. Able to access single user information from admin panel
   3. Able to edit and save single user information from admin panel 
   4. Able to access, edit and save information about a single movie
   5. Read credit cards and addresses from database
5. Video functionality - 
   1. Website is able to read mp4 files and play them back to the user.
   
<br>

## Future development plans:
1. Finish working on the basic HTML and CSS files. Aim to create a finished website look. ✔
2. Clean up the HTML, CSS code, comment appropriately and get rid of useless code, prepare for spring-boot implementation. ✔
3. Start a spring boot project and setup initial project (jpa entities with a database, thymeleaf, spring-boot server, controller etc). ✔
   1. Create basic working website with dummy data ✔
   2. Setup basic user functionality ✔
   3. Give user a response if registration process went wrong ✔
4. REST functionality 
   1. Create a REST Controller ✔
5. Admin Functionality:
   1. Create all the other databases ✔
   2. Create all other admin tables ✔
   3. Create an SQL script to inject data into all these tables ✔
   4. Make all admin tables display data dynamically and implement sort functionality ✔
   5. Make admins able to manipulate data in all the tables ✔
6. Movie functionality
   1. Create more database tables to handle movies ✔
   2. Create SQL scripts to inject data ✔
   3. Fix the user_main website to display data dynamically ✔
   4. Make the movie_single page work by allowing all users to watch movies ✔
7. Implement spring security to authorise and authenticate users
8. Testing:
   1. Unit tests
   2. Integration tests
   3. Mockito
   4. End-to-end user tests
9. (maybe) Integrate paypal developer payment systems 
10. (maybe) More to implement in the future 


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
- 07/11/2022
  - Created 'Address', 'Credit card' and 'Movie' Tables. Also:
    - Respective repositories
    - Respective services
    - Respective data through SQL inject files
  - SQL Inject files are now in resources/data repositories, Spring automatically scans and runs any .sql files in that directory
  - admin_user page is now (almost) fully functional. Admins are able to edit information regarding any registered user.
- 26/11/2022
  - Created video streaming service and controller
  - Created the main user landing page
  - Created the ability for users to watch videos
  - Completed the functionality where admins are able to edit user's details and movie details


<br><br>
Created by Michal Jamula