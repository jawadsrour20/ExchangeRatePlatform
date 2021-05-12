# project_430L_backend

## How to setup and run the platform.

1) Setting up and running Flask on your machine:  
    a) Create a python virtual environment called venv, using the command "py -3 -m venv venv" on windows or using "python3 -m venv venv" on Unix.   
    b) Activate the virtual environment: "venv\Scripts\activate" on windows, "venv/bin/activate" on Unix.    
    c) Download Flask in venv using pip: "pip install Flask"    
    d) Open a pyhton script called "app.py", import Flask (from flask import Flask) and create a flask app: "app = Flask(__name__)" - NOTE: You don't need to do that in this case, just pull the repo, it is already done, just open app.py and check it!       
    e) Type "set FLASK_APP=app.py" in the terminal to set the python script that Flask will run.  
    f) Type "set FLASK_ENV=development" in the terminal to set environment to a development environment, if you skip this step, the flask app will run in production mode.   
    g) Now type "flask run" in the terminal and the app will be running! Proceed to part 2 to run the app inside the github repo.   


2) Running the current Flask project in the Github repo:  
    a) Activate the virtual environment   
    b) Make sure you installed all the requirements in the "requirements.txt" file using pip. For example, you will find "Flask-Bcrypt" in the file, in the command prompt while venv is active 
    write: "pip install Flask-Bcrypt"        
    c) Type "set FLASK_APP=app.py" in the terminal to set the python script that Flask will run.   
    d) Type "set FLASK_ENV=development" in the terminal to set environment to a development environment, if you skip this step, the flask app will run in production mode.   
    e) GOTO PART 3 TO SET UP THE MYSQL DATABASE AND CONTINUE FROM F AFTER DOING SO.   
    f) In the app.py script, fill in the correct place the following:   
    ```pyhton   
    app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://<mysql_username>:<mysql_password>@<mysql_host>:<mysql_port>/<mysql_db_name>'   
    db = SQLAlchemy(app)   
    ```     
    g) open a Python shell ad write the following:   
    ```python                               
    from app import db 
    db.create_all()    
    exit()      
    ```      
    That will create the tables on the MySQL server on your machine.   
    h) Now you are set, type "flask run" in the terminal and the backend will be running!   


3) Dowload and setup MySQL server:  
    a) Download and make sure that the "MySQL Community Server" is running.   
    b) Download "MySQL workbench" to visualize and help in creating the database.   
    c) Open MySQL Workbench and connect to your MySQL server, which will usually be running on port 3306, using the credentials you set when installing the MySQL server.   
    d) Create a new database named "exchange" (Use MySQL Workbench for this)   


## A brief description of the project structure and general architecture of the software platform

a) Tables and database

The database is hosted on the MySQL server, it is called "exchange". It is composed of the following Tables:

 - User     
    id:	                integer (int64)     
    user_name:	        string     
    hashed_password:	string     

 - Rates     
    id:	                integer (int64)     
    usd_to_lbp_amount:	number (float)    
    lbp_to_usd_amount:	number (float)     
    added_date:	        string (datetime)     

 - Transaction     
    id:	                integer (int64)     
    usd_amount:	        number (float)     
    lbp_amount:	        number (float)    
    usd_to_lbp:	        boolean (bool)    
    added_date:	        string (datetime)    
    user_id:	        integer (int64)    

 - UserExchanges      
    id:	                        integer (int64)    
    user_initiated_id:	        integer (int64)     
    user_received_username:	    string    
    usd_amount:	                number (float)     
    lbp_amount:	                number (float)     
    date_of_transaction:	    string (datetime)     
    usd_to_lbp:	                boolean    


b) API routes
All routes are in the 'app.py' file, check the "OPEN API Documentation" folder. 
For more details, check the 'app.py' file for more comments on the code of each API route.


c) Additional info
 - flask_marshmallow used for data serialization schemas
 - flask_bcrypt used for Bcrypt encryption algorithm used to create hashed passwords for users
 - flask_cors used to remove CORS restrictions
 - flask_sqlalchemy used to setup the SQL database



## Documentation of the backend API layer and table models using the openAPI specification is available in the Repo!
Go to the "OPEN API Documentation" folder and open the index.html file.
