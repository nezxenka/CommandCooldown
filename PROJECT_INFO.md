# Информация о проекте CommandCooldown

## Общая информация

**Название:** CommandCooldown  
**Версия:** 1.0  
**Тип:** Плагин для Minecraft (Paper/Spigot)  
**Язык программирования:** Java 17  
**Система сборки:** Maven

## Правообладатель

**ООО "Алекс-Групп"**  
ОГРН: 1257700237134  
ИНН: 7720950383  
КПП: 772001001

© 2026 ООО "Алекс-Групп". Все права защищены.

## Структура проекта

```
CommandCooldown/
├── src/main/java/org/nezxenka/commandcooldown/  # Исходный код
│   ├── api/                                      # API для разработчиков
│   │   └── CooldownAPI.java                     # Публичный API
│   ├── command/                                  # Система команд
│   │   ├── CommandCooldownCommand.java          # Обработчик команд
│   │   └── CommandCooldownTabCompleter.java     # Автодополнение
│   ├── core/                                     # Основная логика
│   │   ├── config/                              # Конфигурация
│   │   │   ├── ConfigManager.java               # Менеджер конфигов
│   │   │   └── MessageManager.java              # Система сообщений
│   │   ├── database/                            # База данных
│   │   │   ├── impl/                            # Реализации БД
│   │   │   │   ├── MySQLDatabase.java           # MySQL
│   │   │   │   └── SQLiteDatabase.java          # SQLite
│   │   │   ├── DatabaseManager.java             # Менеджер БД
│   │   │   └── IDatabase.java                   # Интерфейс БД
│   │   └── CooldownManager.java                 # Менеджер кулдаунов
│   ├── listener/                                 # Обработчики событий
│   │   └── CommandListener.java                 # Перехват команд
│   ├── model/                                    # Модели данных
│   │   └── DatabaseType.java                    # Типы БД
│   └── CommandCooldown.java                      # Главный класс
├── src/main/resources/
│   ├── config.yml                                # Конфигурация кулдаунов
│   ├── database.yml                              # Настройки БД
│   ├── messages.yml                              # Локализация
│   └── plugin.yml                                # Описание плагина
└── Документация
    ├── README.md                                 # Основная документация
    ├── ARCHITECTURE.md                           # Архитектура
    ├── USAGE.md                                  # Руководство пользователя
    ├── PROJECT_INFO.md                           # Информация о проекте
    ├── CHANGELOG.md                              # История изменений
    ├── LICENSE                                   # Лицензия (RU)
    ├── LICENSE.en                                # License (EN)
    ├── COPYRIGHT                                 # Авторские права
    ├── LICENSE_INFO.md                           # Информация о лицензии
    ├── CONTRIBUTING.md                           # Руководство по участию
    └── SECURITY.md                               # Политика безопасности
```

## Технологии

### Основные
- Java 17
- Paper API 1.16.5
- Maven 3.x

### Библиотеки
- Lombok 1.18.30 (compile-time)
- PlaceholderAPI 2.11.5 (runtime, optional)
- MySQL Connector/J 8.2.0
- SQLite JDBC 3.44.1.0

### Паттерны проектирования
- Strategy Pattern (разные реализации БД)
- Singleton Pattern (главный класс)
- Builder Pattern (SQL запросы)
- Facade Pattern (API)
- Template Method Pattern (IDatabase)

## Возможности

1. **Система кулдаунов команд**
   - Гибкая настройка времени
   - Поддержка любых команд
   - Автоматическое отслеживание

2. **Поддержка баз данных**
   - SQLite (локальное хранение)
   - MySQL (для больших серверов)
   - Автоматическое переподключение

3. **Управление**
   - Команды администратора
   - Установка/удаление кулдаунов
   - Очистка всех кулдаунов игрока

4. **Система прав**
   - `commandcooldown.admin` - управление
   - `commandcooldown.bypass` - обход кулдаунов

5. **API для разработчиков**
   - Публичный API для интеграции
   - Простые методы работы с кулдаунами
   - Полная документация

6. **Локализация**
   - Полная система сообщений
   - Поддержка цветовых кодов
   - Легкий перевод через messages.yml

## Команды

```
/commandcooldown set <ник> <команда> <секунды>  - Установить кулдаун
/commandcooldown remove <ник> <команда>          - Удалить кулдаун
/commandcooldown clear <ник>                     - Очистить все кулдауны

Алиасы: /cmdcd, /ccd
```

## API для разработчиков

```java
// Получение API
CooldownAPI api = CommandCooldown.getInstance().getCooldownAPI();

// Проверка кулдауна
boolean isOnCooldown = api.isOnCooldown(playerUUID, "command");

// Установка кулдауна
api.setCooldown(playerUUID, "command", 60);

// Получение оставшегося времени
long remaining = api.getRemainingCooldown(playerUUID, "command");

// Удаление кулдауна
api.removeCooldown(playerUUID, "command");

// Очистка всех кулдаунов
api.clearPlayerCooldowns(playerUUID);
```

## Сборка

```bash
mvn clean package
```

Результат: `target/CommandCooldown-1.0.jar`

## Требования

### Сервер
- Paper/Spigot 1.16.5 или выше
- Java 17 или выше

### Плагины
- PlaceholderAPI (опционально)

## Лицензия

Проприетарная лицензия. Все права защищены.  
Подробности в файлах LICENSE и COPYRIGHT.

## Контакты

**ООО "Алекс-Групп"**  
ОГРН: 1257700237134  
ИНН: 7720950383

Для получения коммерческой лицензии или технической поддержки обращайтесь к правообладателю.

---

**Дата создания:** 2026  
**Статус:** Production Ready  
**Качество кода:** Enterprise Level

**ВНИМАНИЕ:** Выдача себя за владельца или автора данного плагина является
уголовным преступлением по статье 146 УК РФ и преследуется по закону!
