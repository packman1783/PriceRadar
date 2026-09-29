# PriceRadar

Учебный проект: сравнение стоимости списка покупок в разных сетях
(на первом этапе — на CSV-фикстурах вместо реального скрапинга).

## Запуск

1. Поднять PostgreSQL:
   ```
   docker compose up -d
   ```

2. Запустить приложение:
   ```
   ./gradlew bootRun
   ```

3. При старте `DataInitializer` один раз наполнит базу магазинами,
   товарами и ценами из `src/main/resources/data/mock-prices.csv`.

## Проверка через API

```
curl -X POST http://localhost:8080/api/compare \
  -H "Content-Type: application/json" \
  -d '{"products": ["Молоко 1л", "Хлеб белый", "Сахар 1кг"]}'
```

Ответ — список магазинов, отсортированный по сумме корзины, с пометкой,
какие товары нашлись/отсутствуют в каждом (`coversFullList` — покрыт ли весь список).

## Структура

См. пакеты `model`, `repository`, `source` (+ `source/impl`), `comparison`,
`web`, `config` — описание архитектуры было согласовано в чате перед началом реализации.