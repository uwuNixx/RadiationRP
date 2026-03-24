package me.uwunixx.radiationrp.suits;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SuitManager {

    private final RadiationRP plugin;

    public SuitManager(RadiationRP plugin) {
        this.plugin = plugin;
    }

    public int getPlayerSuitLevel(Player player) {
        // TODO: считать уровень защиты по броне (NBT/метаданные)
        return 0;
    }

    public ItemStack createSuit(SuitLevel level) {
        // TODO: создать предметы брони с NBT
        return null;
    }
}
