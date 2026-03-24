package me.uwunixx.radiationrp.commands;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.radiation.RadiationZone;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RadPowerCommand implements CommandExecutor {

    private final RadiationRP plugin;

    public RadPowerCommand(RadiationRP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("§cИспользование: /radpower <id зоны> <новая сила>");
            return true;
        }

        int id = Integer.parseInt(args[0]);
        int power = Integer.parseInt(args[1]);

        RadiationZone zone = plugin.getRadiationManager().getZone(id);

        if (zone == null) {
            sender.sendMessage("§cЗона не найдена.");
            return true;
        }

        zone.setPower(power);
        sender.sendMessage("§aСила зоны изменена на " + power);

        return true;
    }
}
