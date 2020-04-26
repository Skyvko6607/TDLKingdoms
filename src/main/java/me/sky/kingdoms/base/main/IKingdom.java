package me.sky.kingdoms.base.main;

import me.sky.kingdoms.base.data.buildings.types.KingdomBuilding;
import me.sky.kingdoms.base.data.member.MemberData;
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
    Map<UUID, MemberData> getMembers();
    KingdomPrivacy getPrivacy();
    String getThemeName();
    List<KingdomBuilding> getBuildings();
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
