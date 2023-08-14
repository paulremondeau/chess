#Base image
FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0


# Set the working directory
WORKDIR /app


# Copy the project files
COPY . .

EXPOSE 8080

# Executables
CMD [ "sbt", "run" ]
