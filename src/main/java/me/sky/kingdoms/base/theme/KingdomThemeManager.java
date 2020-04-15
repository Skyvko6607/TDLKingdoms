package me.sky.kingdoms.base.theme;

import com.google.gson.Gson;
import me.sky.kingdoms.IKingdomsPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KingdomThemeManager implements IKingdomThemeManager {

    private final IKingdomsPlugin plugin;
    private final List<IKingdomTheme> themes = new ArrayList<>();
    private final Gson gson = new Gson();

    public KingdomThemeManager(IKingdomsPlugin plugin) {
        this.plugin = plugin;
        initThemes();
    }

    @Override
    public void initThemes() {
        File dir = new File("plugins/" + plugin.getName() + "/themes");
        if (!dir.exists()) {
            dir.mkdir();
            return;
        }
        if (dir.listFiles().length <= 0) {
            return;
        }
        for (File file : dir.listFiles()) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNext()) {
                    IKingdomTheme kingdom = gson.fromJson(scanner.nextLine(), KingdomTheme.class);
                    themes.add(kingdom);
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                file.delete();
            }
        }
    }

    @Override
    public List<IKingdomTheme> getThemes() {
        return themes;
    }

    @Override
    public IKingdomTheme getThemeFromId(String id) {
        for (IKingdomTheme theme : themes) {
            if (theme.getId().equals(id)) {
                return theme;
            }
        }
        return null;
    }

    @Override
    public IKingdomsPlugin getPlugin() {
        return plugin;
    }
}
