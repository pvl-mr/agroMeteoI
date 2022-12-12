FROM openjdk:17-oracle
EXPOSE 8080
ADD target/*.jar agroserver.jar
ADD historic_temperature_2019.json historic_temperature_2019.json
ADD historic_temperature_2020.json historic_temperature_2020.json
ADD historic_temperature_2021.json historic_temperature_2021.json
ADD calculated_averages_precipitation.json calculated_averages_precipitation.json
ADD calculated_averages.json calculated_averages.json
ADD month_year_precipitations.json month_year_precipitations.json
ENTRYPOINT ["java", "-jar", "agroserver.jar"]