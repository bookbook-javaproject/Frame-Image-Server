FROM java:8
COPY ./build/libs/*.jar app.jar

WORKDIR .
RUN mkdir /files

ENTRYPOINT ["java","-jar", "-Xmx300M","/app.jar"]
