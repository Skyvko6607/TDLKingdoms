package me.sky.kingdoms.base.building.manager;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.bukkit.wrapper.AsyncWorld;
import com.boydti.fawe.object.RunnableVal;
import com.boydti.fawe.util.EditSessionBuilder;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.world.FastModeExtent;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.IManager;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.data.HouseData;
import me.sky.kingdoms.base.building.schematic.SchematicData;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.base.data.member.MemberData;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.base.data.objects.IValuedBuilding;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.utils.Language;
import me.sky.kingdoms.utils.SerializableLocation;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BuildingManager implements IManager {

    private final IKingdomsPlugin plugin;
    private final List<SchematicData> schematicData = new ArrayList<>();

    public BuildingManager(IKingdomsPlugin plugin) {
        this.plugin = plugin;
        loadSchematicData();
        new BukkitRunnable() {
            @Override
            public void run() {
                saveSchematicData();
            }
        }.runTaskTimer(plugin, 100, 200);
    }

    public void loadSchematicData() {
        File dir = new File("plugins/" + getPlugin().getName() + "/schematicData");
        if (!dir.exists()) {
            dir.mkdir();
            return;
        }
        for (File file : dir.listFiles()) {
            try {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextLine()) {
                    schematicData.add(plugin.getGson().fromJson(scanner.nextLine(), SchematicData.class));
                }
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Error loading schematic data: " + file.getName());
            }
        }
    }

    public void upgradeKingdom(IKingdom kingdom, Player player) {
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName());
        if (theme == null) {
            return;
        }
        if (!theme.getMainTemplates().containsKey(kingdom.getLevel() + 1)) {
            player.sendMessage(KingdomUtils.PREFIX + "§aThere are no more upgrades left!");
            return;
        }
        IKingdomTemplate template = theme.getTemplate(kingdom.getLevel() + 1);
        if (kingdom.getBalance() < template.getUpgradePrice()) {
            player.sendMessage(KingdomUtils.PREFIX + "§aThe kingdom balance is too low to upgrade!");
            return;
        }
        kingdom.getBuildings().keySet().forEach(s -> {
            UUID uuid = kingdom.getBuildingOwner(s);
            if (uuid == null) {
                return;
            }
            IKingdomBuildingData data = kingdom.getBuildings().get(s);
            MemberData memberData = kingdom.getMembers().get(uuid);
            if (data instanceof HouseData) {
                ((HouseData) data).getStorage().values().forEach(itemStacks -> memberData.addDumpItems(itemStacks.toArray(new ItemStack[0])));
                ((HouseData) data).getJsonStorage().clear();
            }
            data.getPlacedBlocks().forEach(location -> {
                Block block = location.getLocation().getBlock();
                if (block.getType().isAir()) {
                    return;
                }
                memberData.addDumpItems(block.getDrops().toArray(new ItemStack[0]));
            });
            data.getPlacedBlocks().clear();
        });
        try {
            placeTemplate(kingdom, template);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(KingdomUtils.PREFIX + Language.get().getMessage("ErrorMessage"));
            return;
        }
        kingdom.getBuildings().forEach((s, kingdomBuildingData) -> {
            final UUID owner = kingdomBuildingData.getOwnedBy();
            kingdomBuildingData.setOwned(false);
            kingdomBuildingData.setOwnedBy((UUID) null);
            IKingdomBuilding building = getBuilding(s, template);
            if (building == null) {
                return;
            }
            try {
                placeBuilding(kingdom, building);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            kingdomBuildingData.setOwned(true);
            kingdomBuildingData.setOwnedBy(owner);
            MemberData memberData = kingdom.getMembers().get(kingdomBuildingData.getOwnedBy());
            if (kingdomBuildingData instanceof HouseData) {
                HouseData data = (HouseData) kingdomBuildingData;
                BlockVector3 weVec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ()).subtract(building.getOffset().toBlockPoint());
                SerializableVector vec = new SerializableVector(weVec.getX(), weVec.getY(), weVec.getZ());
                for (SerializableVector[] area : building.getBuildingAreas(new SerializableVector(vec.getX(), vec.getY(), vec.getZ()), kingdomBuildingData.getAngle(), plugin)) {
                    Region region = new CuboidRegion(FaweAPI.getWorld(kingdom.getLocation().getWorld().getName()),
                            BlockVector3.at(area[0].getX(), area[0].getY(), area[0].getZ()),
                            BlockVector3.at(area[1].getX(), area[1].getY(), area[1].getZ()));
                    region.forEach(blockVector3 -> {
                        Block block = kingdom.getLocation().getWorld().getBlockAt(blockVector3.getX(), blockVector3.getY(), blockVector3.getZ());
                        if (block.getType() == Material.CHEST) {
                            Chest chest = (Chest) block.getState();
                            if (chest.getInventory().getHolder() instanceof DoubleChest) {
                                chest = (Chest) ((DoubleChest) chest.getInventory().getHolder()).getLeftSide();
                            }
                            if (!data.getStorage().containsKey(chest.getLocation())) {
                                List<ItemStack> items = new ArrayList<>();
                                for (int i = 0; i < (chest.getInventory().getHolder() instanceof DoubleChest ? 54 : 27); i++) {
                                    if (i >= memberData.getDumpItems().size()) {
                                        break;
                                    }
                                    items.add(memberData.getDumpItems().get(i));
                                    memberData.getJsonDumpItems().remove(i);
                                }
                                data.setItems(new SerializableLocation(chest.getLocation()), items);
                            }
                        }
                    });
                }
                plugin.getKingdomManager().saveKingdom(kingdom);
            }
            plugin.getKingdomManager().updateSign(kingdom, building);
        });
        plugin.getKingdomManager().saveKingdom(kingdom);
    }

    public boolean claimBuilding(IKingdom kingdom, Player player, IKingdomBuildingData data) {
        IKingdomTheme theme = plugin.getThemeManager().getThemeFromId(kingdom.getThemeName());
        if (theme == null) {
            return false;
        }
        IKingdomTemplate template = theme.getTemplate(kingdom.getLevel());
        if (template == null) {
            return false;
        }
        IKingdomBuilding building = getBuilding(data.getId(), template);
        if (building == null) {
            return false;
        }
        if (building instanceof IValuedBuilding) {
            double balance = plugin.getEconomy().getBalance(player);
            if (balance < ((IValuedBuilding) building).getBuyPrice()) {
                player.sendMessage(KingdomUtils.PREFIX + Language.get().getMessage("NotEnoughMoney"));
                return false;
            }
            plugin.getEconomy().withdrawPlayer(player, ((IValuedBuilding) building).getBuyPrice());
        }
        BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
        vec = vec.subtract(building.getOffset().toBlockPoint());
        try {
            placeBuilding(kingdom, building);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(KingdomUtils.PREFIX + Language.get().getMessage("ErrorMessage"));
            if (building instanceof IValuedBuilding) {
                plugin.getEconomy().depositPlayer(player, ((IValuedBuilding) building).getBuyPrice());
            }
            return false;
        }
        data.setAngle(getAngle(building));
        new Location(kingdom.getLocation().getWorld(), vec.getX(), vec.getY() - 1, vec.getZ()).getBlock().setType(Material.GRASS_BLOCK);
        Block block = new Location(kingdom.getLocation().getWorld(), vec.getX(), vec.getY(), vec.getZ()).getBlock();
        if (block.getType().name().contains("SIGN")) {
            block.setType(Material.AIR);
        }
        data.setOwned(true);
        data.setOwnedBy(player);
        player.teleport(new Location(kingdom.getLocation().getWorld(), vec.getX() + 0.5, vec.getY() + 0.5, vec.getZ() + 0.5, building.getDirection().getAngle(), 0));
        return true;
    }

    public void placeBuilding(IKingdom kingdom, IKingdomBuilding building) throws IOException {
        BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
        vec = vec.subtract(building.getOffset().toBlockPoint());
        placeBuilding(kingdom, vec, building);
    }

    @SuppressWarnings("deprecation")
    private void placeBuilding(IKingdom kingdom, BlockVector3 loc, IKingdomBuilding building) throws IOException {
        BlockVector3 buildingOffset = building.getBuildingOffset(plugin).toBlockPoint();
        File schematic = building.getSchematicFile(plugin);
        SchematicData data = building.getSchematicData(plugin);
        Direction direction = building.getDirection();
        AffineTransform transform = new AffineTransform();
        if (direction != null && !direction.equals(data.getDirection())) {
            transform = transform.rotateY(getAngle(building));
        }
        ClipboardHolder holder = new ClipboardHolder(BuiltInClipboardFormat.MINECRAFT_STRUCTURE.load(schematic));
        holder.getClipboard().setOrigin(buildingOffset);
        EditSession extent = new EditSessionBuilder(FaweAPI.getWorld(kingdom.getLocation().getWorld().getName())).fastmode(false).build();
        holder.setTransform(transform);
        Operations.complete(holder.createPaste(extent).ignoreAirBlocks(false).to(loc).build());
        extent.flushQueue();
    }

    public int getAngle(IKingdomBuilding building) {
        SchematicData data = building.getSchematicData(plugin);
        if (building.getDirection() == null || building.getDirection().equals(data.getDirection())) {
            return 0;
        }
        Direction next = building.getDirection();
        next = Direction.getSubDirection(next, 2);
        int angle = 0;
        for (int i = 0; i < Direction.values().length; i++) {
            angle += 90;
            next = Direction.getSubDirection(next, 1);
            if (next.equals(data.getDirection())) {
                break;
            }
        }
        return -angle;
    }

    public void placeTemplate(IKingdom kingdom, IKingdomTemplate template) throws IOException {
        BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
        vec = vec.subtract(template.getCenterOffset());
        File schematic = template.getSchematicFile();
        placeTemplate(kingdom, vec, schematic);
    }

    @SuppressWarnings("deprecation")
    private void placeTemplate(IKingdom kingdom, BlockVector3 location, File schematic) throws IOException {
        AffineTransform transform = new AffineTransform();
        ClipboardHolder clipboard = new ClipboardHolder(BuiltInClipboardFormat.MINECRAFT_STRUCTURE.load(schematic));
        clipboard.setTransform(transform);
        TaskManager.IMP.sync(new RunnableVal<AsyncWorld>() {
            @Override
            public void run(AsyncWorld asyncWorld) {
                asyncWorld = AsyncWorld.wrap(kingdom.getLocation().getWorld());
                AsyncWorld finalAsyncWorld = asyncWorld;
                clipboard.getClipboard().forEach(blockVector3 -> {
                    final int x = location.getX() + blockVector3.getBlockX();
                    final int y = location.getY() + blockVector3.getBlockY();
                    final int z = location.getZ() + blockVector3.getBlockZ();
                    BlockState b = blockVector3.getBlock(new FastModeExtent(BukkitAdapter.adapt(Bukkit.getWorld(finalAsyncWorld.getName()))));
                    finalAsyncWorld.setBlock(x, y, z, b);
                });
                asyncWorld.commit();
            }
        });
        BlockVector3 loc = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
        BlockVector3 loc1 = loc.subtract(clipboard.getClipboard().getMaximumPoint().divide(2));
        BlockVector3 loc2 = loc.add(clipboard.getClipboard().getMaximumPoint().divide(2));
        kingdom.setPoints(new SerializableVector[]{
                new SerializableVector(loc1.getX(), 0, loc1.getZ()),
                new SerializableVector(loc2.getX(), 255, loc2.getZ())
        });
    }

    public IKingdomBuilding getBuilding(String id, IKingdomTemplate template) {
        for (IKingdomBuilding building : template.getBuildings()) {
            if (building.getId().equalsIgnoreCase(id)) {
                return building;
            }
        }
        return null;
    }

    public void saveSchematicData() {
        File dir = new File("plugins/" + plugin.getName() + "/schematicData");
        if (!dir.exists()) {
            dir.mkdir();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                schematicData.forEach(schematicData1 -> {
                    File file = new File("plugins/" + plugin.getName() + "/schematicData/" + schematicData1.getId() + ".json");
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter(file);
                        writer.write(plugin.getGson().toJson(schematicData1));
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }.runTaskAsynchronously(plugin);
    }

    public SchematicData getSchematicData(String id) {
        for (SchematicData data : schematicData) {
            if (data.getId().equalsIgnoreCase(id)) {
                return data;
            }
        }
        return null;
    }

    public List<SchematicData> getSchematicData() {
        return schematicData;
    }

    @Override
    public IKingdomsPlugin getPlugin() {
        return plugin;
    }
}
