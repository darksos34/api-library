# Spring Boot - CRUD REST API
<small></small>


<b>Author:</b> <a href="https://github.com/darksos34" target="_blank">Jordy Hamwijk</a><br>
<b>Created:</b> 2024-04-05<br>
<b>Last updated:</b> 2024-05-17</br>

[![](https://img.shields.io/badge/Spring%20Boot-8A2BE2)]() [![](https://img.shields.io/badge/release-Apr%2004,%202024-blue)]() [![](https://img.shields.io/badge/version-3.2.4-blue)]()

## 1. What is a CRUD REST API?
A CRUD REST API allows clients to perform basic operations on resources via HTTP requests. These operations include creating new resources, reading existing ones, updating resource data, and deleting resources. It adheres to the principles of REST, ensuring a predictable and uniform interface for communication between clients and servers.</br>

CRUD stands for Create, Read, Update, and Delete. </br>
It represents the four fundamental operations used to interact with database applications.<br/>
These operations allow developers to manipulate data within a collection or database.
Here’s what each function does:
- Create: Adds new entries to the database.
- Read: Retrieves entries based on specific criteria (such as filtering or searching).
- Update: Modifies specific fields in existing entries.
- Delete: Removes one or more existing entries entirely.
- CRUD (Create, Read, Update, Delete):</br>

#### For example, in SQL (a popular language for interacting with databases), the equivalent functions are Insert, Select, Update, and Delete.

## 2. How a CRUD REST API works in Spring Boot?

Spring Boot simplifies building RESTful APIs by providing a framework for handling HTTP requests and integrating with databases.</br>


### 2.1 Create a Spring Boot Application

Go to [Spring](https://start.spring.io) and create a new Spring Boot project.

![01-start-spring-io](https://github.com/darksos34/api-library/blob/master/src/main/resources/images/sping.initializr.png)

Add additional dependencies:
- Springdoc - OpenApi Swagger UI: A library that provides a user interface for viewing API documentation.
- OpenApi webflux: A library that simplifies API documentation for reactive applications.
- OpenApi webmvc - UI: A library that provides a user interface for viewing API documentation.
- OpenApi webmvc - API Docs: A library that provides API documentation for Spring WebMVC applications.
````    xml
        <!-- https://springdoc.org/ -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-ui</artifactId>
            <version>${springdoc.openapi-ui}</version>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webflux-api</artifactId>
            <version>${springdoc-openapi-starter-webflux-api}</version>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc-openapi-starter-webmvc-ui}</version>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-api</artifactId>
            <version>${springdoc.openapi.starter.webmvc.api}</version>
        </dependency>
````

### ModelMapper: A library that simplifies object mapping. 

```` xml
        <!--    https://modelmapper.org/getting-started/    -->
        <dependency>
            <groupId>org.modelmapper</groupId>
            <artifactId>modelmapper</artifactId>
            <version>${modelmapper.version}</version>
        </dependency>
````

### Create GET endpoint 


```java
//User is the parent class of Profile

@Tag(name = "USER", description = "User applicatie Endpoints")
@RequestMapping(RequestPath.V1 + RequestPath.USER)
public interface UserApi {

    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "User weergeven op basis van code.")
    UserDTO getUserByCode(@PathVariable(value = "code")
                          @Parameter(example = "ABCD", description = "test") String code);


}

```


### Create A Pageable GET endpoint to retrieve a list of User's
* @Parameter = swagger list on the web browser

```java
@Tag(name = "USER", description = "User applicatie Endpoints")
@RequestMapping(RequestPath.V1 + RequestPath.USER)
public interface UserApi {

    @GetMapping()
    @Operation(summary = "Lijst weergeven met alle users als paging.")
    @Parameter(name = "page", schema = @Schema(type = "integer", defaultValue = "0"), in = ParameterIn.QUERY)
    @Parameter(name = "size", schema = @Schema(type = "integer", defaultValue = "20"), in = ParameterIn.QUERY)
    PagedModel<?> getAllUsersPageable(@ParameterObject @Parameter(hidden = true) Pageable pageable);
}

```

### Create A POST endpoint to create a new user
```java
@Tag(name = "USER", description = "User applicatie Endpoints")
@RequestMapping(RequestPath.V1 + RequestPath.USER)
public interface UserApi {


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@PreAuthorize("hasAuthority('admin:READ)")
    @Operation(summary = "Nieuwe user aanmaken.")
    @ResponseBody
    UserDTO createUser(@Valid @RequestBody UserDTO userDTO);


}
```

## Let's Stay Connected

Feedback is always welcome. If you have any questions, comments, or suggestions, please do not hesitate to reach out.

- <b>Star</b> the repository to show your support.

- Follow me for more interesting repositories!
