package me.sky.kingdoms.base.main;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.data.IMemberData;
import me.sky.kingdoms.base.main.objects.KingdomPrivacy;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import me.sky.kingdoms.utils.SerializableLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IKingdom {
    String getUuid();
    String getName();
    Map<UUID, IMemberData> getMembers();
    KingdomPrivacy getPrivacy();
    String getThemeName();
    List<IKingdomBuilding> getBuildings();
    int getLevel();
    double getExperience();
    Location getLocation();
    double getBalance();
    SerializableLocation getHome();

    void setName(String name);
    void setLevel(int level);
    void setExperience(double experience);
    void addExperience(double experience);
    void removeExperience(double experience);
    void depositBalance(int balance);
    void withdrawBalance(int balance);
    void addMember(Player player);
    void removeMember(Player player);
    void setRank(Player player, KingdomRank rank);
    void setPrivacy(KingdomPrivacy privacy);
    void setHome(Location location);
}
