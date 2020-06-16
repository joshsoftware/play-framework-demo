# play-framework-demo
Demo application using Play Framework

Note: This project uses sbt as build tool, make sure it is installed

## Database setup
  This project is configured to use Postgre database. You will find the configuration details in /conf/application.conf  
  Update below configuration details as per your database configuration.  
      `
      default.url = "jdbc:postgresql://localhost:5432/<dbname>"  
      default.username = <username>  
      default.password = "<password>"  
      `
  
  Note:  
  1. Make sure password and database url are enclosed in double quotes  
  2. If you want to use database different than postgres, make sure to update the 'hibernate.dialect' property value present in /conf/META_INF/persistence.xml with corresponding dialect  
  
## How to run the application
1. Go to project directory  
2. Execute following command to update to resolve dependencies: `sbt update`
3. Execute following command to build the project: `sbt compile`  
4. Execute following command to run the application: `sbt run`  
    With above command, the application will run on default port 9000  

## API details
### Authentication
  Basic authentication with credentials Admin:Pwd123  
  
1. Get all students  
    GET /students  
    
2. Get specific student details using id  
    GET /students/{id}  

3. Add new student  
    POST /students  
    Request Body  
      ```
      {
        "firstName": "firstname",
        "lastName" : "lastname"
      }
      ```
    



