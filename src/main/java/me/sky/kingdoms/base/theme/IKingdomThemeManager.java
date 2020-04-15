package me.sky.kingdoms.base.theme;

import me.sky.kingdoms.base.IManager;

import java.util.List;

public interface IKingdomThemeManager extends IManager {
    void initThemes();
    List<IKingdomTheme> getThemes();
    IKingdomTheme getThemeFromId(String id);
}
