#Base image
FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0


# Set the working directory
WORKDIR /app


# Copy the project files
COPY . .

# Executables
CMD [ "sbt", "run" ]
