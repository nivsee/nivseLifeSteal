package dev.nh3.lifesteal.listeners;

import dev.nh3.lifesteal.LifeSteal;
import dev.nh3.lifesteal.config.Config;
import dev.nh3.lifesteal.database.entities.PlayerEntity;
import dev.nh3.lifesteal.utils.Utils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.sql.SQLException;

public class PlayerInteract implements Listener {

    @EventHandler
    void onPlayerInteract(PlayerInteractEvent e) throws SQLException {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if( action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK ){
            if(player.getInventory().getItemInMainHand().getType() == Material.AIR) return;
            Integer hp = (Integer) Utils.getPDCItemData(player.getInventory().getItemInMainHand(), "hp", PersistentDataType.INTEGER);
            if (hp == null) return;

            PlayerEntity pe = LifeSteal.getDatabase().getPlayerDao().queryForId(player.getUniqueId().toString());
            if(pe.getHealth() + hp > Config.getInt("max-limit")) {
                player.sendMessage(Utils.translate(Config.getString("messages.limit")));
                return;
            }
            pe.setHealth(pe.getHealth() + hp);
            LifeSteal.getDatabase().getPlayerDao().update(pe);

            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + hp);
            player.getInventory().getItemInMainHand().subtract(1);

            player.sendMessage(Utils.translate(Config.getString("messages.consumed").replace("{hearts}", String.valueOf(hp / 2))));
        }
    }
}
