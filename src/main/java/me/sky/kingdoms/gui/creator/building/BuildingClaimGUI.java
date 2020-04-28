package me.sky.kingdoms.gui.creator.building;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.data.objects.IValuedBuilding;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.utils.ItemUtils;
import me.sky.kingdoms.utils.menu.IMenu;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class BuildingClaimGUI implements IMenu {

    private final IKingdomsPlugin plugin;
    private final IKingdom kingdom;
    private final IKingdomBuilding building;
    private final Inventory inv;

    public BuildingClaimGUI(Player player, IKingdom kingdom, IKingdomBuilding building, IKingdomsPlugin plugin) {
        this.plugin = plugin;
        this.kingdom = kingdom;
        this.building = building;
        this.inv = Bukkit.createInventory(this, 27, "§lClaim Building");
        this.inv.setItem(11, ItemUtils.constructItem(Material.GREEN_WOOL, "§a§lConfirm", new ArrayList<>()));
        this.inv.setItem(13, ItemUtils.constructItem(Material.NETHER_STAR, "§d§lBuilding Information",
                Arrays.asList(
                        "§7Name: §a" + building.getName(),
                        "§7Type: §a" + StringUtils.capitalize(building.getType().name().toLowerCase()),
                        "§7Price: §a$" + (building instanceof IValuedBuilding ? ((IValuedBuilding) building).getBuyPrice() : 0),
                        "§8§m-----------------",
                        "§7Click to claim building.",
                        "§8§m-----------------"
                        )));
        this.inv.setItem(15, ItemUtils.constructItem(Material.RED_WOOL, "§4§lCancel", new ArrayList<>()));
        player.openInventory(this.inv);
    }

    @Override
    public void OnGUI(Player player, int slot, ItemStack item, ClickType clickType) {
        if (slot == 11) {
            boolean success = plugin.getBuildingManager().claimBuilding(kingdom, player, kingdom.getBuildings().get(building.getId()));
            if (!success) {
                player.sendMessage(KingdomUtils.PREFIX + "§cFailed to claim building!");
                return;
            }
            player.sendMessage(KingdomUtils.PREFIX + "§aSuccessfully claimed building!");
        } else if (slot == 15) {
            player.closeInventory();
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
