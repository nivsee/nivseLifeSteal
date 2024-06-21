package dev.nh3.lifesteal;

import dev.nh3.lifesteal.commands.GetCommand;
import dev.nh3.lifesteal.commands.ReloadCommand;
import dev.nh3.lifesteal.commands.SetCommand;
import dev.nh3.lifesteal.commands.WithdrawCommand;
import dev.nh3.lifesteal.database.Database;
import dev.nh3.lifesteal.listeners.PlayerDeath;
import dev.nh3.lifesteal.listeners.PlayerInteract;
import dev.nh3.lifesteal.listeners.PlayerJoin;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class LifeSteal extends JavaPlugin {

    private static LifeSteal instance;
    private static Database database;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private LiteCommands<CommandSender> liteCommands;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if(!getDataFolder().exists()){
            getDataFolder().mkdirs();
        }

        try {
            database = new Database(this, "jdbc:sqlite:" + getDataFolder().getAbsolutePath() + "\\database.db");
        } catch (SQLException e) {
            Bukkit.getPluginManager().disablePlugin(this);
            throw new RuntimeException(e);
        }

        this.liteCommands = LiteCommandsBukkit.builder()
                .commands(new GetCommand())
                .commands(new WithdrawCommand())
                .commands(new SetCommand())
                .commands(new ReloadCommand())
                .build();

        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    public static MiniMessage getMiniMessage(){
        return miniMessage;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Database getDatabase() {
        return database;
    }

    public static LifeSteal getInstance() {
        return instance;
    }
}
