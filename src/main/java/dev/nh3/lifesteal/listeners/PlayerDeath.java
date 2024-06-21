package dev.nh3.lifesteal.listeners;

import dev.nh3.lifesteal.LifeSteal;
import dev.nh3.lifesteal.config.Config;
import dev.nh3.lifesteal.database.Database;
import dev.nh3.lifesteal.database.entities.PlayerEntity;
import dev.nh3.lifesteal.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.sql.SQLException;
import java.util.List;

public class PlayerDeath implements Listener {
    Database database = LifeSteal.getDatabase();
    @EventHandler
    void onPlayerDeath(PlayerDeathEvent e) throws SQLException {
        Player player = e.getPlayer();
        PlayerEntity playerEntity = database.getPlayerDao().queryForId(player.getUniqueId().toString());
        int postHp = playerEntity.getHealth() - Config.getInt("lost-on-death");

        // Brak serc
        if (postHp <= 0) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Config.getInt("health"));
            playerEntity.setHealth(Config.getInt("health"));
            database.getPlayerDao().update(playerEntity);

            List<String> commands = Config.getStringList("commands-no-hearts");
            commands.forEach(command -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
            });
            return;
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(postHp);
        playerEntity.setHealth(postHp);
        database.getPlayerDao().update(playerEntity);

        // Zabicie przez gracza
        if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            Player killer = e.getPlayer().getKiller();
            if(killer != null) {
                PlayerEntity killerEntity = database.getPlayerDao().queryForId(killer.getUniqueId().toString());
                int killerPostHp = killerEntity.getHealth() + Config.getInt("lost-on-death");
                if (killerPostHp > Config.getInt("max-limit")){
                    killer.sendMessage(Utils.translate(Config.getString("messages.limit")));
                    return;
                }
                killerEntity.setHealth(killerPostHp);
                database.getPlayerDao().update(killerEntity);
                killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(killerPostHp);
            }
        }
    }
}
