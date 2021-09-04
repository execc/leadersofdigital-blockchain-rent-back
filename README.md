# Децентрализованная платформа обеспечения арендных платежей

Платформа обеспечивает взаимодействие трех видов участников – арендодателей, 
арендаторов и банков – и позволяет ликвидировать дебиторскую задолженность 
арендодателей за счет гарантированного получения арендной платы путем автоматического 
кредитования арендаторов. Ежедневное фондирование процентов от выручки арендаторов 
повышает дисциплину финансового планирования арендаторов и позволяет уменьшить сроки 
получения денежных средств арендодателями. За счет применения технологии распределенных 
реестров и смарт-контрактов платформа снижает издержки 
взаимодействия всех участвующих сторон.

## Сборка проекта

Перед сборкой необходимо установить следующее ПО:
- OpenJDK 1.8.0_212+ (Java 11 и выше не подойдут)
- Docker версии 20.10+

На Unix/Linux системах перед сборкий нужно выполнить команду `chmod +x ./gradlew`

Сборка проекта осуществляется командой: `./gradlew clean build dockerTag -x test`

## Запуск проекта

Перед запуском необходимо запустить БД PostgreSQL. 
Запустить БД можно при помощи команды:

```
docker run --name app-pg -d -e POSTGRES_PASSWORD=password -e POSTGRES_USER=postgres -e POSTGRES_DB=db_rent_app -p 5432:5432 -d postgres:11.10
```

После запуска БД можно запустить проект командой 

```
docker run --name rent-app -p 8080:8080 registry.weintegrator.com/rent/rent-webapp-app:latest   
```

## Работа с проектом

Проверить работу сервисов бэк-энда можно с использованием REST API. Для этого нужно открыть
ресурс `http://localhost:8080/swagger-ui.html` в браузуре. 

Для работы с REST необходима авторизация, которая проходит с использованием протокола Oauth2.
Для авторизации нужно нажать кнопку `Authorize`, в поле `client` 
заменить `api-client` на `demo-client`, и воспользовать одной из следующих пар логин/пароль:

```
sheremetevo/sheremetevo - для работы от лица Арендодателя
alfabank/alfabank - для работы от лица Банка
farsh/farsh - для работы от лица Арендатора (Бургерная #ФАРШ)
beluga/beluga - для работы от лица Арендатора (Beluga Caviar Bar)
kofehaus/kofehaus - для работы от лица Арендатора (Кофе-Хауз)
```

Описания REST методов приведены ниже.

**GET /rent** - Возвращает список всех созданных договоров на аренду (любой участник)

**POST /rent** - Создает новый договор на аренду (Арендодатель)

**POST /rent/{id}/acceptCreditConditions** - Акцепт договора и условий кредита Арендатором (Арендатор)

**POST /rent/{id}/enterCreditConditions** - Установка условий кредита Банком (Банк)

**POST /rent/{id}/enterEarnings** - Отправка платежа на расщепление Банком (Банк)

**POST /rent/{id}/payCredit** - Отправка платежа в счет погашения кредита (Банк)

**POST /rent/{id}/payDebt** - Отправка платежа в счет погашения задолженности по Аренде (Банк)

**POST /rent/{id}/takeRent** - Старт нового расчетного периода (Арендатор)

**POST /rent/tx/{id}/status** - Получение статуса траназакции в Блокчейне (любой участник)

## Использование обозревателя Блокчейн (Blockchain Explorer)

Блокчейн эксплорер тестовой сети расположен по адресу: https://explorer.rent.weintegrator.com/.
Для его использования необходимо зарегистрироваться или использовать пару логин/пароль: `dvasin@web3tech.ru / Dvasin@web3tech.ru` 

## Тестовый стенд

Тестовый стенд (бек + фронт + блокчейн) расположен по адресу https://rent.weintegrator.com/