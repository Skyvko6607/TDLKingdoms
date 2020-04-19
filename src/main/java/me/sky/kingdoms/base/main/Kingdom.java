package me.sky.kingdoms.base.main;

import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.data.IMemberData;
import me.sky.kingdoms.base.data.member.MemberData;
import me.sky.kingdoms.base.main.objects.KingdomPrivacy;
import me.sky.kingdoms.base.main.objects.KingdomRank;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.utils.SerializableLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Kingdom implements IKingdom {

    private final String uuid, theme;
    private final SerializableLocation location;
    private final List<IKingdomBuilding> buildings = new ArrayList<>();
    private final Map<UUID, IMemberData> memberDataMap = new HashMap<>();
    private String name;
    private int level = 1;
    private double experience = 0, balance = 0;
    private KingdomPrivacy privacy;
    private SerializableLocation home = null;

    public Kingdom(Player player, String name, IKingdomTheme theme) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.theme = theme.getId();
        this.location = new SerializableLocation(player.getLocation());
        this.memberDataMap.put(player.getUniqueId(), new MemberData(player, KingdomRank.CREATOR));
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
    public Map<UUID, IMemberData> getMembers() {
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
    public List<IKingdomBuilding> getBuildings() {
        return buildings;
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
    public void addMember(Player player) {
        memberDataMap.put(player.getUniqueId(), new MemberData(player, KingdomRank.MEMBER));
    }

    @Override
    public void removeMember(Player player) {
        memberDataMap.remove(player.getUniqueId());
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
}
