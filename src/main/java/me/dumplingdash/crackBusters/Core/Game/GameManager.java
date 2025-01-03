package me.dumplingdash.crackBusters.Core.Game;

import me.dumplingdash.crackBusters.Config.CBConfig;
import me.dumplingdash.crackBusters.Config.ConfigPaths;
import me.dumplingdash.crackBusters.Core.Keys;
import me.dumplingdash.crackBusters.Item.Items.*;
import me.dumplingdash.crackBusters.Utility.*;
import me.dumplingdash.crackBusters.CrackBusters;
import me.dumplingdash.crackBusters.Enums.GameState;
import me.dumplingdash.crackBusters.Enums.Team;
import me.dumplingdash.crackBusters.Item.CBItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.*;
import java.util.List;

public class GameManager implements Listener {
    private static Random random;
    private static Location lobbySpawn;
    private static Location gameSpawn;
    private static GameState gameState;
    private static final HashMap<UUID, CBPlayer> players = new HashMap<>();
    private static final HashMap<Material, HiddenBlock> hiddenBlocks = new HashMap<>();
    private static final HashMap<Zone, Tuple<Location, Location>> zones = new HashMap<>();
    private static final HashMap<CBPlayer, Long> zoneCooldown = new HashMap<>();
    private static final ArrayList<CBPlayer> spectators = new ArrayList<>() {
        @Override
        public boolean add(CBPlayer player) {
            player.setTeam(Team.SPECTATOR);
            return super.add(player);
        }
    };
    private static final ArrayList<CBPlayer> hunters = new ArrayList<>() {
        @Override
        public boolean add(CBPlayer player) {
            player.setTeam(Team.HUNTER);
            return super.add(player);
        }
    };
    private static final ArrayList<CBPlayer> crackBusters = new ArrayList<>() {
        @Override
        public boolean add(CBPlayer player) {
            player.setTeam(Team.CRACK_BUSTER);
            return super.add(player);
        }
    };
    private static int blockPlacedSpeedAmplifier; // amplifier for speed given to player when hidden block is found
    private static int blockPlacedSpeedDuration;
    private static int hunterTeleportDelay; // time before hunters are teleported to the game spawn in seconds
    private static int respawnInvincibilityTime; // time in seconds before a player can be killed upon respawning
    private static long zoneCooldownTime;
    private static BukkitTask hunterTeleportTask;
    private static BukkitTask hunterTeleportMessageTask;
    private static List<ItemStack> crackBusterItems;
    private static List<ItemStack> hunterItems;
    private static List<Entity> rebootCards;

    public static void enable() {
        random = new Random();
        lobbySpawn = CBConfig.loadLocation(ConfigPaths.lobbySpawn);
        gameSpawn = CBConfig.loadLocation(ConfigPaths.gameSpawn);
        gameState = GameState.LOBBY;
        hiddenBlocks.put(Material.GOLD_BLOCK, new HiddenBlock(CBConfig.loadLocation(ConfigPaths.goldPedestal), ChatColor.GOLD, "Gold"));
        hiddenBlocks.put(Material.EMERALD_BLOCK, new HiddenBlock(CBConfig.loadLocation(ConfigPaths.emeraldPedestal), ChatColor.GREEN, "Emerald"));
        hiddenBlocks.put(Material.DIAMOND_BLOCK, new HiddenBlock(CBConfig.loadLocation(ConfigPaths.diamondPedestal), ChatColor.AQUA, "Diamond"));
        hiddenBlocks.put(Material.NETHERITE_BLOCK, new HiddenBlock(CBConfig.loadLocation(ConfigPaths.netheritePedestal), ChatColor.DARK_GRAY, "Netherite"));
        zones.put(Zone.GOLD, new Tuple<>(CBConfig.loadLocation(ConfigPaths.goldZone1), CBConfig.loadLocation(ConfigPaths.goldZone2)));
        zones.put(Zone.EMERALD, new Tuple<>(CBConfig.loadLocation(ConfigPaths.emeraldZone1), CBConfig.loadLocation(ConfigPaths.emeraldZone2)));
        zones.put(Zone.DIAMOND, new Tuple<>(CBConfig.loadLocation(ConfigPaths.diamondZone1), CBConfig.loadLocation(ConfigPaths.diamondZone2)));
        zones.put(Zone.NETHERITE, new Tuple<>(CBConfig.loadLocation(ConfigPaths.netheriteZone1), CBConfig.loadLocation(ConfigPaths.netheriteZone2)));
        blockPlacedSpeedAmplifier = CBConfig.loadInteger(ConfigPaths.blockPlacedSpeedAmplifier);
        blockPlacedSpeedDuration = CBConfig.loadInteger(ConfigPaths.blockPlacedSpeedDuration);
        zoneCooldownTime = CBConfig.loadLong(ConfigPaths.zoneCooldownTimePath);
        hunterTeleportDelay = CBConfig.loadInteger(ConfigPaths.hunterTeleportDelay);
        respawnInvincibilityTime = CBConfig.loadInteger(ConfigPaths.respawnInvincibilityTime);
        crackBusterItems = new ArrayList<>();
        crackBusterItems.add((new Pickaxe()).getItem());
        crackBusterItems.add((new Sniffer()).getItem());
        crackBusterItems.add((new Crack()).getItem(2));
        hunterItems = new ArrayList<>();
        hunterItems.add((new Sword()).getItem());
        hunterItems.add((new Radar()).getItem());
        rebootCards = new ArrayList<>();
    }

    public static void disable() {
        CBConfig.saveData(ConfigPaths.lobbySpawn, lobbySpawn);
        CBConfig.saveData(ConfigPaths.gameSpawn, gameSpawn);
        CBConfig.saveData(ConfigPaths.goldPedestal, hiddenBlocks.get(Material.GOLD_BLOCK).getPedestalLocation());
        CBConfig.saveData(ConfigPaths.emeraldPedestal, hiddenBlocks.get(Material.EMERALD_BLOCK).getPedestalLocation());
        CBConfig.saveData(ConfigPaths.diamondPedestal, hiddenBlocks.get(Material.DIAMOND_BLOCK).getPedestalLocation());
        CBConfig.saveData(ConfigPaths.netheritePedestal, hiddenBlocks.get(Material.NETHERITE_BLOCK).getPedestalLocation());
        CBConfig.saveData(ConfigPaths.goldZone1, zones.get(Zone.GOLD).getFirst());
        CBConfig.saveData(ConfigPaths.goldZone2, zones.get(Zone.GOLD).getSecond());
        CBConfig.saveData(ConfigPaths.emeraldZone1, zones.get(Zone.EMERALD).getFirst());
        CBConfig.saveData(ConfigPaths.emeraldZone2, zones.get(Zone.EMERALD).getSecond());
        CBConfig.saveData(ConfigPaths.diamondZone1, zones.get(Zone.DIAMOND).getFirst());
        CBConfig.saveData(ConfigPaths.diamondZone2, zones.get(Zone.DIAMOND).getSecond());
        CBConfig.saveData(ConfigPaths.netheriteZone1, zones.get(Zone.NETHERITE).getFirst());
        CBConfig.saveData(ConfigPaths.netheriteZone2, zones.get(Zone.NETHERITE).getSecond());
        CBConfig.saveData(ConfigPaths.blockPlacedSpeedAmplifier, blockPlacedSpeedAmplifier);
        CBConfig.saveData(ConfigPaths.blockPlacedSpeedDuration, blockPlacedSpeedDuration);
        CBConfig.saveData(ConfigPaths.zoneCooldownTimePath, zoneCooldownTime);
        CBConfig.saveData(ConfigPaths.hunterTeleportDelay, hunterTeleportDelay);
        CBConfig.saveData(ConfigPaths.respawnInvincibilityTime, respawnInvincibilityTime);

        endGame(lobbySpawn.getWorld(), null);
    }

    public static void tryStartGame() {
        if(gameState != GameState.LOBBY) {
            CrackBusters.logMessage("Could not start game because game has already started");
            return;
        }
        if(hunters.isEmpty() || crackBusters.isEmpty()) {
            CrackBusters.logMessage("Could not start game because one or both teams don't have at least 1 player");
            //return;
        }
        if(lobbySpawn == null) {
            CrackBusters.logMessage("Could not start game because no lobby spawn has been set");
            return;
        }
        if(gameSpawn == null) {
            CrackBusters.logMessage("Could not start game because no game spawn has been set");
            return;
        }
        for(Material material : hiddenBlocks.keySet()) {
            if(hiddenBlocks.get(material).getPedestalLocation() == null) {
                CrackBusters.logMessage("Could not start game because " + material.name() + " has no pedestal location");
                return;
            }
        }

        startPlacing();
    }

    private static void startPlacing() {
        gameState = GameState.PLACING;

        teleportTeam(Team.HUNTER, gameSpawn);
        teleportTeam(Team.CRACK_BUSTER, lobbySpawn);

        setTeamGamemode(Team.HUNTER, GameMode.CREATIVE);
        setTeamGamemode(Team.CRACK_BUSTER, GameMode.SURVIVAL);

        clearTeamInventories(Team.HUNTER);
        clearTeamInventories(Team.CRACK_BUSTER);

        clearTeamPotionEffects(Team.HUNTER);
        clearTeamPotionEffects(Team.CRACK_BUSTER);

        // give blocks to hunters
        List<Material> blocks = new ArrayList<>(hiddenBlocks.keySet());
        while(!blocks.isEmpty()) {
            for(CBPlayer hunter : hunters) {
                hunter.getPlayer().getInventory().addItem(new ItemStack(blocks.get(0)));
                blocks.remove(0);
            }
        }

        updateAllPlayerScoreboards();
    }

    private static void startBreaking() {
        gameState = GameState.BREAKING;

        teleportTeam(Team.HUNTER, lobbySpawn);
        teleportTeam(Team.CRACK_BUSTER, gameSpawn);
        teleportTeam(Team.SPECTATOR, gameSpawn);

        setTeamGamemode(Team.HUNTER, GameMode.SURVIVAL);
        setTeamGamemode(Team.CRACK_BUSTER, GameMode.SURVIVAL);

        clearTeamInventories(Team.HUNTER);
        clearTeamInventories(Team.CRACK_BUSTER);

        clearTeamPotionEffects(Team.HUNTER);
        clearTeamPotionEffects(Team.CRACK_BUSTER);

        giveTeamPotionEffect(Team.HUNTER, PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, Integer.MAX_VALUE);
        giveTeamPotionEffect(Team.HUNTER, PotionEffectType.GLOWING, Integer.MAX_VALUE, 1);
        giveTeamPotionEffect(Team.CRACK_BUSTER, PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, Integer.MAX_VALUE);

        // give players their items


        giveTeamItems(hunterItems, Team.HUNTER);
        giveTeamItems(crackBusterItems, Team.CRACK_BUSTER);

        Crack.handleGameStart();

        updateAllPlayerScoreboards();

        // start hunter teleport action bar message task
        hunterTeleportMessageTask = new BukkitRunnable() {
            int time = hunterTeleportDelay;
            @Override
            public void run() {
                CommonUtil.sendActionBarMessageToAll(ChatColor.RED + "Hunter Teleport in " + time + " seconds");
                --time;
            }
        }.runTaskTimer(CrackBusters.instance, 0L, 20L);

        // start hunter teleport task
        hunterTeleportTask = new BukkitRunnable() {
            @Override
            public void run() {
                teleportTeam(Team.HUNTER, gameSpawn);
                Bukkit.broadcastMessage(ChatColor.RED + "Hunters Released");
                SoundUtil.playGlobalSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, .5f);
                hunterTeleportMessageTask.cancel();
            }
        }.runTaskLater(CrackBusters.instance, 20L * hunterTeleportDelay);
    }

    public static void updateAllPlayerScoreboards() {
        for(CBPlayer player : players.values()) {
            player.updateScoreboard();
        }
    }

    public static void endGame(World world, Team winningTeam) {
        gameState = GameState.LOBBY;

        // make hidden block locations and pedestals air
        for(HiddenBlock hiddenBlock : hiddenBlocks.values()) {
            if(hiddenBlock.getLocation() != null) {
                world.getBlockAt(hiddenBlock.getLocation()).setType(Material.AIR);
            }
            if(hiddenBlock.getPedestalLocation() != null) {
                world.getBlockAt(hiddenBlock.getPedestalLocation()).setType(Material.AIR);
            }
            hiddenBlock.setPlaced(false);
            hiddenBlock.setFound(false);
            hiddenBlock.setLocation(null);
        }

        if(winningTeam != null) {
            CommonUtil.sendTitleToAll(winningTeam.getColoredName(), "Win!", 10, 60, 10);
        }

        clearTeamInventories(Team.HUNTER);
        clearTeamInventories(Team.CRACK_BUSTER);

        clearTeamPotionEffects(Team.HUNTER);
        clearTeamPotionEffects(Team.CRACK_BUSTER);

        teleportTeam(Team.HUNTER, lobbySpawn);
        teleportTeam(Team.CRACK_BUSTER, lobbySpawn);

        setTeamGamemode(Team.HUNTER, GameMode.SURVIVAL);
        setTeamGamemode(Team.CRACK_BUSTER, GameMode.SURVIVAL);
        setTeamGamemode(Team.SPECTATOR, GameMode.SURVIVAL);

        if(hunterTeleportTask != null) hunterTeleportTask.cancel();
        if(hunterTeleportMessageTask != null) hunterTeleportMessageTask.cancel();

        for(CBPlayer player : players.values()) {
            player.handleEndGame();
        }

        setAllPlayersAlive();

        updateAllPlayerScoreboards();

        for (Entity rebootCard : rebootCards) {
            rebootCard.remove();
        }
        rebootCards.clear();
    }

    private static void teleportTeam(Team team, Location location) {
        if(team == Team.CRACK_BUSTER) {
            for(CBPlayer player : crackBusters) {
                player.getPlayer().teleport(location);
            }
        } else if(team == Team.HUNTER) {
            for(CBPlayer player : hunters) {
                player.getPlayer().teleport(location);
            }
        }
    }

    private static void setTeamGamemode(Team team, GameMode gameMode) {
        if(team == Team.CRACK_BUSTER) {
            for(CBPlayer player : crackBusters) {
                player.getPlayer().setGameMode(gameMode);
            }
        } else if(team == Team.HUNTER) {
            for(CBPlayer player : hunters) {
                player.getPlayer().setGameMode(gameMode);
            }
        }
    }

    private static void clearTeamInventories(Team team) {
        if(team == Team.CRACK_BUSTER) {
            for(CBPlayer player : crackBusters) {
                player.getPlayer().getInventory().clear();
            }
        } else if(team == Team.HUNTER) {
            for(CBPlayer player : hunters) {
                player.getPlayer().getInventory().clear();
            }
        }
    }

    private static void clearTeamPotionEffects(Team team) {
        if(team == Team.CRACK_BUSTER) {
            for(CBPlayer player : crackBusters) {
                for (PotionEffect effect : player.getPlayer().getActivePotionEffects()) {
                    player.getPlayer().removePotionEffect(effect.getType());
                }
            }
        } else if(team == Team.HUNTER) {
            for(CBPlayer player : hunters) {
                for (PotionEffect effect : player.getPlayer().getActivePotionEffects()) {
                    player.getPlayer().removePotionEffect(effect.getType());
                }
            }
        }
    }

    private static void setAllPlayersAlive() {
        for(CBPlayer player : players.values()) {
            player.setDead(false);
        }
    }

    private static void giveTeamItems(List<ItemStack> items, Team team) {
        for(ItemStack item : items) {
            if(item == null) {
                continue;
            }
            if(team == Team.HUNTER) {
                for(CBPlayer player : hunters) {
                    player.getPlayer().getInventory().addItem(item);
                }
            } else if(team == Team.CRACK_BUSTER) {
                for(CBPlayer player : crackBusters) {
                    player.getPlayer().getInventory().addItem(item);
                }
            } else if(team == Team.SPECTATOR) {
                for(CBPlayer player : spectators) {
                    player.getPlayer().getInventory().addItem(item);
                }
            }
        }
    }

    private static void giveTeamPotionEffect(Team team, PotionEffectType potion, int duration, int amplifier) {
        if(team == Team.HUNTER) {
            for(CBPlayer player : hunters) {
                player.applyPotionEffect(potion, duration, amplifier);
            }
        } else if(team == Team.CRACK_BUSTER) {
            for(CBPlayer player : crackBusters) {
                player.applyPotionEffect(potion, duration, amplifier);
            }
        }
    }

    private static void respawnPlayer(CBPlayer player) {
        PlayerInventory inventory = player.getPlayer().getInventory();
        player.getPlayer().setGameMode(GameMode.SURVIVAL);
        inventory.clear();
        for (ItemStack item : crackBusterItems) {
            inventory.addItem(item);
        }
        player.getPlayer().teleport(gameSpawn);
        Crack.handlePlayerJoin(player);
        player.setInvincible(true);
        player.setDead(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setInvincible(false);
            }
        }.runTaskLater(CrackBusters.instance, 20 * respawnInvincibilityTime);
    }
    @EventHandler
    public static void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        players.put(player.getUniqueId(), new CBPlayer(player));
        CBPlayer cbPlayer = players.get(player.getUniqueId());
        crackBusters.add(cbPlayer);
        cbPlayer.updateScoreboard();
        if(gameState == GameState.PLACING) {
            cbPlayer.getPlayer().teleport(lobbySpawn);
        } else if(gameState == GameState.BREAKING) {
            respawnPlayer(cbPlayer);
        }
    }

    @EventHandler
    public static void handleBlockPlaced(BlockPlaceEvent event) {
        CBPlayer player = players.get(event.getPlayer().getUniqueId());
        Block block = event.getBlock();
        Location location = block.getLocation();
        Material material = block.getType();
        // Check that block placed is of type hidden block
        if(gameState == GameState.BREAKING && !hiddenBlocks.containsKey(material)) {
            event.setCancelled(true);
            return;
        }
        HiddenBlock hiddenBlock = hiddenBlocks.get(material);
        if(hiddenBlock == null) {
            return;
        }
        if(gameState == GameState.PLACING && player.getTeam() == Team.HUNTER) {
            if(hiddenBlock.isPlaced()) {
                player.sendErrorMessage("Could not place block because it is already placed at "
                        + hiddenBlock.getLocation().toVector());
                event.setCancelled(true);
                return;
            }
            hiddenBlock.setPlaced(true);
            hiddenBlock.setLocation(location);
            if(allBlocksHidden()) {
                startBreaking();
            }
        } else if(gameState == GameState.BREAKING && player.getTeam() == Team.CRACK_BUSTER) {
            // check if block is placed somewhere else
            if(hiddenBlock.isPlaced()) {
                player.sendErrorMessage("Could not place block because it is already placed somewhere");
                event.setCancelled(true);
                return;
            }
            // check if placed on pedestal
            if(!hiddenBlock.getPedestalLocation().equals(location)) {
                player.sendErrorMessage("Place the block on the correct pedestal!");
                event.setCancelled(true);
                return;
            }
            hiddenBlock.setFound(true);

            if(allBlocksFound()) {
                endGame(event.getBlock().getWorld(), Team.CRACK_BUSTER);
            } else {
                // TODO : add player name that placed block
                // broadcast message
                CommonUtil.sendTitleToAll(ColorUtils.colorizeText("#0#" + hiddenBlock.getName() + " Block",
                        new ArrayList<>(Collections.singletonList(hiddenBlock.getColor().asBungee().getColor()))), "Found", 10, 60, 10);

                // give player speed
                player.applyPotionEffect(PotionEffectType.SPEED, blockPlacedSpeedDuration * 20, blockPlacedSpeedAmplifier);
            }
        }
    }

    @EventHandler
    public static void handleBlockBreak(BlockBreakEvent event) {
        CBPlayer player = players.get(event.getPlayer().getUniqueId());
        Team playerTeam = player.getTeam();
        Block block = event.getBlock();

        if(gameState == GameState.PLACING && playerTeam == Team.HUNTER) {
            HiddenBlock hiddenBlock = hiddenBlocks.getOrDefault(block.getType(), null);
            if(hiddenBlock == null) {
                event.setCancelled(true);
                return;
            }
            hiddenBlock.setPlaced(false);
            hiddenBlock.setFound(false);
        } else if(gameState == GameState.BREAKING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void handleBlockDamage(BlockDamageEvent event) {
        CBPlayer player = players.get(event.getPlayer().getUniqueId());
        Team playerTeam = player.getTeam();
        Block block = event.getBlock();
        if(gameState == GameState.BREAKING && playerTeam == Team.CRACK_BUSTER) {
            HiddenBlock hiddenBlock = hiddenBlocks.getOrDefault(block.getType(), null);
            if(hiddenBlock == null) {
                event.setCancelled(true);
                return;
            }
            if(!hiddenBlock.getLocation().equals(block.getLocation())) {
                player.sendErrorMessage("This is not the hidden block");
                event.setCancelled(true);
                return;
            }
            ItemStack item = player.getPlayer().getInventory().getItemInMainHand();
            CBItem cbItem = ItemUtil.getCBItem(item);
            if(cbItem instanceof Pickaxe) {
                player.getPlayer().getInventory().addItem(new ItemStack(block.getType()));
                SoundUtil.playGlobalSound(Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
                hiddenBlock.setPlaced(false);
                block.setType(Material.AIR);
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void handlePlayerMove(PlayerMoveEvent event) {
        checkForZoneCross(event);
        checkForRebootCardPickup(event);
    }

    @EventHandler
    public static void handlePlayerDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            CBPlayer player = players.get(((Player) event.getEntity()).getUniqueId());
            if(gameState != GameState.BREAKING) {
                return;
            }
            if(player.isInvincible()) {
                event.setCancelled(true);
                return;
            }
            if(event.getDamageSource().getDamageType() != DamageType.PLAYER_ATTACK) {
                event.setCancelled(true);
                return;
            }
            CBPlayer damager = players.get(((Player) event.getDamageSource().getCausingEntity()).getUniqueId());
            if(damager == null) return;
            if(damager.getTeam() != Team.HUNTER) {
                event.setCancelled(true);
                return;
            }
            if(player.getTeam() != Team.CRACK_BUSTER) {
                event.setCancelled(true);
                return;
            }
            CBItem item = ItemUtil.getCBItem(damager.getPlayer().getInventory().getItemInMainHand());
            if(item instanceof Sword) { // kill player
                // send messages
                Bukkit.broadcastMessage(net.md_5.bungee.api.ChatColor.of(Team.CRACK_BUSTER.getColor()) + player.getPlayer().getName()
                        + ChatColor.WHITE + " was killed by " + net.md_5.bungee.api.ChatColor.of(Team.HUNTER.getColor()) + damager.getPlayer().getName());

                player.getPlayer().sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "Killed By", ChatColor.RED + damager.getPlayer().getName(), 0, 50, 10);

                // Add player to dead players list
                player.setDead(true);
                crackBusters.remove(player);

                // check if player had one of the blocks
                for(Material block : hiddenBlocks.keySet()) {
                    if(player.getPlayer().getInventory().contains(block)) {
                        // give random player block if player had it
                        crackBusters.get(random.nextInt(crackBusters.size())).getPlayer().getInventory().addItem(new ItemStack(block));
                    }
                }
                player.getPlayer().getInventory().clear();

                // set gamemode
                player.getPlayer().setGameMode(GameMode.SPECTATOR);

                // lightning effect
                player.getPlayer().getWorld().strikeLightningEffect(player.getPlayer().getLocation());

                // spawn reboot card
                spawnRebootCard(player);

                // check if all crack busters are dead
                if(allCrackBustersDead()) {
                    endGame(player.getPlayer().getWorld(), Team.HUNTER);
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void handleGamemodeChange(PlayerCommandPreprocessEvent event) {
        // prevent gamemode changes while in breaking state
        if(gameState == GameState.BREAKING) {
            if(event.getMessage().startsWith("/gamemode")) {
                event.setCancelled(true);
            }
        }
    }

    private static void checkForZoneCross(PlayerMoveEvent event) {
        if(gameState != GameState.BREAKING) {
            return;
        }
        CBPlayer player = players.get(event.getPlayer().getUniqueId());
        if(player.getTeam() == Team.SPECTATOR || player.isDead()) {
            return;
        }
        Location location = player.getPlayer().getLocation();
        long timeOfLastZone = zoneCooldown.getOrDefault(player, System.currentTimeMillis() - zoneCooldownTime);
        // check if player is off cooldown
        if(timeOfLastZone + zoneCooldownTime > System.currentTimeMillis()) {
            return;
        }
        for(Zone zone : zones.keySet()) {
            Tuple<Location, Location> edges = zones.get(zone);
            if(edges == null) {
                continue;
            }
            Location edge1 = edges.getFirst();
            Location edge2 = edges.getSecond();
            if(edge1 == null || edge2 == null) {
                continue;
            }
            float minX = Math.min(edge1.getBlockX(), edge2.getBlockX());
            float maxX = Math.max(edge1.getBlockX(), edge2.getBlockX());
            float minY = Math.min(edge1.getBlockY(), edge2.getBlockY());
            float maxY = Math.max(edge1.getBlockY(), edge2.getBlockY());
            float minZ = Math.min(edge1.getBlockZ(), edge2.getBlockZ());
            float maxZ = Math.max(edge1.getBlockZ(), edge2.getBlockZ());

            // check if player is inside zone transition cube
            if(location.getX() >= minX && location.getX() <= maxX && location.getY() >= minY
                    && location.getY() <= maxY && location.getZ() >= minZ && location.getZ() <= maxZ) {
                Bukkit.broadcastMessage(ChatColor.BOLD + "A player has crossed the " + zone.getColoredName() + " Zone");
                player.setZone(zone);
                zoneCooldown.put(player, System.currentTimeMillis());
                player.updateScoreboard();
            }
        }
    }

    private static void checkForRebootCardPickup(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        List<Entity> nearbyEntities = player.getNearbyEntities(2, 2, 2);
        if(players.get(player.getUniqueId()).getTeam() != Team.CRACK_BUSTER) return;
        if(players.get(player.getUniqueId()).isDead()) return;
        List<Entity> toRemove = new ArrayList<>();
        for (Entity nearbyEntity : nearbyEntities) {
            if(!(nearbyEntity instanceof ArmorStand)) continue;
            PersistentDataContainer data = nearbyEntity.getPersistentDataContainer();
            if(data.has(Keys.rebootCard)) {
                String uuid = data.get(Keys.rebootCard, PersistentDataType.STRING);
                if(uuid == null) continue;
                CBPlayer cbPlayer = players.get(UUID.fromString(uuid));
                cbPlayer.setRebootCollected(true);
                respawnPlayer(cbPlayer);
                nearbyEntity.remove();
                toRemove.add(nearbyEntity);
            }
        }
        nearbyEntities.removeAll(toRemove);
    }

    private static void spawnRebootCard(CBPlayer player) {
        Location location = player.getPlayer().getLocation();
        World world = location.getWorld();

        ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setCustomName(net.md_5.bungee.api.ChatColor.of(Team.CRACK_BUSTER.getColor()) + "" + ChatColor.BOLD + player.getPlayer().getName() + "'s Reboot Card");  // Set the nametag
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setMarker(true);
        armorStand.setSilent(true);

        ItemStack helmet = new ItemStack(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
        armorStand.getEquipment().setHelmet(helmet);

        // store player uuid in armorstand
        PersistentDataContainer data = armorStand.getPersistentDataContainer();
        data.set(Keys.rebootCard, PersistentDataType.STRING, player.getPlayer().getUniqueId().toString());
        rebootCards.add(armorStand);
    }

    private static boolean allBlocksHidden() {
        for(HiddenBlock hiddenBlock : hiddenBlocks.values()) {
            if(hiddenBlock.isFound() || !hiddenBlock.isPlaced()) {
                return false;
            }
        }
        return true;
    }

    private static boolean allBlocksFound() {
        for(HiddenBlock hiddenBlock : hiddenBlocks.values()) {
            if(!hiddenBlock.isFound()) {
                return false;
            }
        }
        return true;
    }

    private static boolean allCrackBustersDead() {
        for(CBPlayer player : crackBusters) {
            if(!player.isDead()) return false;
        }
        return true;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static CBPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public static List<CBPlayer> getTeam(Team team) {
        if(team == Team.HUNTER) {
            return hunters;
        } else if(team == Team.CRACK_BUSTER) {
            return crackBusters;
        } else if(team == Team.SPECTATOR) {
            return spectators;
        }
        return null;
    }

    @Nullable
    public static HiddenBlock getHiddenBlock(Material material) {
        return hiddenBlocks.getOrDefault(material, null);
    }

    public static HashMap<Material, HiddenBlock> getHiddenBlocks() {
        return hiddenBlocks;
    }

    public static void setGameSpawn(Location newGameSpawn) {
        gameSpawn = newGameSpawn;
    }

    public static void setLobbySpawn(Location newLobbySpawn) {
        lobbySpawn = newLobbySpawn;
    }

    public static void setZoneLocation(Zone zone, Location location, int which) {
        if(zone == null || location == null) {
            return;
        }
        if(which == 1) {
            zones.get(zone).setFirst(location);
        } else if (which == 2) {
            zones.get(zone).setSecond(location);
        }
    }

    public static void setPedestalLocation(Zone zone, Location location) {
        if(zone == null || location == null) {
            return;
        }
        hiddenBlocks.get(zone.getMaterial()).setPedestalLocation(location);
    }

    public static void setPlayerTeam(Player player, Team team) {
        CBPlayer cbPlayer = players.get(player.getUniqueId());
        crackBusters.remove(cbPlayer);
        spectators.remove(cbPlayer);
        hunters.remove(cbPlayer);
        if(team == Team.HUNTER) {
            hunters.add(cbPlayer);
        } else if(team == Team.CRACK_BUSTER) {
            crackBusters.add(cbPlayer);
        } else if(team == Team.SPECTATOR) {
            spectators.add(cbPlayer);
        }
    }
}
