package dev.nh3.lifesteal.commands;

import dev.nh3.lifesteal.config.Config;
import dev.nh3.lifesteal.utils.Utils;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "lifesteal reload")
public class ReloadCommand{
    @Execute
    @Permission("lifesteal.reload")
    public void execute(@Context Player player) {
        Config.reload();
        player.sendMessage(Utils.translate("<GRADIENT:#b366ff:#4db2ff>nivseLifeSteal</GRADIENT> <dark_gray>|</dark_gray> <green>Przeładowano plugin!</green>"));
    }
}
