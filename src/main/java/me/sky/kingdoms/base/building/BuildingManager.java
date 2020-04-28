package me.sky.kingdoms.base.building;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.bukkit.wrapper.AsyncWorld;
import com.boydti.fawe.object.RunnableVal;
import com.boydti.fawe.util.EditSessionBuilder;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.world.FastModeExtent;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.IManager;
import me.sky.kingdoms.base.KingdomUtils;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.base.data.objects.IValuedBuilding;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.template.IKingdomTemplate;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.utils.Language;
import me.sky.kingdoms.utils.SerializableVector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            plugin.getEconomy().depositPlayer(player, ((IValuedBuilding) building).getBuyPrice());
            return false;
        }
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
        File schematic = building.getSchematicFile(plugin);
        placeBuilding(kingdom, vec, building.getBuildingOffset(plugin).toBlockPoint(), schematic, building.getSchematicData(plugin), building.getDirection());
    }

    @SuppressWarnings("deprecation")
    private void placeBuilding(IKingdom kingdom, BlockVector3 loc, BlockVector3 buildingOffset, File schematic, SchematicData data, @Nullable Direction direction) throws IOException {
        AffineTransform transform = new AffineTransform();
        if (direction != null && !direction.equals(data.getDirection())) {
            Direction next = direction;
            next = Direction.getSubDirection(next, 2);
            float angle = 0;
            for (int i = 0; i < Direction.values().length; i++) {
                angle += 90;
                next = Direction.getSubDirection(next, 1);
                if (next.equals(data.getDirection())) {
                    break;
                }
            }
            transform = transform.rotateY(-angle);
        }
        ClipboardHolder holder = new ClipboardHolder(BuiltInClipboardFormat.MINECRAFT_STRUCTURE.load(schematic));
        Clipboard clipboard = holder.getClipboard();
        clipboard.setOrigin(buildingOffset);
        EditSession extent = new EditSessionBuilder(FaweAPI.getWorld(kingdom.getLocation().getWorld().getName())).fastmode(true).build();
        ForwardExtentCopy copy = new ForwardExtentCopy(holder.getClipboard(), clipboard.getRegion(), clipboard.getOrigin(), extent, loc);
        copy.setTransform(transform);
        Operations.complete(copy);
        extent.flushQueue();
    }

    public void placeTemplate(IKingdom kingdom, IKingdomTemplate template) throws IOException {
        BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
        vec = vec.subtract(template.getCenterOffset());
        File schematic = template.getSchematicFile();
        placeTemplate(kingdom, vec, schematic, null);
    }

    @SuppressWarnings("deprecation")
    private void placeTemplate(IKingdom kingdom, BlockVector3 location, File schematic, @Nullable Direction direction) throws IOException {
        AffineTransform transform = new AffineTransform();
        if (direction != null) {
            transform.rotateY(direction.getAngle());
        }
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
        BlockVector3 loc2 = loc.add(clipboard.getClipboard().getMaximumPoint().divide(2).add(1, 1, 1));
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
