package dev.nh3.lifesteal.config;

import dev.nh3.lifesteal.LifeSteal;

import java.util.List;

public class Config {
    public static void reload(){
        LifeSteal.getInstance().reloadConfig();
    }

    public static String getString(String path){
        return LifeSteal.getInstance().getConfig().getString(path);
    }

    public static Integer getInt(String path){
        return LifeSteal.getInstance().getConfig().getInt(path);
    }

    public static List<String> getStringList(String path){
        return LifeSteal.getInstance().getConfig().getStringList(path);
    }
}
