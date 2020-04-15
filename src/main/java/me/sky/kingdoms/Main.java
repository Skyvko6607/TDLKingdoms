package me.sky.kingdoms;

import me.sky.kingdoms.base.main.IKingdomManager;
import me.sky.kingdoms.base.theme.IKingdomThemeManager;
import me.sky.kingdoms.base.theme.KingdomThemeManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements IKingdomsPlugin {

    private IKingdomThemeManager themeManager;

    public void onEnable() {
        this.themeManager = new KingdomThemeManager(this);
    }

    @Override
    public IKingdomManager getManager() {
        return null;
    }

    @Override
    public IKingdomThemeManager getThemeManager() {
        return themeManager;
    }
}
