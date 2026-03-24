package me.uwunixx.radiationrp.commands;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RadRemoveCommand implements CommandExecutor {

    private final RadiationRP plugin;

    public RadRemoveCommand(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage("§cИспользование: /radremove <id|all>");
            return true;
        }

        if (args[0].equalsIgnoreCase("all")) {
            plugin.getRadiationManager().clearAllZones();
            sender.sendMessage("§aВсе зоны радиации удалены.");
            return true;
        }

        int id = Integer.parseInt(args[0]);

        plugin.getRadiationManager().removeZone(id);
        sender.sendMessage("§aЗона " + id + " удалена.");

        return true;
    }
}
