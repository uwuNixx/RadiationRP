package me.uwunixx.radiationrp.commands;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.items.NBTUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RadGiveFilterCommand implements CommandExecutor {

    private final RadiationRP plugin;

    public RadGiveFilterCommand(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игрок может использовать эту команду.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Использование: /radgivefilter <level>");
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(args[0]);
        } catch (Exception e) {
            player.sendMessage("Уровень должен быть числом.");
            return true;
        }

        ItemStack filter = new ItemStack(Material.FURNACE);
        filter = NBTUtils.setFilterLevel(filter, level);

        player.getInventory().addItem(filter);
        player.sendMessage("Выдан фильтр уровня " + level + ".");
        return true;
    }
}
