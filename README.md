#AMQ-TEST

Reference project for demoing technologies:
 - RedHat AMQ on Openshift
 - Active MQ local
 - Spring Cloud Stream
 - Hibernate via Postgres

## Running

To run locally execute the following command:

`mvn spring-boot:run -Dspring-boot.run.profiles=[PROFILE]`

Profile can be one of the following:

| Profile | JMS Type | Stream Type | Hibernate |
| --- | --- | --- | --- |
| seamless | activemq | test binder | Postgres |
| local | activemq | Rabbit | Postgres |
| server | redhatmq | test binder | Postgres |

## Links

### Local
- [Health](http://localhost:8080/amq/actuator/health)
- [Env](http://localhost:8080/amq/actuator/env)
- [Swagger](http://localhost:8080/amq/swagger-ui.html)
