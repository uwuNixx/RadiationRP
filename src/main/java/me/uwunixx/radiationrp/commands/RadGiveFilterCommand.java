package me.uwunixx.radiationrp.commands;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.items.NBTUtils;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RadGiveFilterCommand implements CommandExecutor {

    private final RadiationRP plugin;

    public RadGiveFilterCommand(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игрок может использовать эту команду.");
            return true;
        }

        ItemStack filter = new ItemStack(Material.FURNACE);
        NBTUtils.setFilterLevel(filter, 1); // помечаем как фильтр уровня 1
        player.getInventory().addItem(filter);

        player.sendMessage("§aВыдан фильтр.");
        return true;
    }
}
