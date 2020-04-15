package me.sky.kingdoms;

import me.sky.kingdoms.base.main.IKingdomManager;
import me.sky.kingdoms.base.theme.IKingdomThemeManager;
import org.bukkit.plugin.Plugin;

public interface IKingdomsPlugin extends Plugin {
    IKingdomManager getManager();
    IKingdomThemeManager getThemeManager();
}
