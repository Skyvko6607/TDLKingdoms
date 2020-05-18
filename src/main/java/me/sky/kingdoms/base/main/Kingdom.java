package me.sky.kingdoms.base.main;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.data.HouseData;
import me.sky.kingdoms.base.building.data.KingdomBuildingData;
import me.sky.kingdoms.base.building.data.ShopData;
import me.sky.kingdoms.base.building.types.Mine;
import me.sky.kingdoms.base.data.member.MemberData;
import me.sky.kingdoms.base.main.objects.KingdomPrivacy;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.utils.SerializableLocation;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Kingdom implements IKingdom {

    private final String uuid, theme;
    private final SerializableLocation location;
    private final Map<String, HouseData> houses = new HashMap<>();
    private final Map<String, ShopData> shops = new HashMap<>();
    private final Map<UUID, MemberData> memberDataMap = new HashMap<>();
    private String name;
    private int level = 1;
    private double experience = 0, balance = 0;
    private SerializableVector[] points;
    private KingdomPrivacy privacy;
    private SerializableLocation home = null;

    public Kingdom(Player player, String name, IKingdomTheme theme) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.theme = theme.getId();
        this.location = new SerializableLocation(player.getLocation());
        this.memberDataMap.put(player.getUniqueId(), new MemberData(player, KingdomRank.CO_RULER));
        this.privacy = KingdomPrivacy.PRIVATE;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<UUID, MemberData> getMembers() {
        return memberDataMap;
    }

    @Override
    public KingdomPrivacy getPrivacy() {
        return privacy;
    }

    @Override
    public String getThemeName() {
        return theme;
    }

    @Override
    public Map<String, KingdomBuildingData> getBuildings() {
        return new HashMap<String, KingdomBuildingData>() {{
            putAll(houses);
            putAll(shops);
        }};
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public double getExperience() {
        return experience;
    }

    @Override
    public Location getLocation() {
        return location.getLocation();
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public SerializableLocation getHome() {
        return home;
    }

    @Override
    public Map<String, HouseData> getHouses() {
        return houses;
    }

    @Override
    public Map<String, ShopData> getShops() {
        return shops;
    }

    @Override
    public UUID getBuildingOwner(String building) {
        if (getBuildings().containsKey(building) && getBuildings().get(building).isOwned()) {
            return getBuildings().get(building).getOwnedBy();
        }
        return null;
    }

    @Override
    public UUID getBuildingOwner(IKingdomBuilding building) {
        return getBuildingOwner(building.getId());
    }

    @Override
    public SerializableVector[] getPoints() {
        return points;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void setExperience(double experience) {
        this.experience = experience;
    }

    @Override
    public void addExperience(double experience) {
        this.experience += experience;
    }

    @Override
    public void removeExperience(double experience) {
        this.experience = this.experience - experience < 0 ? 0 : this.experience - experience;
    }

    @Override
    public void depositBalance(int balance) {
        this.balance += balance;
    }

    @Override
    public void withdrawBalance(int balance) {
        this.balance -= balance;
    }

    @Override
    public void addMember(Player player, IKingdomsPlugin plugin) {
        memberDataMap.put(player.getUniqueId(), new MemberData(player, KingdomRank.MEMBER));
    }

    @Override
    public void removeMember(Player player, IKingdomsPlugin plugin) {
        memberDataMap.remove(player.getUniqueId());
        if (memberDataMap.isEmpty()) {
            plugin.getKingdomManager().removeKingdom(this);
        }
    }

    @Override
    public void setRank(Player player, KingdomRank rank) {
        try {
            memberDataMap.get(player.getUniqueId()).setRank(rank);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void setPrivacy(KingdomPrivacy privacy) {
        this.privacy = privacy;
    }

    @Override
    public void setHome(Location location) {
        this.home = new SerializableLocation(location);
    }

    @Override
    public void setPoints(SerializableVector[] vectors) {
        this.points = vectors;
    }

    @Override
    public void addBuildingData(String id, KingdomBuildingData data) {
        if (data instanceof HouseData) {
            houses.put(id, (HouseData) data);
        } else if (data instanceof ShopData) {
            shops.put(id, (ShopData) data);
        }
    }

    @Override
    public void removeBuildingData(String id) {
        houses.remove(id);
    }
}
