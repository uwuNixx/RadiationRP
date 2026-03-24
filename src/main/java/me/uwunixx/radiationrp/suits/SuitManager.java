package me.uwunixx.radiationrp.suits;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.items.NBTUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SuitManager {

    private final RadiationRP plugin;

    public SuitManager(RadiationRP plugin) {
        this.plugin = plugin;
    }

    public ItemStack createSuitPiece(Material material, int level) {
        ItemStack item = new ItemStack(material);
        return NBTUtils.setSuitLevel(item, level);
    }

    public ItemStack[] createFullSuit(int level) {
        return new ItemStack[]{
                createSuitPiece(Material.IRON_HELMET, level),
                createSuitPiece(Material.IRON_CHESTPLATE, level),
                createSuitPiece(Material.IRON_LEGGINGS, level),
                createSuitPiece(Material.IRON_BOOTS, level)
        };
    }

    // 🔥 Вот этот метод тебе и нужен
    public ItemStack[] createSuit(SuitLevel level) {
        return createFullSuit(level.getLevel());
    }

    public int getPlayerSuitLevel(Player player) {
        int min = Integer.MAX_VALUE;

        for (ItemStack item : player.getInventory().getArmorContents()) {
            int lvl = NBTUtils.getSuitLevel(item);
            if (lvl == 0) return 0;
            min = Math.min(min, lvl);
        }

        return min == Integer.MAX_VALUE ? 0 : min;
    }
}
