package me.uwunixx.radiationrp.commands;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.suits.SuitLevel;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class RadGiveSuitCommand implements CommandExecutor {

    private final RadiationRP plugin;

    public RadGiveSuitCommand(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игрок может использовать эту команду.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cИспользование: /radgivesuit <уровень>");
            return true;
        }

        int lvl = Integer.parseInt(args[0]);
        SuitLevel level = SuitLevel.fromInt(lvl);

        if (level == null) {
            player.sendMessage("§cНеверный уровень.");
            return true;
        }

        player.getInventory().addItem(plugin.getSuitManager().createSuit(level));
        player.sendMessage("§aВыдан костюм уровня " + lvl);

        return true;
    }
}
