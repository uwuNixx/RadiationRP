package me.uwunixx.radiationrp.commands;

import me.uwunixx.radiationrp.RadiationRP;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RadCommand implements CommandExecutor {

    private final RadiationRP plugin;

    public RadCommand(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игрок может использовать эту команду.");
            return true;
        }

        if (args.length != 5) {
            sender.sendMessage("Использование: /rad <x> <y> <z> <radius> <power>");
            return true;
        }

        try {
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
            double radius = Double.parseDouble(args[3]);
            int power = Integer.parseInt(args[4]);

            Location center = new Location(player.getWorld(), x, y, z);
            plugin.getRadiationManager().createZone(center, radius, power);

            sender.sendMessage("Создана зона радиации.");
        } catch (Exception e) {
            sender.sendMessage("Неверные аргументы.");
        }

        return true;
    }
}
