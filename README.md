# Cloud Lab

Cloud Native Lab - Simple Workshop demonstrating Cloud-Native development with Spring Boot and Pivotal Cloud Foundry.

>> WORK IN PROGRESS -- update of cloud-lab repo -- but with templating examples. 

# Items covered:

## Spring Boot

* Initializr
* Web and Test

## PCF

* Deployment
* Scaling / Auto-Scaling


## PCF

## Prerequisites

### Modern Java JDK Installed (at-least Java8)

### Cloud Foundry Command Line Interface (cf CLI) installed

[https://docs.cloudfoundry.org/cf-cli/install-go-cli.html](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html)

### Java IDE of choice ..

Intellij recommended .. The Community addition is free.

Using an non-Java IDE, can cause issues with test formatting as well as lack of "auto imports".

### Access to a Pivotal Cloud Foundry instance

Access will be provided during the workshop, or you can sign-up for a free access at : https://run.pivotal.io

## 0 - Initialization

Key points:
* Spring Boot Initializr
* Maven
* Maven Wrapper

### 0.1 - Generate a Spring Boot Template from https://start.spring.io
Stick to the default settings, however update:
- for dependencies add **Web**

<img src="img/init-screen.png">

Download it, and unzip it.

> NOTE - Make sure that the **Web** Dependency was added -- you should see *spring-boot-starter-web* in your pom.xml

Note the mvnw file:
This is the Wrapper components for Maven: ensuring build script version consistency, removing the dependency of having these build tools installed , and simplifying CI build agent dependencies (only a JDK will be required).

### 0.2 - Import the project into your IDE

Note make sure to do an *import* and not just *open* , to allow for your IDE to correctly pull down dependencies.

## 1 - WebApplication with SpringBoot

Key points:
* Spring Boot Web
* Creating a Simple Helloworld endpoint
* Running the App locally
* No XML!

### 1.1 - Implement a HelloWorld endpoint
This can be done by creating a *HelloWorldController* Java class file with:


```java
package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String index(){
        return "Hello world";
    }

}
```

### 1.2 - Run the application

> NOTE: Maven instructions are for Bash/Linux/MacOS ... for Windows , replace calls to *./mvnw* with *mvnw.cmd*

```sh
./mvnw spring-boot:run
```

On Windows machines:

```sh
mvnw.cmd spring-boot:run
```

> NOTE: if you have trouble running Maven Wrapper (mvwn) it is most likely due to Proxy issues,  try regular Maven (not wrapper)

---

To build the artifact:

Run:

```sh
./mvnw package
```

On Windows machines:

```sh
mvnw.cmd package
```

To run your built application: 

```sh
java -jar ./target/demo-0.0.1-SNAPSHOT.jar
```

Note Tomcat is embedded inside of the build artifact (you don't need an external application Server).

### 1.3 - Test the root endpoint
The address will be: localhost:8080

You can test via a browser or commandline:
```sh
curl localhost:8080
```

### 1.4 - BONUS - Add Unit Tests for HelloWorld endpoint
This can be done by creating a *HelloWorldControllerTests* Java class file in the test/java directory with:

```java

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testHelloWorld(){
        String body = restTemplate.getForObject("/",String.class);

        assertThat(body).contains("Hello world");
    }
}
```

With Intellij, you can now run the Test by right clicking on it.

From the commandline you can run them with:

```sh
./mvwn test
```

On Windows machines:

```sh
mvnw.cmd test
```


## 2 - Server side rendering with Thymeleaf and Bootstrap CSS

Key points:
* Server side vs Client side rendering 
* Html templating 
* Webjars 

### 2.1 - Change your helloworld controller to use the /hello path instead of / (in HelloWorldController.java)

```java
@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String index(){
        return "Hello world";
    }
}
```

Also fix your now broken tests (in HelloWorldControllerTests.java):

```java

@Test
public void testHelloWorld(){
    String body = restTemplate.getForObject("/hello",String.class);

    assertThat(body).contains("Hello world");
}
    
```

Verify your test is not breaking:

From the commandline you can run them with:

```sh
./mvwn test
```

On Windows machines:

```sh
mvnw.cmd test
```

### 2.2 - Add the Spring Boot Thymeleaf and Bootstrap dependencies to your build script (pom.xml).

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- thymeleaf -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    
    <!-- boot strap css -->
    <dependency>
        <groupId>org.webjars</groupId>
        <artifactId>bootstrap</artifactId>
        <version>3.3.7</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
</dependencies>
```

### 3.3 - Add a IndexController 


```java
package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Hello world !!!");

        return "index";
    }

```

### 2.4 - Add an index.html template in the resources/templates directory.

```html
<!DOCTYPE HTML>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Spring Boot Thymeleaf Hello World Example</title>
    <link rel="stylesheet" th:href="@{webjars/bootstrap/3.3.7/css/bootstrap.min.css}"/>
</head>

<body>

    <div class="container" style="padding: 16px 16px;">
        <div class="row">
            <div class=".col-md-3 .offset-md-3">
                <div class="panel panel-default">
                    <div class="panel-heading"><h2>Message</h2></div>
                    <div class="panel-body">
                        <p th:text="${message}"></p>
                    </div>
                </div>
            </div>
        </div>

    </div>
</body>
</html>


```


### 2.5 - Run the application

> NOTE: Maven instructions are for Bash/Linux/MacOS ... for Windows , replace calls to *./mvnw* with *mvnw.cmd*

```sh
./mvnw spring-boot:run
```

On Windows machines:

```sh
mvnw.cmd spring-boot:run
```

Verify in your browser that localhost:8080 works. 


## 3 - WebApplication on PCF

Key points:
* Deploying to PCF
* Scaling in PCF
* VCAP variables 

### 3.1 - Login into the PCF instance that you are using (if required)

```sh
cf login -a ENTER_API_URL_HERE
```

*Note: ENTER_API_URL_HERE will be provided to you*

Enter your Username and Password.

### 3.2 - Deploy your application to PCF

```sh
cf push cloud-lab -p target/demo-0.0.1-SNAPSHOT.jar
```

*cloud-lab* will represent a unique name of your running application on PCF.  This can be something more unique like : cloud-lab-your-initials. You may run into chalenges if someone else has already used this unique name in which case use something else.  The error that you would see if this is the case: 

> _The app cannot be mapped to route demo.cfapps.io because the route exists in a different space._   

.. if that is the case going forward ALWAYS use a different application name and try something like: 

```sh
cf push cloud-lab-john-smith -p target/demo-0.0.1-SNAPSHOT.jar
```


This will automatically create a new application in your default PCF development space, with the specific jar artifact deployed.

Note that PCF will automatically detect that this is a Java application, and use the appropriate *BuildPack*.

### 3.3 - Login into the PCF portal to view your newly deployed / created application

If you are using Pivotal Web Services, the portal is at:

https://run.pivotal.io

Click through to your app by selecting the default space and org.

Your route to the application (URL) will be presented besides your application.

### 3.4 - Test the / endpoint at this route <ROUTE>/

### 3.5 - Scale the App

Either provision more instances or more space.

This can be done via the command line or via the GUI.

To scale up to 2 instances:

```sh
cf scale cloud-lab -i 2
```

Via the GUI observe additional instances being spun up.

### 3.6 - Show PCF information in your index page

Update your IndexController:

```java
@Controller
public class IndexController {

    @Value("${vcap.application.name:localMachine}")
    private String applicationName;

    @Value("${vcap.application.space_name:localSpace}")
    private String spaceName;

    @Value("${vcap.application.instance_id:localInstanceId}")
    private String instanceId;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Hello world !!!");
        model.addAttribute("applicationName", applicationName);
        model.addAttribute("spaceName", spaceName);
        model.addAttribute("instanceId", instanceId);

        return "index";
    }

}



```

Update your index.html template:

```html

<!DOCTYPE HTML>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Spring Boot Thymeleaf Hello World Example</title>
    <link rel="stylesheet" th:href="@{webjars/bootstrap/4.3.1/css/bootstrap.min.css}"/>
</head>

<body>

    <div class="container" style="padding: 16px 16px;">
        <div class="row">
            <div class=".col-md-3 .offset-md-3">

                <div class="panel panel-default">
                    <div class="panel-heading"><h2>Message</h2></div>
                    <div class="panel-body">
                        <p th:text="${message}"></p>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading"><h2>PCF Info</h2></div>
                    <div class="panel-body">
                        <p th:text="'Application name: ' + ${applicationName}"></p>
                        <p th:text="'Space name: ' + ${spaceName}"></p>
                        <p th:text="'Unique instance id: ' + ${instanceId}"></p>
                    </div>
                </div>

            </div>
        </div>

</body>
</html>
```


Build and redeploy to PCF. Hit the homepage URL and note the PCF information.

This includes Round Robin load balancing when you have multiple instances running.
