package me.sky.kingdoms.base.data.buildings;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.bukkit.wrapper.AsyncWorld;
import com.boydti.fawe.object.RunnableVal;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.world.FastModeExtent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.sky.kingdoms.IKingdomsPlugin;
import me.sky.kingdoms.base.IManager;
import me.sky.kingdoms.base.building.IKingdomBuilding;
import me.sky.kingdoms.base.building.IKingdomTemplate;
import me.sky.kingdoms.base.data.objects.Direction;
import me.sky.kingdoms.base.data.IKingdomBuildingData;
import me.sky.kingdoms.base.data.objects.IValuedBuilding;
import me.sky.kingdoms.base.main.IKingdom;
import me.sky.kingdoms.base.theme.IKingdomTheme;
import me.sky.kingdoms.utils.Language;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class BuildingManager implements IManager {

    private final IKingdomsPlugin plugin;

    public BuildingManager(IKingdomsPlugin plugin) {
        this.plugin = plugin;
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
                player.sendMessage(Language.get().getMessage("NotEnoughMoney"));
                return false;
            }
            plugin.getEconomy().withdrawPlayer(player, ((IValuedBuilding) building).getBuyPrice());
        }
        BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
        vec.add(building.getOffset().toBlockPoint());
        EditSession session;
        try {
            placeBuilding(kingdom, building);
        } catch (IOException e) {
            player.sendMessage(Language.get().getMessage("ErrorMessage"));
            return false;
        }
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
        vec.add(building.getOffset().toBlockPoint());
        File schematic = building.getSchematicFile();
        placeSchematic(kingdom, vec, schematic, building.getDirection());
    }

    public void placeTemplate(IKingdom kingdom, IKingdomTemplate template) throws IOException {
        BlockVector3 vec = BlockVector3.at(kingdom.getLocation().getX(), kingdom.getLocation().getY(), kingdom.getLocation().getZ());
        vec.add(template.getCenterOffset());
        Bukkit.broadcastMessage(vec.toString());
        Bukkit.broadcastMessage(template.getCenterOffset().toString());
        File schematic = template.getSchematicFile();
        placeSchematic(kingdom, vec, schematic, null);
    }

    @SuppressWarnings("deprecation")
    private void placeSchematic(IKingdom kingdom, BlockVector3 location, File schematic, @Nullable Direction direction) throws IOException {
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
    }

    public IKingdomBuilding getBuilding(String id, IKingdomTemplate template) {
        for (IKingdomBuilding building : template.getBuildings()) {
            if (building.getId().equalsIgnoreCase(id)) {
                return building;
            }
        }
        return null;
    }

    @Override
    public IKingdomsPlugin getPlugin() {
        return plugin;
    }
}
