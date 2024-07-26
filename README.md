# Banking service

**Banking service** - это  web приложения для банковских операций.

## Функциональность

- Регистрация пользователей
- Авторизация пользователей
- Перевод денежных средств на счет другого пользователя
- Возможность сделать вклад
- Другие полезные функции

## Запуск приложения

-Конфигурацию для БД(PostgreSQL) можно взять из файла "DataBaseconfig".
- Делать запросы можно через Postman.
- После авторизации пользователя нужно использовать JWT token.

## API Endpoints


- `POST /auth/signup`: Регистрация нового пользователя.
```json
{
    "username":"username",
    "fullname":"fullname",
    "email":"email@mail.ru",
    "phone": "000000000",
    "dateOfBirth":"0000-00-00",
    "balance":"0",
    "password":"password"
}
```
- `POST /auth/signin`: Авторизация пользователя.
```json
{
  "username": "username",
  "password": "password"
}
```
## Пользовательские операции
- `GET /user/getBalance`: Показать баланс пользователя.

- `POST /user/transfer`: Перевод money другому пользователю ("getUsername"-кому переводить).
```json
{
  "getUsername":"username",
  "money":"300"
}
```
- `GET /user/getPhone`: Показать телефоны пользователя.
- `GET /user/getEmail`: Показать emails пользователя.
- `POST /user/submitPhone`: Добавить phone.
```json
{
  "phone":"00000000"
}
```
- `POST /user/submitEmail`: Добавить email.
```json
{
  "email":"email@mail.ru"
}
```
- `POST /user/deletePhone`: Удалить номер телефона.
```json
{
  "phone":"00000000"
}
```
- `POST /user/deleteEmail`: Удалить email.
```json
{
  "email":"email@mail.ru"
}
```
## Фильтрация информации
- `GET /filter/all`: Показать всю информацию о всех пользователях.
- `GET /filter/fullname`: Показать всю информацию о пользователе.
## Вклад
- `POST /con/contriburion`: Сделать вклад.
```json
{
  "money":"0"
}
```
- `GET /con/moneyToTheAccount`: Положить деньги со вклада на счет.
- `GET /con/showMoneyInContribution`: Показать, сколько денег на вкладе.


## Связаться со мной
- telegram:[@dvi_zhenya](https://t.me/dvi_zhenya)
- zhenya_nikolaev_1995@list.ru
