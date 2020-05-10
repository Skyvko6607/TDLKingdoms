package me.sky.kingdoms;

import com.google.gson.Gson;
import me.sky.kingdoms.base.building.manager.BuildingManager;
import me.sky.kingdoms.base.main.IKingdomManager;
import me.sky.kingdoms.base.main.KingdomManager;
import me.sky.kingdoms.base.theme.IKingdomThemeManager;
import me.sky.kingdoms.base.theme.KingdomThemeManager;
import me.sky.kingdoms.commands.KingdomCommand;
import me.sky.kingdoms.events.BuildingEvents;
import me.sky.kingdoms.utils.Language;
import me.sky.kingdoms.utils.Options;
import me.sky.kingdoms.utils.menu.IMenuHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements IKingdomsPlugin {

    private IKingdomThemeManager themeManager;
    private BuildingManager buildingManager;
    private IKingdomManager kingdomManager;
    private Economy economy;
    private Gson gson;

    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        new IMenuHandler(this);
        new Language(this);
        new Options(this);
        this.gson = new Gson();
        this.kingdomManager = new KingdomManager(this);
        this.themeManager = new KingdomThemeManager(this);
        this.buildingManager = new BuildingManager(this);
        this.getCommand("kingdom").setExecutor(new KingdomCommand(this));
        new BuildingEvents(this);
    }

    @Override
    public IKingdomManager getKingdomManager() {
        return kingdomManager;
    }

    @Override
    public IKingdomThemeManager getThemeManager() {
        return themeManager;
    }

    @Override
    public BuildingManager getBuildingManager() {
        return buildingManager;
    }

    @Override
    public Economy getEconomy() {
        return economy;
    }

    @Override
    public Gson getGson() {
        return gson;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
