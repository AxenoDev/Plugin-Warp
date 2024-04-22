package fr.axeno.warp.config;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import fr.axeno.warp.Warp;

public class WarpConfig {
    
    private final Warp warp;
    public FileConfiguration config;
    public File file;
    public WarpConfig(Warp warp) {
        this.warp = warp;
        this.file = new File(getDataFolder(), "warp_location.yml");
    }

    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     *
     * @return The folder.
     */
    public final File getDataFolder() {
        return warp.getDataFolder();
    }

    public void loadConfig() {
        getDataFolder().mkdirs();
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        this.config = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            this.config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveWarpLocation(String warp, Location location, ItemStack block) {
        this.config.set(warp + ".x", location.getX());
        this.config.set(warp + ".y", location.getY());
        this.config.set(warp + ".z", location.getZ());
        this.config.set(warp + ".yaw", location.getYaw());
        this.config.set(warp + ".pitch", location.getPitch());
        this.config.set(warp + ".world", location.getWorld().getName());
        this.config.set(warp + ".menu.block", block);
        saveConfig();
    }

    public Location getWarpLocation(String warp) {
        double x = this.config.getDouble(warp + ".x");
        double y = this.config.getDouble(warp + ".y");
        double z = this.config.getDouble(warp + ".z");
        float yaw = (float) this.config.getDouble(warp + ".yaw");
        float pitch = (float) this.config.getDouble(warp + ".pitch");
        String worldName = this.config.getString(warp + ".world");
        ItemStack block = this.config.getItemStack(warp + ".menu.block");

        return new Location(this.warp.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    public boolean isWarpExiste(String warp) {
        return this.config.contains(warp);
    }

    public void removeWarpLocation(String warp) {
        if(isWarpExiste(warp)) {
            this.config.set(warp, null);
            saveConfig();
        }
    } 

    public Set<String> getAllWarps() {
        return this.config.getKeys(false);
    }

    public void changeWarpName(String warp, String title) {
        if(isWarpExiste(warp)) {
            this.config.set(warp, title);
            saveConfig();
        }
    }

}
