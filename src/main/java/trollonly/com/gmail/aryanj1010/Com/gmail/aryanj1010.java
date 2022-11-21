    package trollonly.com.gmail.aryanj1010.Com.gmail;

    import org.bukkit.*;
    import org.bukkit.command.Command;
    import org.bukkit.command.CommandSender;
    import org.bukkit.command.ConsoleCommandSender;
    import org.bukkit.entity.*;
    import org.bukkit.entity.minecart.ExplosiveMinecart;
    import org.bukkit.event.EventHandler;
    import org.bukkit.event.Listener;
    import org.bukkit.event.block.Action;
    import org.bukkit.event.enchantment.EnchantItemEvent;
    import org.bukkit.event.entity.*;
    import org.bukkit.event.player.PlayerDropItemEvent;
    import org.bukkit.event.player.PlayerInteractEvent;
    import org.bukkit.event.player.PlayerJoinEvent;
    import org.bukkit.inventory.ItemStack;
    import org.bukkit.inventory.RecipeChoice;
    import org.bukkit.inventory.ShapedRecipe;
    import org.bukkit.inventory.meta.ItemMeta;
    import org.bukkit.inventory.meta.SkullMeta;
    import org.bukkit.plugin.java.JavaPlugin;

    import java.time.ZoneId;
    import java.util.*;

    public final class aryanj1010 extends JavaPlugin implements Listener {
        ItemStack heart = new ItemStack(Material.RED_DYE);
        ItemMeta hMeta = heart.getItemMeta();

        ItemStack revive = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta rMeta = revive.getItemMeta();

        ItemStack Srevive = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta srMeta = Srevive.getItemMeta();
        @Override
        public void onEnable() {


            // Makes Heart Meta
            ArrayList<String> hLore = new ArrayList<>();
            hLore.add("Right Click to");
            hLore.add("Gain a Heart");
            hMeta.setDisplayName("Heart");
            hMeta.setLore(hLore);
            heart.setItemMeta(hMeta);



            //Makes Revive Meta
            ArrayList<String> rLore = new ArrayList<>();
            rLore.add("Right Click to");
            rLore.add("Revive a player");
            rMeta.setDisplayName("Reviver");
            rMeta.setLore(rLore);
            revive.setItemMeta(rMeta);

            //Makes SuperRevive Meta
            ArrayList<String> srLore = new ArrayList<>();
            srLore.add("Right Click to");
            srLore.add("Revive a player");
            srMeta.setDisplayName("Super Reviver");
            srMeta.setLore(srLore);
            Srevive.setItemMeta(srMeta);

            // Registers Events
            getServer().getPluginManager().registerEvents(this, this);


            // Adds Revive Recipe
            ShapedRecipe reviverecipe = new ShapedRecipe(new NamespacedKey(this, "reviverecipe"), revive);
            reviverecipe.shape("dtd","heh","tdt");
            reviverecipe.setIngredient('e', Material.ELYTRA);
            reviverecipe.setIngredient('t', Material.TOTEM_OF_UNDYING);
            reviverecipe.setIngredient('d', Material.DIAMOND_BLOCK);
            reviverecipe.setIngredient('h', Material.PLAYER_HEAD);
            Bukkit.addRecipe(reviverecipe);

            // Adds SuperRevive Recipe
            ShapedRecipe Sreviverecipe = new ShapedRecipe(new NamespacedKey(this, "Sreviverecipe"), Srevive);
            Sreviverecipe.shape("nhn","heh","ghg");
            Sreviverecipe.setIngredient('n', Material.NETHERITE_INGOT);
            Sreviverecipe.setIngredient('h', new RecipeChoice.ExactChoice(heart));
            Sreviverecipe.setIngredient('e', Material.ELYTRA);
            Sreviverecipe.setIngredient('g', Material.ENCHANTED_GOLDEN_APPLE);
            Bukkit.addRecipe(Sreviverecipe);
        }

        @SuppressWarnings("deprecation")
        public ItemStack getPlayerHead(String player) {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD,1);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(player);
            item.setItemMeta(meta);
            return item;
        }

        @EventHandler
        @SuppressWarnings("deprecation")
        public void OnPlayerDie (PlayerDeathEvent e) {
            Player p = e.getEntity();

            Entity DroppedHeart = p.getWorld().dropItem(p.getEyeLocation(), heart);
            DroppedHeart.setInvulnerable(true);

            if (Math.random() < 0.25) {
                Entity DroppedHead = p.getWorld().dropItem(p.getEyeLocation(), getPlayerHead(p.getName()));
                DroppedHead.setInvulnerable(true);
            }

            if (p.getMaxHealth() <= 2) {
                p.getServer().getBanList(BanList.Type.NAME).addBan(p.getName(), "You Ran Out of Hearts :(", null, null);
                Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "kick " + p.getName());
            } else if (p.getMaxHealth() > 3) {

                p.setMaxHealth(p.getMaxHealth() - 2);
            }
        }

        @EventHandler
        @SuppressWarnings("deprecation")
        public void OnPlayerRightClickAir (PlayerInteractEvent e) {
            Action a = e.getAction();
            ItemStack i = e.getItem();
            Player p = e.getPlayer();
            if (a.equals(Action.RIGHT_CLICK_AIR) && i.getItemMeta().equals(hMeta) && i != null) {
                if (!(p.getMaxHealth() >= 40)) {
                    p.setMaxHealth(p.getMaxHealth() + 2);
                    i.setAmount(i.getAmount() - 1);
                }
            }

            if (a.equals(Action.RIGHT_CLICK_AIR) && i.equals(revive) && i.getItemMeta().hasDisplayName()) {
                Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "pardon " + i.getItemMeta().getDisplayName());
                Player revivedPlayer = Bukkit.getPlayer(i.getItemMeta().getDisplayName());
                revivedPlayer.setMaxHealth(2);
                i.setAmount(i.getAmount() - 1);
            }
            if (a.equals(Action.RIGHT_CLICK_BLOCK) && i.equals(revive) && i != null) {
                e.setCancelled(true);
            }

            if (a.equals(Action.RIGHT_CLICK_AIR) && i.equals(Srevive) && i.getItemMeta().hasDisplayName()) {
                Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "pardon " + i.getItemMeta().getDisplayName());
                Player revivedPlayer = Bukkit.getPlayer(i.getItemMeta().getDisplayName());
                revivedPlayer.setMaxHealth(8);
                i.setAmount(i.getAmount() - 1);
            }
            if (a.equals(Action.RIGHT_CLICK_BLOCK) && i.equals(Srevive) && i != null) {
                e.setCancelled(true);
            }
        }

        @Override
        @SuppressWarnings("deprecation")
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if(command.getName().equalsIgnoreCase("withdraw")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.getMaxHealth() > 2) {
                        p.setMaxHealth(p.getMaxHealth() - 2);
                        p.getInventory().addItem(heart);
                    } else if (p.getMaxHealth() <= 2) {
                        p.sendMessage("You cannot withdraw your last heart, Idiot");
                    }
                }
            }
            if(command.getName().equalsIgnoreCase("giveheart") && sender instanceof ConsoleCommandSender) {
                Bukkit.getServer().getPlayer(args[0]).getInventory().addItem(heart);
            }
            if (command.getName().equalsIgnoreCase("resethealth")  && sender instanceof ConsoleCommandSender) {
                for (Player player: getServer().getOnlinePlayers()) {
                    Player p = player;
                    p.setMaxHealth(20);
                }
            }
            return true;
        }


        int droppedItemID;
        @EventHandler
        public void itemdrop(PlayerDropItemEvent e) {
            ItemStack droppedItem = e.getItemDrop().getItemStack();
            if (droppedItem.getItemMeta().equals(hMeta)) {
                droppedItemID = e.getItemDrop().getEntityId();
            }
            if (droppedItem.getType().equals(Material.PLAYER_HEAD)) {
                droppedItemID = e.getItemDrop().getEntityId();
            }
        }
        @EventHandler
        public void onEntityDamaged(EntityDamageEvent e) {
            if (e.getEntity().getEntityId() == droppedItemID) {
                e.setCancelled(true);
            }


        }
        @EventHandler
        public void onEntityDamagedbyEntity(EntityDamageByEntityEvent e) {
            if (e.getEntity().getEntityId() == droppedItemID) e.setCancelled(true);


            if (e.getEntity() instanceof ExplosiveMinecart) e.setDamage(e.getDamage() * 3.5);
            if (e.getEntity() instanceof TNTPrimed) e.setDamage(e.getDamage() * 3.5);
            if (e.getEntity() instanceof Firework) e.setDamage(e.getDamage() * 3.5);
        }
        @EventHandler
        public void onTotemPop(EntityResurrectEvent e) {
            e.setCancelled(true);
        }

        @EventHandler
        public void onEnchant (EnchantItemEvent e) {
            if (e.getItem().equals(Material.NETHERITE_HELMET) || e.getItem().equals(Material.NETHERITE_CHESTPLATE) || e.getItem().equals(Material.NETHERITE_LEGGINGS) || e.getItem().equals(Material.NETHERITE_BOOTS))
                e.setCancelled(true);
        }
    }
