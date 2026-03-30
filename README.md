# CommandCooldown

![Version](https://img.shields.io/badge/version-1.0-blue.svg)
![Minecraft](https://img.shields.io/badge/minecraft-1.16.5-green.svg)
![Java](https://img.shields.io/badge/java-17-orange.svg)
![License](https://img.shields.io/badge/license-Proprietary-red.svg)

Профессиональная система управления кулдаунами команд для PaperMC серверов.

## Правообладатель

**ООО "Алекс-Групп"**  
ОГРН: 1257700237134  
ИНН: 7720950383

© 2026 ООО "Алекс-Групп". Все права защищены.

## 📋 Описание

CommandCooldown - это мощный плагин для управления кулдаунами команд на вашем Minecraft сервере. Плагин позволяет устанавливать временные ограничения на использование любых команд, предотвращая спам и злоупотребления.

## ✨ Возможности

- 🔒 **Гибкая система кулдаунов** - устанавливайте кулдауны для любых команд
- 💾 **Поддержка баз данных** - SQLite и MySQL
- 🎨 **Настраиваемые сообщения** - полная кастомизация всех текстов
- ⚡ **Высокая производительность** - оптимизированная работа с БД
- 🔧 **Простая настройка** - интуитивно понятные конфигурационные файлы
- 🛡️ **Система прав** - обход кулдаунов для администраторов
- 📊 **API для разработчиков** - интеграция с другими плагинами
- 🔄 **Автоматическое переподключение** - к базе данных при потере соединения

## 📦 Требования

- **Сервер**: PaperMC 1.16.5+
- **Java**: 17+
- **Зависимости**: PlaceholderAPI (опционально)

## 🚀 Установка

1. Скачайте последнюю версию плагина
2. Поместите `.jar` файл в папку `plugins/`
3. Перезапустите сервер
4. Настройте конфигурационные файлы в `plugins/CommandCooldown/`

## ⚙️ Конфигурация

### config.yml
Основной файл конфигурации для настройки кулдаунов команд:

```yaml
cooldowns:
  fix: 43200        # 12 часов
  'tpa ': 5         # 5 секунд
  lightning: 604800 # 7 дней
  ext: 60           # 1 минута
  'pay ': 15        # 15 секунд
  'broadcast ': 300 # 5 минут
```

### database.yml
Настройка подключения к базе данных:

```yaml
storage:
  type: SQLITE  # SQLITE или MYSQL

mysql:
  host: "localhost"
  port: 3306
  database: "commandcooldown"
  username: "root"
  password: "password"
  useSSL: false

sqlite:
  filename: "cooldowns.db"
```

### messages.yml
Настройка всех сообщений плагина с поддержкой цветовых кодов.

## 📝 Команды

| Команда | Описание | Права |
|---------|----------|-------|
| `/commandcooldown set <ник> <команда> <секунды>` | Установить кулдаун | `commandcooldown.admin` |
| `/commandcooldown remove <ник> <команда>` | Удалить кулдаун | `commandcooldown.admin` |
| `/commandcooldown clear <ник>` | Очистить все кулдауны игрока | `commandcooldown.admin` |

**Алиасы**: `/cmdcd`, `/ccd`

## 🔑 Права доступа

| Право | Описание | По умолчанию |
|-------|----------|--------------|
| `commandcooldown.admin` | Доступ к управлению кулдаунами | OP |
| `commandcooldown.bypass` | Обход всех кулдаунов | OP |

## 🔌 API для разработчиков

```java
// Получение API
CommandCooldown plugin = (CommandCooldown) Bukkit.getPluginManager().getPlugin("CommandCooldown");
CooldownAPI api = plugin.getCooldownAPI();

// Проверка кулдауна
boolean isOnCooldown = api.isOnCooldown(playerUUID, "command");

// Установка кулдауна
api.setCooldown(playerUUID, "command", 60); // 60 секунд

// Получение оставшегося времени
long remaining = api.getRemainingCooldown(playerUUID, "command");

// Удаление кулдауна
api.removeCooldown(playerUUID, "command");

// Очистка всех кулдаунов игрока
api.clearPlayerCooldowns(playerUUID);
```

## 🏗️ Структура проекта

```
src/main/java/org/nezxenka/commandcooldown/
├── api/                    # API для разработчиков
├── command/                # Команды и tab completer
├── core/                   # Основная логика
│   ├── config/            # Менеджеры конфигурации
│   └── database/          # Работа с базами данных
│       └── impl/          # Реализации (MySQL, SQLite)
├── listener/              # Обработчики событий
└── model/                 # Модели данных
```

## 📊 Поддерживаемые базы данных

- **SQLite** - локальное хранение, не требует настройки
- **MySQL** - для больших серверов и сетей

## 🛠️ Сборка

```bash
mvn clean package
```

Скомпилированный `.jar` файл будет находиться в папке `target/`.

## 📄 Лицензия

Этот проект является проприетарным программным обеспечением.  
© 2026 ООО "Алекс-Групп". Все права защищены.

Подробности в файле [LICENSE](LICENSE).

## 👥 Разработчики

**ООО "Алекс-Групп"**
- Организация: ООО "Алекс-Групп"

## 📞 Поддержка

При возникновении проблем или вопросов:
1. Проверьте документацию
2. Убедитесь, что используете последнюю версию
3. Проверьте логи сервера на наличие ошибок

## 🔄 История версий

### 1.0 (Текущая)
- Первый релиз
- Поддержка SQLite и MySQL
- Система кулдаунов команд
- API для разработчиков
- Настраиваемые сообщения

---

**Сделано с ❤️ для Minecraft сообщества**