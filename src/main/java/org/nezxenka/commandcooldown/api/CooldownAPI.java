package org.nezxenka.commandcooldown.api;

import lombok.RequiredArgsConstructor;
import org.nezxenka.commandcooldown.core.CooldownManager;

import java.util.UUID;

/**
 * API для работы с кулдаунами команд
 * Используется другими плагинами для интеграции
 */
@RequiredArgsConstructor
public class CooldownAPI {

    private final CooldownManager cooldownManager;

    /**
     * Проверяет, находится ли игрок на кулдауне для команды
     *
     * @param playerId UUID игрока
     * @param command  Команда
     * @return true если игрок на кулдауне
     */
    public boolean isOnCooldown(UUID playerId, String command) {
        return cooldownManager.isOnCooldown(playerId, command);
    }

    /**
     * Устанавливает кулдаун для игрока на команду
     *
     * @param playerId UUID игрока
     * @param command  Команда
     * @param seconds  Время кулдауна в секундах
     */
    public void setCooldown(UUID playerId, String command, long seconds) {
        cooldownManager.setCooldown(playerId, command, seconds);
    }

    /**
     * Получает оставшееся время кулдауна
     *
     * @param playerId UUID игрока
     * @param command  Команда
     * @return Оставшееся время в секундах
     */
    public long getRemainingCooldown(UUID playerId, String command) {
        return cooldownManager.getRemainingCooldown(playerId, command);
    }

    /**
     * Удаляет кулдаун для игрока на команду
     *
     * @param playerId UUID игрока
     * @param command  Команда
     */
    public void removeCooldown(UUID playerId, String command) {
        cooldownManager.removeCooldown(playerId, command);
    }

    /**
     * Очищает все кулдауны игрока
     *
     * @param playerId UUID игрока
     */
    public void clearPlayerCooldowns(UUID playerId) {
        cooldownManager.clearPlayerCooldowns(playerId);
    }

    /**
     * Проверяет, отслеживается ли команда
     *
     * @param command Команда
     * @return true если команда отслеживается
     */
    public boolean isCommandTracked(String command) {
        return cooldownManager.isCommandTracked(command);
    }

    /**
     * Получает время кулдауна для команды из конфига
     *
     * @param command Команда
     * @return Время кулдауна в секундах
     */
    public long getCommandCooldown(String command) {
        return cooldownManager.getCommandCooldown(command);
    }
}
