package me.uwunixx.radiationrp.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import me.uwunixx.radiationrp.RadiationRP;

public class NBTUtils {

    private static final NamespacedKey SUIT_KEY =
            new NamespacedKey(RadiationRP.getInstance(), "suit_level");

    private static final NamespacedKey FILTER_KEY =
            new NamespacedKey(RadiationRP.getInstance(), "filter_level");

    private static final NamespacedKey GEIGER_KEY =
            new NamespacedKey(RadiationRP.getInstance(), "geiger");

    // --- SUIT ---

    public static ItemStack setSuitLevel(ItemStack item, int level) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(SUIT_KEY, PersistentDataType.INTEGER, level);
        item.setItemMeta(meta);
        return item;
    }

    public static int getSuitLevel(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        Integer lvl = item.getItemMeta().getPersistentDataContainer().get(SUIT_KEY, PersistentDataType.INTEGER);
        return lvl == null ? 0 : lvl;
    }

    public static boolean isSuit(ItemStack item) {
        return getSuitLevel(item) > 0;
    }

    // --- FILTER ---

    public static ItemStack setFilterLevel(ItemStack item, int level) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(FILTER_KEY, PersistentDataType.INTEGER, level);
        item.setItemMeta(meta);
        return item;
    }

    public static int getFilterLevel(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        Integer lvl = item.getItemMeta().getPersistentDataContainer().get(FILTER_KEY, PersistentDataType.INTEGER);
        return lvl == null ? 0 : lvl;
    }

    public static boolean isFilterItem(ItemStack item) {
        return getFilterLevel(item) > 0;
    }

    // --- GEIGER COUNTER ---

    public static ItemStack markGeiger(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(GEIGER_KEY, PersistentDataType.INTEGER, 1);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isGeiger(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        Integer flag = item.getItemMeta().getPersistentDataContainer().get(GEIGER_KEY, PersistentDataType.INTEGER);
        return flag != null && flag == 1;
    }
}
