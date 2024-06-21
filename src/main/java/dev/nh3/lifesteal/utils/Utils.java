package dev.nh3.lifesteal.utils;

import dev.nh3.lifesteal.LifeSteal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyFormat;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static ItemStack setPDC(ItemStack itemStack, String key, PersistentDataType<String, String> persistentDataType, String value){
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(
                new NamespacedKey(LifeSteal.getInstance(), key),
                persistentDataType,
                value
        );

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack setPDC(ItemStack itemStack, String key, PersistentDataType<Integer, Integer> persistentDataType, int value){
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(
                new NamespacedKey(LifeSteal.getInstance(), key),
                persistentDataType,
                value
        );

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static Object getPDCItemData(ItemStack itemStack, String key, PersistentDataType<?, ?> persistentDataType){
        return itemStack.getItemMeta().getPersistentDataContainer().get(
                new NamespacedKey(LifeSteal.getInstance(), key),
                persistentDataType
        );
    }

    public static List<String> replaceInLore(List<String> lore, String from, String to){
        List<String> newLore = new ArrayList<>();
        lore.forEach(line -> {
            newLore.add(line.replace(from, to));
        });
        return newLore;
    }
    public static ItemMeta setLore(List<String> lore, ItemMeta itemMeta){
        List<Component> newLore = new ArrayList<>();
        lore.forEach(line -> {
            newLore.add(Utils.translate(line));
        });
        itemMeta.lore(newLore);

        return itemMeta;
    }

    public static boolean isInventoryFull(Player player){
        Inventory inventory = player.getInventory();
        for (ItemStack item: inventory.getContents()) {
            if(item == null) {
                return false;
            }
        }
        return true;
    }

    public static String toMiniMessage(String input) {
        char[] legacyColors = "0123456789abcdef".toCharArray();
        char[] legacyFormatting = "klmno".toCharArray();
        String output = input;

        // Replace RGB color codes with MiniMessage color format
        output = output.replaceAll("&#([A-Fa-f0-9]{6})", "<#$1>");

        // Replace legacy color codes with MiniMessage color format
        for (char x : legacyColors) {
            LegacyFormat format = LegacyComponentSerializer.parseChar(x);
            TextColor color = format.color();
            output = output.replaceAll("[&]" + x, "<" + color.asHexString() + ">");
        }

        // Replace legacy formatting codes with MiniMessage formatting
        for (char x : legacyFormatting) {
            LegacyFormat format = LegacyComponentSerializer.parseChar(x);
            TextDecoration decoration = format.decoration();
            output = output.replaceAll("[&]" + x, "<" + decoration.name().toLowerCase() + ">");
        }

        output = output.replaceAll("&r", "<reset>");
        return output;
    }

    public static Component translate(String input) {
        return LifeSteal.getMiniMessage().deserialize(toMiniMessage(input)).decoration(TextDecoration.ITALIC, false);
    }
}
