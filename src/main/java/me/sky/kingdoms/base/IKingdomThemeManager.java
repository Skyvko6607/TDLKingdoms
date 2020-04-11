package me.sky.kingdoms.base;

import java.util.List;

public interface IKingdomThemeManager {
    List<IKingdomTheme> getThemes();
    IKingdomTheme getThemeFromId(String id);
}
