springMoneyTransfer project

Данный проект создан с использованием spring boot и реализует апи связи с https://serp-ya.github.io/card-transfer/ 
(исходный репозиторий здесь: https://github.com/serp-ya/card-transfer).
Для локального запуска проекта удобнее всего воспользоваться IDE, где нужно запускать main в com.example.springmoneytransfer.SpringTransferApplication,
предварительно скомпилировав проект средствами IDE.
Для запуска без IDE необходимо собрать проект в jar файл с помощью gradle, отключив запуск интеграционных 
тестов (или любых тестов) и запустить его, выполнив команду
```
java -jar jarFileName.jar
```
Если интеграционное тестирование не отключить, то собрать проект не удастся, так как интеграционные тесты запускают преподготовленный
docker образ, который заранее (если есть потребность интеграционного тестирования) следует собрать на основе собранного jar файла и хранить в локальном репозитории docker под именем
money-transfer:latest (см. dockerfile и docker-compose файлы).

После запуска сервиса, можно делать локальные пост-запросы, по умолчанию проект стартует на порту 5500.

Запрос на /transfer (если это локалхост и дефолтный порт, то http://localhost:5500/transfer).
Пример json: {"cardFromNumber": "5540000000000000", "cardFromValidTill": "12/25", "cardFromCVV": "330", "cardToNumber": "5530000000000000",
"amount": {"value": 1000, "currency": "RUR"}}.
В случае, если карты есть в условной базе данных (repository level сервиса), а карта списания имеет достаточно 
средств на счету, то в ответ придет json формата: {"operationId": "0ae746e4-b885-48ed-b5ae-c554c4368df2"}. При этом
операция выполнена пока не будет, а будет внесена в список проверенных операций, сервис будет ожидать её верификации.

Запрос на /confirmOperation (если это локалхост и дефолтный порт, то http://localhost:5500/confirmOperation).
Пример json: {"operationId": "0f851972-45d9-4bd1-a443-433e6b985f7c", "code": "0000"}
В случае, если по данному номеру операции есть операция в списке проверенных операций, а верификационный код верен, то
сервис попытается выполнить операцию и в случае успешного выполнения вернет такой же ответ как и при изначальном
запросе на трансфер, то есть повторит ответ. Если же в промежутке между выполнением проверки и запросом на верификацию 
деньги уже были списаны так, что на счете карты стало недостаточно средств, то вернется ошибка формата:
{"message": "money is not enough on a card", "id": 0}.
