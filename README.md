# What's left inventory tracking

Inventory tracking system with multi-user access to same data. Users can track, add, remove products and categories.
Three-level hierarchy system: admin, team leader and user. Admin can create admins and team leaders, team leader creates users for his/her team. 
Data is shared among team members and unavailable to admins or other teams. 

### Installing

Set up an SQL database with new schema.
Create a file with SQL server authentication data

```
/resources/application.properties
```
Example:
```
# ===============================
# = DATA SOURCE
# ===============================
# Set here configurations for the database connection
spring.datasource.url=jdbc:mysql://SQL_IP_ADDRESS:3306/DATABASE_NAME
# Username and secret
#spring.datasource.username=SQL_LOGIN
#spring.datasource.password=SQL_PASSWORD
```

Building and running shouldn't be a problem

## Deployment

Configure an Apache/Nginx virtual host and header redirects.


## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - Web framework
* [Thymeleaf](https://www.thymeleaf.org) - Template engine
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Nikita Shevchuk** - (https://github.com/shevchuk-na)
