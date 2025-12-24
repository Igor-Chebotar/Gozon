# Gozon

Система интернет-магазина с микросервисной архитектурой.

## Запуск

```bash
docker compose up --build
```

## Доступ

- Frontend: http://localhost:3000
- API: http://localhost:8080
- RabbitMQ: http://localhost:15672 (guest/guest)

## API

### Счета

```bash
# создать счет
curl -X POST http://localhost:8080/api/accounts -H "Content-Type: application/json" -d '{"userId": "user1"}'

# пополнить
curl -X POST http://localhost:8080/api/accounts/topup -H "Content-Type: application/json" -d '{"userId": "user1", "amount": 1000}'

# баланс
curl http://localhost:8080/api/accounts/user1
```

### Заказы

```bash
# создать заказ
curl -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d '{"userId": "user1", "amount": 100, "description": "test"}'

# список заказов
curl http://localhost:8080/api/orders?userId=user1

# статус заказа
curl http://localhost:8080/api/orders/{orderId}
```

## Архитектура

Frontend -> API Gateway -> Orders Service -> RabbitMQ -> Payments Service

### Transactional Outbox
При создании заказа сохраняем событие в таблицу outbox в той же транзакции. Фоновый процесс читает outbox и отправляет в очередь.

### Transactional Inbox
При получении события проверяем order_id в таблице inbox. Если уже есть - пропускаем (защита от дубликатов).

### Exactly-once
Деньги списываются ровно один раз за заказ благодаря inbox.

## Статусы

- NEW - создан
- FINISHED - оплачен
- CANCELLED - не хватило денег
