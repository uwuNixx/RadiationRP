package me.uwunixx.radiationrp;

import me.uwunixx.radiationrp.filters.FilterManager;
import me.uwunixx.radiationrp.listeners.*;
import me.uwunixx.radiationrp.commands.*;
import me.uwunixx.radiationrp.radiation.RadiationManager;
import me.uwunixx.radiationrp.suits.SuitManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class RadiationRP extends JavaPlugin {

    private static RadiationRP instance;

    private RadiationManager radiationManager;
    private FilterManager filterManager;
    private SuitManager suitManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.radiationManager = new RadiationManager(this);
        this.filterManager = new FilterManager(this);
        this.suitManager = new SuitManager(this);

        // --- РЕГИСТРАЦИЯ КОМАНД ---
        registerCommand("rad", new RadCommand(this));
        registerCommand("radpower", new RadPowerCommand(this));
        registerCommand("radgivefilter", new RadGiveFilterCommand(this));
        registerCommand("radgivesuit", new RadGiveSuitCommand(this));
        registerCommand("radremove", new RadRemoveCommand(this));

        // --- РЕГИСТРАЦИЯ СОБЫТИЙ ---
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);

        radiationManager.loadData();

        getLogger().info("Before filter tasks");
        filterManager.startTasks();
        getLogger().info("After filter tasks");

        getLogger().info("Before radiation tasks");
        radiationManager.startTasks();
        getLogger().info("After radiation tasks");


        getLogger().info("RadiationRP enabled.");
    }

    @Override
    public void onDisable() {
        radiationManager.saveData();
        radiationManager.stopTasks();
        filterManager.stopTasks();
        getLogger().info("RadiationRP disabled.");
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand cmd = Objects.requireNonNull(getCommand(name),
                "Команда '" + name + "' не найдена в plugin.yml");

        cmd.setExecutor(executor);
    }

    public static RadiationRP getInstance() {
        return instance;
    }

    public RadiationManager getRadiationManager() {
        return radiationManager;
    }

    public FilterManager getFilterManager() {
        return filterManager;
    }

    public SuitManager getSuitManager() {
        return suitManager;
    }
}
