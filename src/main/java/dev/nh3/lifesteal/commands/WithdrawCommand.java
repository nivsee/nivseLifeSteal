package dev.nh3.lifesteal.commands;

import dev.nh3.lifesteal.LifeSteal;
import dev.nh3.lifesteal.config.Config;
import dev.nh3.lifesteal.database.entities.PlayerEntity;
import dev.nh3.lifesteal.items.Heart;
import dev.nh3.lifesteal.utils.Utils;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

@Command(name = "lifesteal withdraw")
public class WithdrawCommand{
    @Execute
    public void execute(@Context Player sender, @Arg int ammount) throws SQLException {
        if(sender.getPlayer() == null) return;

        if(Utils.isInventoryFull(sender.getPlayer())){
            sender.sendMessage(Utils.translate(Config.getString("messages.full-inventory")));
        }

        PlayerEntity pe = LifeSteal.getDatabase().getPlayerDao().queryForId(sender.getUniqueId().toString());
        int health = pe.getHealth() - (ammount * 2);
        if (health <= 0){
            sender.sendMessage(Utils.translate(Config.getString("messages.not-enough-hearts")));
            return;
        }

        Heart heart = new Heart(
                Config.getInt("heart-item.custommodeldata"),
                Config.getInt("heart-item.hp"),
                Material.valueOf(Config.getString("heart-item.material"))
        );

        ItemStack itemStack = heart.getItemStack();
        itemStack.setAmount(ammount);

        sender.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        pe.setHealth(health);

        LifeSteal.getDatabase().getPlayerDao().update(pe);
        sender.getInventory().addItem(itemStack);
    }
}
