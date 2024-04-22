package fr.axeno.warp.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.axeno.warp.Warp;
import fr.axeno.warp.config.MenuEnum;

public class WarpCommands implements CommandExecutor, Listener {

    private Warp warp;
    public WarpCommands(Warp warp) {
        this.warp = warp;
        warp.getCommand("warp").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, warp);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande est réservé au joueur.");
            return true;
        }

        Player player = (Player) sender;

        if(args.length < 1) {
            openMenu(player, MenuEnum.Menu.LIST);
        } else if(args[0].equals("add")) {
            String title = args[1];
            if(title == null) player.sendMessage("WARP > Vous dever mettre un nom au warp.");
            warp.warpConfig.saveWarpLocation(title, player.getLocation(), new ItemStack(Material.BED));
        } else if(args[0].equals("help")) {
            player.sendMessage("§7§m------------------§8[ §6WARP HELP §8]§7§m------------------");
            player.sendMessage("    §7• §6/warp §8- §7Ouvre le menu des warps.");
            player.sendMessage("    §7• §6/warp add <name> §8- §7Crée un nouveau warp à votre position actuelle.");
            player.sendMessage("    §7• §6/warp delete <name> §8- §7Supprime un warp existant.");
            player.sendMessage("    §7• §6/warp list §8- §7Consulte la liste complète des warps disponibles dans le chat.");
            player.sendMessage("    §7• §6/warp reload §8- §7Rafraîchit le plugin.");
            player.sendMessage("§7§m------------------§8[ §6WARP HELP §8]§7§m------------------");
        }

        return true;
    }

    private void openMenu(Player player, Enum menu) {

        if(MenuEnum.Menu.LIST.equals(menu)) {
            
            Inventory warpMenu = Bukkit.createInventory(null, MenuEnum.Menu.LIST.getSlots(), MenuEnum.Menu.LIST.getTitle());

            for(String warpName : warp.warpConfig.getAllWarps()) {
                
                ConfigurationSection warps = warp.warpConfig.config.getConfigurationSection(warpName);
                
                double x = warps.getDouble("x");
                double z = warps.getDouble("y");
                double y = warps.getDouble("z");
                String world = warps.getString("world");

                List<String> lore = Arrays.asList(
                    "  §7Coordonées:",
                         "    §8- §eX: §6" + x,
                         "    §8- §eY: §6" + y,
                         "    §8- §eZ: §6" + z,
                         "    §8- §eWorld: §6" + world,
                         "§7 ",
                         "§8[§6»§8] §7Clique gauche pour se téléporter",
                         "§8[§6»§8] §7Clique droit pour modifier le warp"
                );
                ItemStack originalItem = warps.getItemStack("menu.block");
                ItemStack newItem = new ItemStack(originalItem);
                ItemMeta itemMeta = newItem.getItemMeta();
                itemMeta.setDisplayName(warpName);
                itemMeta.setLore(lore);
                newItem.setItemMeta(itemMeta);
                warpMenu.addItem(newItem);

            }

            player.openInventory(warpMenu);
        } else if(MenuEnum.Menu.WARP_INFO.equals(menu)) {
            Inventory inventory = Bukkit.createInventory(null, MenuEnum.Menu.WARP_INFO.getSlots(), "§6Warp: §7");

            player.openInventory(inventory);
        }
    }

    private void openMenu(Player player, Enum menu, String warpName) {

        if(MenuEnum.Menu.WARP_INFO.equals(menu)) {
            Inventory inventory = Bukkit.createInventory(null, MenuEnum.Menu.WARP_INFO.getSlots(), "§6Warp: §8" + warpName);

            ConfigurationSection warps = warp.warpConfig.config.getConfigurationSection(warpName);

            double x = warps.getDouble("x");
            double z = warps.getDouble("y");
            double y = warps.getDouble("z");
            String world = warps.getString("world");

            List<String> lore = Arrays.asList(
                "  §7Coordonées:",
                     "    §8- §eX: §6" + x,
                     "    §8- §eY: §6" + y,
                     "    §8- §eZ: §6" + z,
                     "    §8- §eWorld: §6" + world
            );
            ItemStack originalItem = warps.getItemStack("menu.block");
            ItemStack newItem = new ItemStack(originalItem);
            ItemMeta itemMeta = newItem.getItemMeta();
            ItemStack returnItem = new ItemStack(Material.ARROW);
            ItemMeta returnMeta = returnItem.getItemMeta();
            ItemStack changeBlock = new ItemStack(Material.STONE);
            ItemMeta changeBlockMeta = changeBlock.getItemMeta();
            ItemStack delWarp = new ItemStack(Material.BARRIER);
            ItemMeta delWarpMeta = delWarp.getItemMeta();
            
            returnMeta.setDisplayName("§7↩ Revenir à la liste des warps.");
            itemMeta.setDisplayName(warpName);
            itemMeta.setLore(lore);

            changeBlockMeta.setDisplayName("§aChanger le block du warp.");
            delWarpMeta.setDisplayName("§cSupprimer le warp.");

            newItem.setItemMeta(itemMeta);
            returnItem.setItemMeta(returnMeta);
            changeBlock.setItemMeta(changeBlockMeta);
            delWarp.setItemMeta(delWarpMeta);

            inventory.setItem(4, newItem);
            inventory.setItem(11, changeBlock);
            inventory.setItem(15, delWarp);
            inventory.setItem(26, returnItem);

            player.openInventory(inventory);
        }

    }


    @EventHandler
    public void onInventoryInteraction(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player player = (Player)event.getWhoClicked();

        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;
        if(inventory == null) return;
        if(player == null) return;

        if(inventory.getTitle().equals(MenuEnum.Menu.LIST.getTitle())) {
            event.setCancelled(true);
            if(event.getClick().isLeftClick()) {
                player.closeInventory();
                player.teleport(warp.warpConfig.getWarpLocation(event.getCurrentItem().getItemMeta().getDisplayName().toString()));
            } else if(event.getClick().isRightClick()) {
                if(event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    player.closeInventory();
                    openMenu(player, MenuEnum.Menu.WARP_INFO, event.getCurrentItem().getItemMeta().getDisplayName().toString());
                }
                
            }
        } else if(inventory.getTitle().contains("§6Warp: §8")) {

            String warpName = inventory.getTitle().replace("§6Warp: §8", "");
            event.setCancelled(true);
            if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§7↩ Revenir à la liste des warps.")) {
                openMenu(player, MenuEnum.Menu.LIST);
            }

        }

    }    
}
