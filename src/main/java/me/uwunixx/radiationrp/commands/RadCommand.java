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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игрок может использовать эту команду.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cИспользование: /rad <радиус> <сила>");
            return true;
        }

        double radius = Double.parseDouble(args[0]);
        int power = Integer.parseInt(args[1]);

        Location loc = player.getLocation();

        plugin.getRadiationManager().createZone(loc, radius, power);

        player.sendMessage("§aСоздана зона радиации! Радиус: " + radius + ", сила: " + power);
        return true;
    }
}
