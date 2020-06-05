package me.sky.kingdoms.base.data.member;

import me.sky.kingdoms.base.data.IMemberData;
import me.sky.kingdoms.base.building.data.KingdomBuildingData;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import me.sky.kingdoms.utils.JsonItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemberData implements IMemberData {

    private final String uuid;
    private String name;
    private KingdomRank rank;
    private final List<String> owningBuildings;
    private final List<String> dumpItems = new ArrayList<>();

    public MemberData(Player player, KingdomRank rank) {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
        this.rank = rank;
        this.owningBuildings = new ArrayList<>();
    }

    @Override
    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public KingdomRank getRank() {
        return rank;
    }

    @Override
    public List<String> getOwningBuildings() {
        return owningBuildings;
    }

    @Override
    public List<ItemStack> getDumpItems() {
        List<ItemStack> items = new ArrayList<>();
        dumpItems.forEach(s -> {
            try {
                items.add(JsonItemStack.itemFrom64(s));
            } catch (IOException | ClassNotFoundException ignored) {
            }
        });
        return items;
    }

    @Override
    public List<String> getJsonDumpItems() {
        return dumpItems;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRank(KingdomRank rank) {
        this.rank = rank;
    }

    @Override
    public void addDumpItems(ItemStack... items) {
        for (ItemStack itemStack : items) {
            try {
                dumpItems.add(JsonItemStack.itemTo64(itemStack));
            } catch (IOException ignored) {
            }
        }
    }
}
