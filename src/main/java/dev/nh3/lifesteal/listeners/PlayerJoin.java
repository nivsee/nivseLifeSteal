package dev.nh3.lifesteal.listeners;

import dev.nh3.lifesteal.LifeSteal;
import dev.nh3.lifesteal.config.Config;
import dev.nh3.lifesteal.database.entities.PlayerEntity;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoin implements Listener {
    @EventHandler()
    void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();

        LifeSteal.getDatabase().getPlayerDao().createIfNotExists(new PlayerEntity(player.getUniqueId().toString(), Config.getInt("health")));
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(LifeSteal.getDatabase().getPlayerDao().queryForId(player.getUniqueId().toString()).getHealth());
    }
}
