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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Использование: /radpower <power>");
            return true;
        }

        int power;
        try {
            power = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Неверное значение.");
            return true;
        }

        for (RadiationZone zone : plugin.getRadiationManager().getZones()) {
            zone.setPower(power);
        }

        sender.sendMessage("Сила радиации обновлена.");
        return true;
    }
}
