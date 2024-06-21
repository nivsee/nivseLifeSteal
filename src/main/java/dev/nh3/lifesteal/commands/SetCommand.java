package dev.nh3.lifesteal.commands;

import dev.nh3.lifesteal.LifeSteal;
import dev.nh3.lifesteal.database.entities.PlayerEntity;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@Command(name = "lifesteal set")
public class SetCommand{
    @Execute
    @Permission("lifesteal.set")
    public void execute(@Arg Player player, @Arg int hp) throws SQLException {
        if(hp <= 0 || hp % 2 == 1) return;

        PlayerEntity pe = LifeSteal.getDatabase().getPlayerDao().queryForId(player.getUniqueId().toString());
        pe.setHealth(hp);
        LifeSteal.getDatabase().getPlayerDao().update(pe);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
    }
}
