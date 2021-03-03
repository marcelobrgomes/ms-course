FROM openjdk:11
VOLUME /tmp
EXPOSE 8761
ADD ./target/hr-payroll-0.0.1-SNAPSHOT.jar hr-payroll.jar
ENTRYPOINT ["java","-jar","/hr-payroll.jar"]