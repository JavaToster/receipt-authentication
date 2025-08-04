# Этап 1: Сборка приложения
FROM maven:3.9.5-eclipse-temurin-21 AS builder
WORKDIR /app

# Сначала копируем только pom.xml — зависимости будут кэшированы
COPY pom.xml .

# Предзагрузка зависимостей (будет использовать кэш при повторной сборке)
RUN mvn dependency:go-offline -B

# Теперь копируем исходники
COPY src ./src

# Собираем проект (будет использовать кэшированные зависимости)
RUN mvn clean package -DskipTests

# Этап 2: Финальный образ
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Копируем собранный JAR из предыдущего этапа
COPY --from=builder /app/target/*.jar app.jar

# Указываем порт, на котором работает Spring Boot
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]
