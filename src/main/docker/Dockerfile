FROM openjdk:11.0.4-jre-slim

ENV INSTALL_DIRECTORY ${project.build.installDirectory}
ENV LOG_DIRECTORY ${project.build.logDirectory}

RUN mkdir -p $INSTALL_DIRECTORY $LOG_DIRECTORY && \
    groupadd coatli && \
    useradd -l -g coatli coatli && \
    chown -R coatli: $INSTALL_DIRECTORY $LOG_DIRECTORY

WORKDIR $INSTALL_DIRECTORY

USER coatli

COPY artifact/${project.build.finalName}.${project.packaging} .

ENTRYPOINT ["java","-Duser.timezone=America/Mexico_City","-jar","${project.build.finalName}.${project.packaging}"]

EXPOSE ${application.port}
