# Решение на конкурс AgroIntelligence.Meteo

## Архитектура
Архитектура «клиент-сервер» с тонким клиентом
Приложение состоит из 2 проектов:
- Сервер - уровень обработки запросов
- Веб-приложение – уровень представлений


## Технологии
Клиентская часть
- Язык программировани TypeScript
- Фреймворк разработки для frontend части – ReactJS
- react-router для навигации
- react-chart-js 2 для отрисовки графики
- Date js - для работы с времнем
- material ui - библиотека компонентов

Серверная часть 
- Язык программирования Java
- Фреймворк разработки для серверной части Spring

## Реализованные функции
1) Визуализация параметра 
 - солнечная радиация
 - осадки
 - скорость ветра
 - влажность листа
 - температура воздуха
 - влажность воздуха
 - точка росы

2) Исторические данные об осадках в сравнении с нормой для региона

3) Исторические данные о температуре в сравнении с нормой для региона

4) Нарастающее количество выпавших осадков

## Установка

Необходим установленный [docker](https://www.docker.com/products/docker-desktop/), чтобы запустить приложение.

Выполнить следующие команды в командой строке. 
1) Для запуска серверной части

```sh
   docker pull maripavlova/agroserver
   docker run -p 8080:8080 maripavlova/agroserver
```

2) Для запуска клиентской части

```sh
   docker pull maripavlova/agrofrontend
   docker run -p 3000:3000 maripavlova/agroserver
```
После выполнения команд
Серверная часть будет доступна по адресу http://localhost:8080
Клиентская часть будет доступна по адресу http://localhost:3000

