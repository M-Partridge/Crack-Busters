package me.dumplingdash.crackBusters.Core.Game;

import me.dumplingdash.crackBusters.Config.CBConfig;
import me.dumplingdash.crackBusters.Config.ConfigPaths;
import me.dumplingdash.crackBusters.Utility.CommonUtil;
import me.dumplingdash.crackBusters.CrackBusters;
import me.dumplingdash.crackBusters.Enums.GameState;
import me.dumplingdash.crackBusters.Enums.Team;
import me.dumplingdash.crackBusters.Item.CBItem;
import me.dumplingdash.crackBusters.Utility.ItemUtil;
import me.dumplingdash.crackBusters.Item.Items.Pickaxe;
import me.dumplingdash.crackBusters.Item.Items.Sword;
import me.dumplingdash.crackBusters.Utility.ColorUtils;
import me.dumplingdash.crackBusters.Utility.SoundUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.awt.Color;
import java.util.*;
import java.util.List;

public class GameManager {
    private static Location lobbySpawn;
    private static Location gameSpawn;
    private static GameState gameState;
    private static final HashMap<UUID, CBPlayer> players = new HashMap<>();
    private static final HashMap<Material, HiddenBlock> hiddenBlocks = new HashMap<>();
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
    private static BukkitTask hunterTeleportTask;

    public static void enable() {
        lobbySpawn = CBConfig.loadLocation(ConfigPaths.lobbySpawn);
        gameSpawn = CBConfig.loadLocation(ConfigPaths.gameSpawn);
        gameState = GameState.LOBBY;
        hiddenBlocks.put(Material.GOLD_BLOCK, new HiddenBlock(CBConfig.loadLocation(ConfigPaths.goldPedestal), new Color(235, 178, 21)));
        hiddenBlocks.put(Material.EMERALD_BLOCK, new HiddenBlock(CBConfig.loadLocation(ConfigPaths.emeraldPedestal), new Color(24, 219, 24)));
        hiddenBlocks.put(Material.DIAMOND_BLOCK, new HiddenBlock(CBConfig.loadLocation(ConfigPaths.diamondPedestal), new Color(0, 199, 235)));
        hiddenBlocks.put(Material.NETHERITE_BLOCK, new HiddenBlock(CBConfig.loadLocation(ConfigPaths.netheritePedestal), new Color(54, 47, 45)));
        blockPlacedSpeedAmplifier = CBConfig.loadInteger(ConfigPaths.blockPlacedSpeedAmplifier);
        blockPlacedSpeedDuration = CBConfig.loadInteger(ConfigPaths.blockPlacedSpeedDuration);
        hunterTeleportDelay = CBConfig.loadInteger(ConfigPaths.hunterTeleportDelay);
    }

    public static void disable() {
        CBConfig.saveData(ConfigPaths.lobbySpawn, lobbySpawn);
        CBConfig.saveData(ConfigPaths.gameSpawn, gameSpawn);
        CBConfig.saveData(ConfigPaths.goldPedestal, hiddenBlocks.get(Material.GOLD_BLOCK).getPedestalLocation());
        CBConfig.saveData(ConfigPaths.emeraldPedestal, hiddenBlocks.get(Material.EMERALD_BLOCK).getPedestalLocation());
        CBConfig.saveData(ConfigPaths.diamondPedestal, hiddenBlocks.get(Material.DIAMOND_BLOCK).getPedestalLocation());
        CBConfig.saveData(ConfigPaths.netheritePedestal, hiddenBlocks.get(Material.NETHERITE_BLOCK).getPedestalLocation());
        CBConfig.saveData(ConfigPaths.blockPlacedSpeedAmplifier, blockPlacedSpeedAmplifier);
        CBConfig.saveData(ConfigPaths.blockPlacedSpeedDuration, blockPlacedSpeedDuration);
        CBConfig.saveData(ConfigPaths.hunterTeleportDelay, hunterTeleportDelay);
    }

    public static void tryStartGame() {
        if(gameState != GameState.LOBBY) {
            CrackBusters.logMessage("Could not start game because game has already started");
            //return;
        }
        if(hunters.isEmpty() || crackBusters.isEmpty()) {
            CrackBusters.logMessage("Could not start game because one or both teams don't have at least 1 player");
            //return;
        }
        if(lobbySpawn == null) {
            CrackBusters.logMessage("Could not start game because no lobby spawn has been set");
            //return;
        }
        if(gameSpawn == null) {
            CrackBusters.logMessage("Could not start game because no game spawn has been set");
           /// return;
        }
        for(Material material : hiddenBlocks.keySet()) {
            if(hiddenBlocks.get(material).getPedestalLocation() == null) {
                CrackBusters.logMessage("Could not start game because " + material.name() + " has no pedestal location");
               // return;
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

        // give blocks to hunters
        List<Material> blocks = new ArrayList<>(hiddenBlocks.keySet());
        while(!blocks.isEmpty()) {
            for(CBPlayer hunter : hunters) {
                hunter.getPlayer().getInventory().addItem(new ItemStack(blocks.get(0)));
                blocks.remove(0);
            }
        }
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

        giveTeamPotionEffect(Team.HUNTER, PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, Integer.MAX_VALUE);
        giveTeamPotionEffect(Team.CRACK_BUSTER, PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, Integer.MAX_VALUE);

        // give players their items
        List<ItemStack> hunterItems = new ArrayList<>();
        hunterItems.add((new Sword()).getItem());
        hunterItems.add((new Pickaxe()).getItem());

        giveTeamItems(hunterItems, Team.HUNTER);

        List<ItemStack> crackBusterItems = new ArrayList<>();
        crackBusterItems.add((new Pickaxe()).getItem());

        giveTeamItems(crackBusterItems, Team.CRACK_BUSTER);

        // start hunter teleport task
        hunterTeleportTask = new BukkitRunnable() {
            @Override
            public void run() {
                teleportTeam(Team.HUNTER, gameSpawn);
            }
        }.runTaskLater(CrackBusters.instance, 20L * hunterTeleportDelay);
    }

    public static void endGame(World world, Team winningTeam) {
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

        CommonUtil.sendTitleToAll(winningTeam.getColoredName(), "Win!", 10, 60, 10);

        clearTeamInventories(Team.HUNTER);
        clearTeamInventories(Team.CRACK_BUSTER);

        teleportTeam(Team.HUNTER, lobbySpawn);
        teleportTeam(Team.CRACK_BUSTER, lobbySpawn);

        hunterTeleportTask.cancel();
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

    public static void handlePlayerJoin(PlayerJoinEvent event) {
        players.put(event.getPlayer().getUniqueId(), new CBPlayer(event.getPlayer()));
        spectators.add(players.get(event.getPlayer().getUniqueId()));
    }

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

        if(gameState == GameState.PLACING && player.getTeam() == Team.HUNTER) {
            if(hiddenBlock.isPlaced()) {
                player.sendErrorMessage("Could not place block because it is already placed at "
                        + hiddenBlock.getLocation());
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
            if(hiddenBlock.getPedestalLocation() != location) {
                player.sendErrorMessage("Place the block on the correct pedestal!");
                event.setCancelled(true);
                return;
            }
            hiddenBlock.setFound(true);

            if(allBlocksFound()) {
                endGame(event.getBlock().getWorld(), Team.CRACK_BUSTER);
            } else {
                // broadcast message
                CommonUtil.sendTitleToAll(ColorUtils.colorizeText("#0#" + material.name(),
                        new ArrayList<>(Collections.singletonList(hiddenBlock.getColor()))), "Found", 10, 60, 10);

                // give player speed
                player.applyPotionEffect(PotionEffectType.SPEED, blockPlacedSpeedDuration, blockPlacedSpeedAmplifier);
            }
        }
    }

    public static void handleBlockBreak(BlockBreakEvent event) {

    }

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

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameSpawn(Location newGameSpawn) {
        gameSpawn = newGameSpawn;
    }

    public static void setLobbySpawn(Location newLobbySpawn) {
        lobbySpawn = newLobbySpawn;
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
