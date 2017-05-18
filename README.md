# Sample spring boot app to demonstrate jenkins CI with docker

This application is a demonstration of a simple web application using spring boot, spring data using a postgres
database.

The important trick is that it also provides an automated acceptance test towards a fully deployed
docker-compose stack.

## How to setup in IntelliJ

1. Open the `build.gradle` and import it as a project
2. Make sure that you have the lombok plugin installed in IntelliJ
3. Make sure that "Annotation processing" is enabled under Java Compiler in the IntelliJ settings (more details can be found [here](https://github.com/mplushnikov/lombok-intellij-plugin)).

## Run the App 

1. Start the database
	
		cd docker
		docker-compose up -d postgres 
		
2. depending on your docker setup you might need to change `application.properties` to set up the correct 
   database URL in the key: `spring.datasource.url`
   
### IntelliJ

1. Open the Class `WebshopApplication` and run it as a spring boot application (or java application in case you dont have spring support).

### Commandline

	./gradlew bootRun
	
### Docker 

1. build the container

		mkdir -p docker/app/context
		./gradlew assemble
		cp web/build/libs/web-*.jar docker/app/context/web.jar
		cd docker/app
		docker build -t demo/web
	
2. start the stack

		cd docker 
		docker-compose up -d
		

More details can be found at [Lanyrd](http://lanyrd.com/2017/spring-io/sfqtth/).