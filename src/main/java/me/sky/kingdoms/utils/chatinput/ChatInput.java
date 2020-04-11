package me.sky.kingdoms.utils.chatinput;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatInput implements Listener {

    private Plugin plugin;
    private Player player;
    private String text;
    private IResponse response;
    private boolean finished = false;

    private ChatInput(Player player, String text, IResponse response, Plugin plugin) {
        this.player = player;
        this.text = text;
        this.response = response;
        this.plugin = plugin;
        player.closeInventory();
        player.sendMessage(text);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (finished) {
                    this.cancel();
                    return;
                }
                if (!player.isValid() || !player.isOnline()) {
                    this.cancel();
                    finish();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (finished) {
                    this.cancel();
                    return;
                }
                if (check) {
                    check();
                }
            }
        }.runTaskTimer(plugin, 0, 5);
    }

    private void finish() {
        finished = true;
        HandlerList.unregisterAll(this);
    }

    private void check() {
        ResponseType res = response.onResponse(player, checkText);
        if (res == ResponseType.SUCCESS) {
            check = false;
            finish();
            return;
        }
        player.sendMessage(text);
        check = false;
    }

    private boolean check = false;
    private String checkText;

    @EventHandler
    public void chatInput(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().equals(player) || finished) {
            return;
        }
        this.checkText = event.getMessage();
        check = true;
    }

    public interface IResponse {
        ResponseType onResponse(Player player, String message);
    }

    public enum ResponseType {
        SUCCESS,
        ERROR
    }

    public static class Builder {

        private Plugin plugin;
        private IResponse response;
        private String text;

        public Builder() {
        }

        public Builder onInput(IResponse response) {
            this.response = response;
            return this;
        }

        public Builder plugin(Plugin plugin) {
            this.plugin = plugin;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public ChatInput build(Player player) {
            return new ChatInput(player, text, response, plugin);
        }
    }

}
