# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.4/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.4/gradle-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#web)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Project Objective
Create a SpringBoot web service fetching data from MySQL database and have both containerized. 
The SpringBoot project will use Gradle as the build tool.

[Source of Inspiration](https://www.youtube.com/watch?v=U2GCM0GBzNI)

### How to Get Started?

In your terminal, pull the latest mysql-server image:

`docker pull mysql/mysql-server:latest`

Create and run a container out of the pulled image:

`docker run -p 3307:3306 --name mysqldb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=test mysql/mysql-server:latest`

Here, '-p' flag is for mapping 'external':'internal' ports, the name of the container is 'mysqldb',
the environmental variables are marked with '-e' flag, the name of the database is 'test' and the image is
'mysql/mysql-server:latest'.

Now, we will configure DataGrip Connection with dockerized MySQL database ([see Configuring Docker MySQL and Datagrip Connection](https://medium.com/@m22kats/configuring-docker-mysql-and-datagrip-connection-da0b6b71082f)) below:

Execute this command in the terminal:

`docker exec -it $(docker ps -q -f name='mysqldb') bash -l;`

You will get a prompt something like:
`[root@fbef4367d678 /]#`

Type in a command:

`[root@fbef4367d678 /]# mysql -u root -p`

Enter password: `root`

You will get to your MySQL prompt:

`mysql>`

Create MySQL user and password like this:

`mysql> CREATE USER 'apo'@'%' IDENTIFIED BY 'pswd123';`

Here, the '%' is a wildcard meaning any host, e.g. localhost. 

You will get a response:

`Query OK, 0 rows affected (0.06 sec)`


Grant all privileges to your user:

`mysql> GRANT ALL PRIVILEGES ON *.* TO 'apo'@'%';`

Get the response:

`Query OK, 0 rows affected (0.02 sec)`

Flush the privileges:

`mysql> FLUSH PRIVILEGES;`

`Query OK, 0 rows affected (0.01 sec)`

Exit and Logout:

`mysql> exit`

`Bye`

`[root@fbef4367d678 /]# exit`

`logout`


Test your db connection with e.g. DataGrip with username 'apo', password 'pswd123', port '3307' and db name 'test'.

Now, once the connection is established, create a network:

Have a look at available networks and choose a new name for your network:

`docker network`

Create a new network name e.g. 'spring-network':

`docker network create spring-network`

Get your network ID: 
`c8491768c4a9b57...`

Have a look at our JVM Environmental Variables in SpringBoot Configuration:
`-DMYSQL_USER=apo -DMYSQL_PASSWORD=pswd123 -DMYSQL_PORT=3307`

Now, launch our SpringBoot Project inside IDE and hit your Restful endpoints to test the response, the port will be 8080 for internal Tomcat:

`localhost:8080/student/all`

The response with be an empty array '[]' for the first time.

You can add a new student with a POST request to `localhost:8080/student/` and a body:

`
{
"student": "John Doe",
"dateOfBirth": "2023-01-27"
}   
`

Add more students and repeat the GET request.

Now, it's time to containerize our SpringBoot web service.

Inside our project folder with Dockerfile, run a command:

`docker build -t springboot-mysql-docker .`

Get a Successfully built  message with the container ID

`Successfully built c00b87a4c0dd`

Type `docker images` to see that we got our 'springboot-mysql-docker' image running.

Check that we have our  'mysqldb' running `docker ps`

Check that we have our network 'spring-network' available `docker network ls`

Then, we connect our 'mysqldb' to our 'spring-network' like this: `docker network connect spring-network mysqldb`

We can inspect our network `docker network inspect spring-network` to check that our container name 'mysqldb' has been connected to this network.

Finally , we run :

`docker run -p 9090:8080 --name springboot-mysql-docker --net spring-network -e MYSQL_HOST=mysqldb -e MYSQL_USER=apo -e MYSQL_PASSWORD=pswd123 -e MYSQL_PORT=3306 springboot-mysql-docker`

And we get our 'springboot-mysql-docker' available at port 9090, registered on the network 'spring-network'.

And finally Hit your endpoints with Postman!!!

By sending a GET request to `localhost:9090/student/all` to localhost at port 9090, we get  a list of available students:

`[
{
"id": 1,
"student": "Pascal",
"dateOfBirth": "2015-03-21"
},
{
"id": 2,
"student": "Anand",
"dateOfBirth": "2023-01-27"
}
]`

By sending a POST request to `localhost:9090/student/`, with a request body, e.g. 
`
{
"student": "Juri",
"dateOfBirth": "2018-03-07"
}
`,
we add a new student to the db.

Done!