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

        if(postHp <= 0){
            playerEntity.setHealth(Config.getInt("health"));
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Config.getInt("health"));
            database.getPlayerDao().update(playerEntity);

            List<String> commands = Config.getStringList("commands-no-hearts");
            commands.forEach(command -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
            });
            return;
        }

        playerEntity.setHealth(postHp);
        database.getPlayerDao().update(playerEntity);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(postHp);

        if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            if(e.getPlayer().getKiller() != null){
                Player attacker = e.getPlayer().getKiller();
                PlayerEntity attackerPlayerEntity = database.getPlayerDao().queryForId(attacker.getUniqueId().toString());
                int attackerPostHp = attackerPlayerEntity.getHealth() + Config.getInt("hp");
                if(attackerPostHp > Config.getInt("max-limit")) {
                    attacker.sendMessage(Utils.translate(Config.getString("messages.limit")));
                    return;
                }
                attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(attackerPostHp);
                attackerPlayerEntity.setHealth(attackerPostHp);
                database.getPlayerDao().update(attackerPlayerEntity);
            }
        }
    }
}
