package me.sky.kingdoms;

import com.google.gson.Gson;
import me.sky.kingdoms.base.data.buildings.BuildingManager;
import me.sky.kingdoms.base.main.IKingdomManager;
import me.sky.kingdoms.base.theme.IKingdomThemeManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;

public interface IKingdomsPlugin extends Plugin {
    IKingdomManager getManager();
    IKingdomThemeManager getThemeManager();
    BuildingManager getBuildingManager();
    Economy getEconomy();
    Gson getGson();
}
