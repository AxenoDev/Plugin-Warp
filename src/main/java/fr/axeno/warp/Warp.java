package fr.axeno.warp;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import fr.axeno.warp.commands.WarpCommands;
import fr.axeno.warp.config.WarpConfig;

/*
 * warp java plugin
 * By Axeno_Off
 */
public class Warp extends JavaPlugin {

    public WarpConfig warpConfig = new WarpConfig(this);
    public static Warp instance;
    private static final Logger LOGGER=Logger.getLogger("warp");

    public void onEnable() {
        new WarpCommands(this);
        warpConfig.loadConfig();

        LOGGER.info("#######################");
        LOGGER.info("warp enabled");  
        LOGGER.info(
                    "\n __          __     _____  _____             ______     __" + 
                    "\n \\ \\        / /\\   |  __ \\|  __ \\           |  _ \\ \\   / /" +
                    "\n  \\ \\  /\\  / /  \\  | |__) | |__) |  ______  | |_) \\ \\_/ /  " +
                    "\n   \\ \\/  \\/ / /\\ \\ |  _  /|  ___/  |______| |  _ < \\   /   " +
                    "\n    \\  /\\  / ____ \\| | \\ \\| |               | |_) | | |     " +
                    "\n     \\/  \\/_/   _\\_\\_|__\\_\\_|_  ____        |____/  |_|    " +
                    "\n     /\\    \\ \\ / /  ____| \\ | |/ __ \\                       " +
                    "\n    /  \\    \\ V /| |__  |  \\| | |  | |                        " +
                    "\n   / /\\ \\    > < |  __| | . ` | |  | |                         " +
                    "\n  / ____ \\  / . \\| |____| |\\  | |__| |                        " + 
                    "\n /_/    \\_\\/_/ \\_\\______|_| \\_|\\____/                      ");    
                                            
                                                                
                                            
    }

    public void onDisable() {
        LOGGER.info("warp disabled");
        warpConfig.saveConfig();
    }

    public static Warp getInstance() {
        return instance;
    }

}
