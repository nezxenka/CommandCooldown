package org.nezxenka.commandcooldown.core.database;

import java.util.UUID;

/**
 * Интерфейс для работы с базой данных
 */
public interface IDatabase {

    /**
     * Подключение к базе данных
     */
    void connect();

    /**
     * Создание таблицы для хранения кулдаунов
     */
    void createTable();

    /**
     * Получение времени окончания кулдауна
     *
     * @param playerId UUID игрока
     * @param command  Команда
     * @return Время окончания кулдауна в миллисекундах или null
     */
    Long getCooldown(UUID playerId, String command);

    /**
     * Установка кулдауна
     *
     * @param playerId    UUID игрока
     * @param command     Команда
     * @param cooldownEnd Время окончания кулдауна в миллисекундах
     */
    void setCooldown(UUID playerId, String command, long cooldownEnd);

    /**
     * Удаление кулдауна
     *
     * @param playerId UUID игрока
     * @param command  Команда
     */
    void removeCooldown(UUID playerId, String command);

    /**
     * Очистка всех кулдаунов игрока
     *
     * @param playerId UUID игрока
     */
    void clearPlayerCooldowns(UUID playerId);

    /**
     * Закрытие соединения с базой данных
     */
    void closeConnection();

    /**
     * Проверка соединения с базой данных
     */
    void checkConnection();
}
