# AutoMine

Профессиональная система автоматической регенерации шахт для Minecraft серверов.

## Правообладатель

**ООО "Алекс-Групп"**  
ОГРН: 1257700237134  
ИНН: 7720950383

© 2026 ООО "Алекс-Групп". Все права защищены.

## Возможности

- Автоматическая регенерация шахт с настраиваемыми таймерами
- Поддержка трех типов шахт (Overworld, End, Nether)
- Система редкостей с весами (Default, Epic, Legendary)
- Интеграция с PlaceholderAPI
- Настраиваемые распределения блоков
- Полная локализация через messages.yml
- Команды администратора для ручного управления

## Структура проекта

```
org.nezxenka.automine/
├── command/              # Система команд
│   ├── executor/         # Исполнители
│   ├── handler/          # Обработчики подкоманд
│   └── completer/        # Автодополнение
├── config/               # Конфигурация
│   ├── data/             # Модели данных
│   └── loader/           # Загрузчики
├── generation/           # Генерация блоков
│   └── data/             # Данные генерации
├── integration/          # Интеграции
├── listener/             # События
│   └── handler/          # Обработчики событий
├── message/              # Система сообщений
├── region/               # Регионы
│   ├── data/             # Данные регионов
│   └── handler/          # Обработчики регионов
├── scheduler/            # Планировщик
│   ├── selector/         # Выбор редкости
│   └── task/             # Задачи обновления
└── util/                 # Утилиты
```

## Команды

```
/automine update <overworld|end|nether> <default|epic|legendary>
/automine timer <overworld|end|nether> <секунды>
/automine settings <overworld|endnether> time <секунды>
```

## Placeholders

```
%automine_overworld_current%  - Текущая редкость
%automine_overworld_next%     - Следующая редкость
%automine_overworld_minutes%  - Минуты до обновления
%automine_overworld_seconds%  - Секунды до обновления
```

Аналогично для `end` и `nether`.

## Конфигурация

### config.yml
Основная конфигурация шахт, регионов и редкостей.

### messages.yml
Все сообщения плагина для локализации.

## Зависимости

- Paper/Spigot 1.16.5+
- PlaceholderAPI
- Lombok (compile-time)

## Сборка

```bash
mvn clean package
```

Готовый JAR: `target/AutoMine-1.0.jar`

## Лицензия

Все права защищены © 2026 ООО "Алекс-Групп"  
ОГРН: 1257700237134, ИНН: 7720950383

Подробности в файле [LICENSE](LICENSE).