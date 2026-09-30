package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
//main()
//  │
//  ▼
//SpringApplication.run()
//  │
//  ├── Creates Spring environment
//  │
//  ├── Creates ApplicationContext
//  │
//  ├── Performs auto-configuration
//  │
//  ├── Scans for Spring components
//  │
//  ├── Creates/registers Beans
//  │
//  ├── Starts embedded web server
//  │
//  └── Application becomes ready

//@SpringBootApplication
//        │
//        ├── @SpringBootConfiguration
//        │
//        ├── @EnableAutoConfiguration
//        │
//        └── @ComponentScan

//This is important.
//You didn't install Tomcat separately.
//Yet your application becomes an HTTP server.
//Why?
//Because your web starter brings the necessary web infrastructure.
//Conceptually:
//
//Your Java Application
//        │
//        ▼
//Spring Boot
//        │
//        ▼
//Spring MVC
//        │
//        ▼
//Embedded Tomcat
//        │
//        ▼
//localhost:8080

// FULL FLOW
//                  SPRING BOOT
//                       │
//                       ▼
//                  Controller
//                       │
//                       │ DTO
//                       ▼
//                    Service
//                       │
//                       │ Entity
//                       ▼
//                  Repository
//                       │
//                       ▼
//                 Spring Data JPA
//                       │
//                       ▼
//                    JPA API
//                       │
//                       ▼
//                   Hibernate
//                       │
//                       ▼
//                      JDBC
//                       │
//                       ▼
//                  PostgreSQL

//REVERSE

//PostgreSQL
//    ↓
//JDBC
//    ↓
//Hibernate
//    ↓
//JPA
//    ↓
//Repository
//    ↓
//Entity
//    ↓
//Service
//    ↓
//DTO
//    ↓
//Jackson
//    ↓
//JSON