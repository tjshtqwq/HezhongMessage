package org.hzmsg;

import dev.diona.pluginhooker.PluginHooker;
import me.liwk.karhu.api.KarhuAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.hzmsg.command.ItemCommands;
import org.hzmsg.command.KickCommand;
import org.hzmsg.command.MiscCommands;
import org.hzmsg.command.PingCommand;
import org.hzmsg.command.SelectAnticheatCommand;
import org.hzmsg.command.TPACommands;
import org.hzmsg.command.ToggleCommands;
import org.hzmsg.listener.AnticheatAlertsEvent;
import org.hzmsg.listener.BlockBreakListener;
import org.hzmsg.listener.BlockPlaceListener;
import org.hzmsg.listener.ChatListener;
import org.hzmsg.listener.DamageResetListener;
import org.hzmsg.listener.KickHandler;
import org.hzmsg.listener.MainListener;
import org.hzmsg.listener.SpeedTest;
import org.hzmsg.task.BlockClearTask;
import org.hzmsg.task.FoodRestoreTask;
import org.hzmsg.task.Message;
import org.hzmsg.task.ScaffoldCleanAlertTask;
import org.hzmsg.task.ScoreBoard;
import org.hzmsg.task.SpammerTaskA;
import org.hzmsg.task.TPARequestCleanupTask;
import org.hzmsg.task.TpsMonitorTask;
import org.hzmsg.task.VelocityApplyTask;
import org.hzmsg.utils.TabHandler;
import org.hzmsg.utils.type.AnticheatInfo;
import org.hzmsg.utils.type.TPARequest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class HezhongMessage extends JavaPlugin {

    public static final String prefix = "&b&lHezhong ";

    // 调度器
    public static BukkitScheduler scheduler;
    public static HezhongMessage instance;
    public static double currentTps = 20.;
    // Scaffold区域
    public static long lastScaffoldClean = System.currentTimeMillis();
    public static long nextScaffoldClean = System.currentTimeMillis() + 600000;
    public static long scaffoldCleanInterval = 600000;
    public static boolean scaffoldCleanAlertOn5Minutes = false;
    public static boolean scaffoldCleanAlertOn1Minutes = false;
    public static boolean scaffoldCleanAlertOn30Seconds = false;
    public static boolean scaffoldCleanAlertOn5Seconds = false;

    // 玩家数据统一PlayerDataManager

    // 所有TPA请求
    public static Map<UUID, TPARequest> tpaRequests = new HashMap<>();

    // 插件名称和反作弊
    public static Map<Anticheat, String> anticheatsVsPluginName = new HashMap<>();
    // 反作弊信息
    public static Map<Anticheat, AnticheatInfo> allAnticheats = new HashMap<>();


    public static final int MAX_ANTICHEATS = 2;
    public static final String RESTART_MESSAGE = "&c&l服务器即将重启。The server will be restart soon.";



    @Override
    public void onEnable() {
        say(prefix + "&aEnabling HezhongMessage");

        loadAnticheats:
        {
            anticheatsVsPluginName.put(Anticheat.MX, "MX");
            anticheatsVsPluginName.put(Anticheat.TATAKO, "Tatako");
            anticheatsVsPluginName.put(Anticheat.KARHU, "KarhuAC");
            anticheatsVsPluginName.put(Anticheat.ORCA, "ORCA");
            anticheatsVsPluginName.put(Anticheat.NANA, "NanaDetector");
            anticheatsVsPluginName.put(Anticheat.ACDV1, "AlexisCheatDetection");
            // anticheatsVsPluginName.put(Anticheat.STARRY, "StarryAnticheat");
            anticheatsVsPluginName.put(Anticheat.MATRIX, "Matrix");
            anticheatsVsPluginName.put(Anticheat.VULCAN, "Vulcan");
            // anticheatsVsPluginName.put(Anticheat.AMLEGIT, "AmLegit");
            // anticheatsVsPluginName.put(Anticheat.ETERNAL, "EternalAntiCheat");

            allAnticheats.put(Anticheat.VANILLA, new AnticheatInfo("Vanilla", Anticheat.VANILLA, null));
            allAnticheats.put(Anticheat.TATAKO, new AnticheatInfo("Tatako", Anticheat.TATAKO, Bukkit.getPluginManager().getPlugin("Tatako")));
            allAnticheats.put(Anticheat.MX, new AnticheatInfo("MX", Anticheat.MX, Bukkit.getPluginManager().getPlugin("MX")));
            allAnticheats.put(Anticheat.KARHU, new AnticheatInfo("Karhu", Anticheat.KARHU, Bukkit.getPluginManager().getPlugin("KarhuAC")));
            allAnticheats.put(Anticheat.ORCA, new AnticheatInfo("ORCA", Anticheat.ORCA, Bukkit.getPluginManager().getPlugin("ORCA")));
            allAnticheats.put(Anticheat.NANA, new AnticheatInfo("Nana", Anticheat.NANA, Bukkit.getPluginManager().getPlugin("NanaDetector")));
            allAnticheats.put(Anticheat.ACDV1, new AnticheatInfo("ACDV1", Anticheat.ACDV1, Bukkit.getPluginManager().getPlugin("AlexisCheatDetection")));
            // allAnticheats.put(Anticheat.STARRY, new AnticheatInfo("Starry", Anticheat.STARRY, Bukkit.getPluginManager().getPlugin("StarryAnticheat")));
            allAnticheats.put(Anticheat.MATRIX, new AnticheatInfo("Matrix", Anticheat.MATRIX, Bukkit.getPluginManager().getPlugin("Matrix"), false));
            allAnticheats.put(Anticheat.VULCAN, new AnticheatInfo("Vulcan", Anticheat.VULCAN, Bukkit.getPluginManager().getPlugin("Vulcan")));
            // allAnticheats.put(Anticheat.AMLEGIT, new AnticheatInfo("AmLegit", Anticheat.AMLEGIT, Bukkit.getPluginManager().getPlugin("AmLegit")));
            // allAnticheats.put(Anticheat.ETERNAL, new AnticheatInfo("Eternal", Anticheat.ETERNAL, Bukkit.getPluginManager().getPlugin("EternalAntiCheat")));
        }

        say("  _   _                _                                 ____                                      \n" +
                " | | | |   ___   ____ | |__     ___    _ __     __ _    / ___|    ___   _ __  __   __   ___   _ __ \n" +
                " | |_| |  / _ \\ |_  / | '_ \\   / _ \\  | '_ \\   / _` |   \\___ \\   / _ \\ | '__| \\ \\ / /  / _ \\ | '__|\n" +
                " |  _  | |  __/  / /  | | | | | (_) | | | | | | (_| |    ___) | |  __/ | |     \\ V /  |  __/ | |   \n" +
                " |_| |_|  \\___| /___| |_| |_|  \\___/  |_| |_|  \\__, |   |____/   \\___| |_|      \\_/    \\___| |_|   \n" +
                "                                               |___/                                               ");

        say(prefix + "&aHezhong Server Plugin (Anticheat test server) by tjshawa");


        say(prefix + "&e&lWARNING &cPluginHooker(DionaMC) and TitleAPI must be installed. Please check!");
        // 可惜了，无法钩入Artemis
        for (AnticheatInfo info : allAnticheats.values()) {
            if (info.hook) {
                PluginHooker.getPluginManager().addPlugin(info.plugin);
            }
        }
        // PluginHooker.getPluginManager().addPlugin(Bukkit.getServer().getPluginManager().getPlugin("AmLegit"));
        // PluginHooker.getPluginManager().addPlugin(Bukkit.getServer().getPluginManager().getPlugin("EternalAntiCheat"));
        instance = this;
        ItemCommands itemCommands = new ItemCommands();
        Bukkit.getPluginCommand("block").setExecutor(itemCommands);
        Bukkit.getPluginCommand("sword").setExecutor(itemCommands);
        Bukkit.getPluginCommand("gapple").setExecutor(itemCommands);
        Bukkit.getPluginCommand("pickaxe").setExecutor(itemCommands);

        ToggleCommands toggleCommands = new ToggleCommands();
        Bukkit.getPluginCommand("d").setExecutor(toggleCommands);
        Bukkit.getPluginCommand("nofall").setExecutor(toggleCommands);
        Bukkit.getPluginCommand("nodamage").setExecutor(toggleCommands);
        Bukkit.getPluginCommand("alert").setExecutor(toggleCommands);

        TPACommands tpaCommands = new TPACommands();
        Bukkit.getPluginCommand("tpa").setExecutor(tpaCommands);
        Bukkit.getPluginCommand("tpaccept").setExecutor(tpaCommands);
        Bukkit.getPluginCommand("tpdeny").setExecutor(tpaCommands);
        Bukkit.getPluginCommand("tpcancel").setExecutor(tpaCommands);

        MiscCommands miscCommands = new MiscCommands();
        Bukkit.getPluginCommand("help").setExecutor(miscCommands);
        Bukkit.getPluginCommand("suicide").setExecutor(miscCommands);
        Bukkit.getPluginCommand("shout").setExecutor(miscCommands);
        Bukkit.getPluginCommand("warp").setExecutor(miscCommands);
        Bukkit.getPluginCommand("rest").setExecutor(miscCommands);
        Bukkit.getPluginCommand("fly").setExecutor(miscCommands);
        Bukkit.getPluginCommand("info").setExecutor(miscCommands);

        Bukkit.getPluginCommand("selac").setExecutor(new SelectAnticheatCommand());
        Bukkit.getPluginCommand("ban").setExecutor(new KickCommand());
        Bukkit.getPluginCommand("kick").setExecutor(new KickCommand());
        Bukkit.getPluginCommand("pingpong").setExecutor(new PingCommand());

        // ScoreBoard manage suffix
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        // 废弃了 用LuckPerms更爽

        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new DamageResetListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new MainListener(), this);
        getServer().getPluginManager().registerEvents(new AnticheatAlertsEvent(), this);
        getServer().getPluginManager().registerEvents(new KickHandler(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new SpeedTest(), this);
        getCommand("warp").setTabCompleter(new TabHandler());
        getCommand("selac").setTabCompleter(new TabHandler());
        getCommand("tpa").setTabCompleter(new TabHandler());
        getCommand("tpaccept").setTabCompleter(new TabHandler());
        getCommand("tpdeny").setTabCompleter(new TabHandler());
        getCommand("tpcancel").setTabCompleter(new TabHandler());
        KarhuAPI.getEventRegistry().addListener(new AnticheatAlertsEvent());
        scheduler = getServer().getScheduler();
        HezhongMessage.scaffoldCleanInterval = 600000;
        HezhongMessage.nextScaffoldClean = System.currentTimeMillis() + HezhongMessage.scaffoldCleanInterval;
        scheduler.runTaskTimer(this, new BlockClearTask(getWorld(), 236, 291, 61, 256, 166, 206), 0L, 1L);
        scheduler.runTaskTimer(this, new FoodRestoreTask(), 0L, 0);
        scheduler.runTaskTimer(this, new Message(), 0L, 10000);
        scheduler.runTaskTimer(this, new SpammerTaskA(), 0L, 10000);
        scheduler.runTaskTimer(this, new TpsMonitorTask(), 0L, 1);
        scheduler.runTaskTimer(this, new VelocityApplyTask(), 0L, 1);
        scheduler.runTaskTimer(this, new ScaffoldCleanAlertTask(), 0L, 1);
        scheduler.runTaskTimer(this, new TPARequestCleanupTask(), 0L, 1);
        scheduler.runTaskTimer(this, new ScoreBoard(), 0L, 1);
        Runnable playerPotionTask = () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 114514, 2 - 1));
            }
        };
        scheduler.runTaskTimer(this, playerPotionTask, 1L, 40L);

    }


    public World getWorld() {
        return getServer().getWorlds().get(0);
    }


    @Override
    public void onDisable() {
        say("Disabling HezhongMessage");
    }

    public void say(String s) {
        CommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + s));
    }

    public CommandMap getCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception ignored) {}
        return null;
    }
}
