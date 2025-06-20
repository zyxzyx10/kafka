### run Docker at local with the image apache/kafka:latest and have the port 9092 exposed

### Start the SpringBoot application by running org.example.Application

### To access Swagger UI and send test message:
http://localhost:8088/swagger-ui/index.html#/kafka-controller/publishMessage
### Health Check
http://localhost:8088/actuator/health
http://localhost:8088/actuator/info
http://localhost:8088/actuator/metrics
