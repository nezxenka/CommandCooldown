# Руководство по использованию CommandCooldown

## Команды

### Установка кулдауна

Установить кулдаун для игрока на определенную команду:

```
/commandcooldown set <ник> <команда> <секунды>
```

Примеры:
```
/commandcooldown set Player123 fix 43200
/commandcooldown set Player123 tpa 5
/commandcooldown set Player123 pay 15
```

Алиасы команды: `/cmdcd`, `/ccd`

### Удаление кулдауна

Удалить кулдаун для конкретной команды:

```
/commandcooldown remove <ник> <команда>
```

Примеры:
```
/commandcooldown remove Player123 fix
/commandcooldown remove Player123 tpa
```

### Очистка всех кулдаунов

Очистить все кулдауны игрока:

```
/commandcooldown clear <ник>
```

Пример:
```
/commandcooldown clear Player123
```

## Настройка кулдаунов

### config.yml

Настройте кулдауны для команд в файле `config.yml`:

```yaml
cooldowns:
  fix: 43200        # 12 часов (в секундах)
  'tpa ': 5         # 5 секунд
  lightning: 604800 # 7 дней
  ext: 60           # 1 минута
  'pay ': 15        # 15 секунд
  'broadcast ': 300 # 5 минут
```

Время указывается в секундах:
- 60 секунд = 1 минута
- 3600 секунд = 1 час
- 86400 секунд = 1 день

### Пробелы в командах

Если команда содержит аргументы, добавьте пробел после названия:

```yaml
cooldowns:
  'tpa ': 5    # Команда /tpa с аргументами
  tpa: 10      # Команда /tpa без аргументов
```

## База данных

### database.yml

Выберите тип базы данных:

```yaml
storage:
  type: SQLITE  # SQLITE или MYSQL
```

### SQLite (по умолчанию)

Не требует настройки, работает сразу:

```yaml
sqlite:
  filename: "cooldowns.db"
```

### MySQL

Для больших серверов настройте MySQL:

```yaml
storage:
  type: MYSQL

mysql:
  host: "localhost"
  port: 3306
  database: "commandcooldown"
  username: "root"
  password: "password"
  useSSL: false
```

## Сообщения

### messages.yml

Настройте все сообщения плагина:

```yaml
prefix: "&8[&6CommandCooldown&8] &f"

cooldown:
  active: "{prefix}&cПожалуйста, подождите &e{time} &cдля использования этой команды."
```

Поддерживаемые плейсхолдеры:
- `{prefix}` - префикс плагина
- `{player}` - имя игрока
- `{command}` - название команды
- `{time}` - оставшееся время

Цветовые коды:
- `&0-9, a-f` - стандартные цвета
- `&l` - жирный
- `&o` - курсив
- `&n` - подчеркнутый
- `&m` - зачеркнутый
- `&r` - сброс форматирования

## Права доступа

```yaml
commandcooldown.admin  - Доступ ко всем командам управления (по умолчанию: op)
commandcooldown.bypass - Обход всех кулдаунов (по умолчанию: op)
```

### Примеры использования прав

Дать игроку права администратора:
```
/lp user Player123 permission set commandcooldown.admin true
```

Дать группе право обхода кулдаунов:
```
/lp group vip permission set commandcooldown.bypass true
```

## API для разработчиков

### Получение API

```java
CommandCooldown plugin = (CommandCooldown) Bukkit.getPluginManager()
    .getPlugin("CommandCooldown");
CooldownAPI api = plugin.getCooldownAPI();
```

### Проверка кулдауна

```java
UUID playerId = player.getUniqueId();
String command = "fix";

if (api.isOnCooldown(playerId, command)) {
    long remaining = api.getRemainingCooldown(playerId, command);
    player.sendMessage("Осталось: " + remaining + " секунд");
}
```

### Установка кулдауна

```java
// Установить кулдаун на 60 секунд
api.setCooldown(playerId, "fix", 60);
```

### Удаление кулдауна

```java
// Удалить кулдаун для конкретной команды
api.removeCooldown(playerId, "fix");

// Очистить все кулдауны игрока
api.clearPlayerCooldowns(playerId);
```

### Проверка отслеживания команды

```java
if (api.isCommandTracked("fix")) {
    long cooldownTime = api.getCommandCooldown("fix");
    System.out.println("Кулдаун: " + cooldownTime + " секунд");
}
```

## Примеры конфигурации

### Для PvP сервера

```yaml
cooldowns:
  home: 300        # 5 минут
  spawn: 60        # 1 минута
  tpa: 10          # 10 секунд
  tpaccept: 5      # 5 секунд
  back: 180        # 3 минуты
```

### Для экономического сервера

```yaml
cooldowns:
  'pay ': 30       # 30 секунд
  shop: 5          # 5 секунд
  sell: 10         # 10 секунд
  auction: 300     # 5 минут
```

### Для ролевого сервера

```yaml
cooldowns:
  fix: 43200       # 12 часов
  feed: 3600       # 1 час
  heal: 1800       # 30 минут
  fly: 600         # 10 минут
```

## Советы

1. **Используйте SQLite** для небольших серверов (до 100 игроков)
2. **Используйте MySQL** для больших серверов и сетей
3. **Регулярно делайте бэкапы** базы данных
4. **Тестируйте кулдауны** перед применением на продакшене
5. **Используйте bypass** для VIP игроков
6. **Настройте сообщения** под стиль вашего сервера

## Устранение проблем

### Кулдауны не работают

1. Проверьте права `commandcooldown.bypass`
2. Убедитесь, что команда добавлена в `config.yml`
3. Проверьте логи сервера на ошибки

### Ошибки базы данных

1. Проверьте настройки в `database.yml`
2. Убедитесь, что MySQL сервер запущен
3. Проверьте права доступа к базе данных

### Сообщения не отображаются

1. Проверьте `messages.yml` на синтаксические ошибки
2. Убедитесь, что используются правильные плейсхолдеры
3. Перезагрузите конфигурацию
