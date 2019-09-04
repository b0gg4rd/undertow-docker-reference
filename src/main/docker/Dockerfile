FROM openjdk:8u201-jre-alpine

RUN mkdir -p ${project.build.installDirectory}

COPY artifact/${project.build.finalName}.${project.packaging} ${project.build.installDirectory}

ENTRYPOINT ["java","-Duser.timezone=America/Mexico_City","-jar","${project.build.installDirectory}/${project.build.finalName}.${project.packaging}"]

EXPOSE ${application.port}
