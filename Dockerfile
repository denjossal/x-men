FROM adoptopenjdk/openjdk11
COPY build/libs/mutant-*-all.jar mutant.jar
EXPOSE 8080
RUN chmod 755 mutant.jar
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-XX:+IdleTuningGcOnIdle", "-Xtune:virtualized", "-jar", "mutant.jar"]
