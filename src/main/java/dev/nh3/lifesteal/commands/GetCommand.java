package dev.nh3.lifesteal.commands;

import dev.nh3.lifesteal.config.Config;
import dev.nh3.lifesteal.items.Heart;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Command(name = "lifesteal get-hearts")
public class GetCommand{
    @Execute
    @Permission("lifesteal.get")
    public void execute(@Arg Player player, @Arg int customModelData, @Arg int hp) {
        Heart heart = new Heart(customModelData, hp, Material.valueOf(Config.getString("heart-item.material")));
        player.getInventory().addItem(heart.getItemStack());
    }
}
