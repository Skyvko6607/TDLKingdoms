package me.sky.kingdoms.base.main;

import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.data.HouseData;
import me.sky.kingdoms.base.building.data.KingdomBuildingData;
import me.sky.kingdoms.base.building.data.ShopData;
import me.sky.kingdoms.base.data.member.MemberData;
import me.sky.kingdoms.base.main.objects.KingdomPrivacy;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import me.sky.kingdoms.utils.SerializableLocation;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IKingdom {
    String getUuid();
    String getName();
    Map<UUID, MemberData> getMembers();
    KingdomPrivacy getPrivacy();
    String getThemeName();
    Map<String, KingdomBuildingData> getBuildings();
    int getLevel();
    double getExperience();
    Location getLocation();
    double getBalance();
    SerializableLocation getHome();
    Map<String, HouseData> getHouses();
    Map<String, ShopData> getShops();
    List<ShopData> getShopDatasByOwner(Player player);
    UUID getBuildingOwner(String building);
    UUID getBuildingOwner(IKingdomBuilding building);
    SerializableVector[] getPoints();

    void setName(String name);
    void setLevel(int level);
    void setExperience(double experience);
    void addExperience(double experience);
    void removeExperience(double experience);
    void depositBalance(int balance);
    void withdrawBalance(int balance);
    void addMember(Player player, IKingdomsPlugin plugin);
    void removeMember(Player player, IKingdomsPlugin plugin);
    void setRank(Player player, KingdomRank rank);
    void setPrivacy(KingdomPrivacy privacy);
    void setHome(Location location);
    void setPoints(SerializableVector[] vectors);
    void addBuildingData(String id, KingdomBuildingData data);
    void removeBuildingData(String id);
}
