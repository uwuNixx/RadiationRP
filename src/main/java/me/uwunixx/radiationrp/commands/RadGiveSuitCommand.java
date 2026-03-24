package me.uwunixx.radiationrp.commands;

import me.uwunixx.radiationrp.RadiationRP;
import me.uwunixx.radiationrp.suits.SuitLevel;
import me.uwunixx.radiationrp.suits.SuitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RadGiveSuitCommand implements CommandExecutor {

    private final SuitManager suitManager;

    public RadGiveSuitCommand(RadiationRP plugin) {
        this.suitManager = plugin.getSuitManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игрок может использовать эту команду.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Использование: /radgivesuit <level>");
            return true;
        }

        int levelInt;
        try {
            levelInt = Integer.parseInt(args[0]);
        } catch (Exception e) {
            player.sendMessage("Уровень должен быть числом.");
            return true;
        }

        SuitLevel level = SuitLevel.fromInt(levelInt);
        if (level == null) {
            player.sendMessage("Уровень должен быть от 1 до 5.");
            return true;
        }

        ItemStack suit = suitManager.createSuit(level);
        if (suit == null) {
            player.sendMessage("Костюм ещё не реализован.");
            return true;
        }

        player.getInventory().addItem(suit);
        player.sendMessage("Выдан костюм уровня " + levelInt + ".");
        return true;
    }
}
