package net.minelink.ctlite;

import net.minelink.ctlite.hook.Hook;
import net.minelink.ctlite.hook.HookManager;
import net.minelink.ctlite.listener.ForceFieldListener;
import net.minelink.ctlite.listener.InstakillListener;
import net.minelink.ctlite.listener.PlayerListener;
import net.minelink.ctlite.listener.TagListener;
import net.minelink.ctlite.task.ForceFieldTask;
import net.minelink.ctlite.task.SafeLogoutTask;
import net.minelink.ctlite.task.TagUpdateTask;
import net.minelink.ctlite.util.BarUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

public final class CombatTagLite extends JavaPlugin {

    private final PlayerCache playerCache = new PlayerCache();

    private Settings settings;

    private HookManager hookManager;

    private TagManager tagManager;

    public PlayerCache getPlayerCache() {
        return playerCache;
    }

    public Settings getSettings() {
        return settings;
    }

    public HookManager getHookManager() {
        return hookManager;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    @Override
    public void onEnable() {
        // Load settings
        saveDefaultConfig();

        settings = new Settings(this);
        if (settings.isOutdated()) {
            settings.update();
            getLogger().info("Configuration file has been updated.");
        }

        // Initialize plugin state
        hookManager = new HookManager(this);
        tagManager = new TagManager(this);

        integrateWorldGuard();

        BarUtils.init();

        // Build player cache from currently online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            getPlayerCache().addPlayer(player);
        }

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new ForceFieldListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InstakillListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new TagListener(this), this);

        // Anti-SafeZone task
        ForceFieldTask.run(this);

        // Periodic task for purging unused data
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                getTagManager().purgeExpired();
                TagUpdateTask.purgeFinished();
                SafeLogoutTask.purgeFinished();
            }
        }, 3600, 3600);

    }

    @Override
    public void onDisable() {
        TagUpdateTask.cancelTasks(this);
    }

       private void integrateWorldGuard() {
        if (!getSettings().useWorldGuard()) {
            return;
        }

        // Determine if WorldGuard is loaded
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null) {
            return;
        }

        String v = plugin.getDescription().getVersion();

        // Determine which hook implementation to use
        String className = "net.minelink.ctlite.worldguard.v" + (v.startsWith("5") ? 5 : 6) + ".WorldGuardHook";

        try {
            // Create and add WorldGuardHook
            getHookManager().addHook((Hook) Class.forName(className).newInstance());
        } catch (Exception e) {
            // Something went wrong, chances are it's a newer, incompatible WorldGuard
            getLogger().warning("**WARNING**");
            getLogger().warning("Failed to enable WorldGuard integration due to errors.");
            getLogger().warning("This is most likely due to a newer WorldGuard.");

            // Let's leave a stack trace in console for reporting
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("ctlitereload")) {
            reloadConfig();
            getSettings().load();
            if (sender instanceof Player) {
                sender.sendMessage(GREEN + getName() + " config reloaded.");
            }

            getLogger().info("Config reloaded by " + sender.getName());
        } else if (cmd.getName().equals("combattaglite")) {
            if (!(sender instanceof Player)) return false;

            UUID uniqueId = ((Player) sender).getUniqueId();
            Tag tag = getTagManager().getTag(uniqueId);
            if (tag == null || tag.isExpired() || !getTagManager().isTagged(uniqueId)) {
                sender.sendMessage(getSettings().getCommandUntagMessage());
                return true;
            }

            String duration = settings.formatDuration(tag.getTagDuration());
            sender.sendMessage(getSettings().getCommandTagMessage().replace("{time}", duration));
        } else if (cmd.getName().equals("ctlitelogout")) {
            if (!(sender instanceof Player)) return false;

            // Do nothing if player is already logging out
            Player player = (Player) sender;
            if (SafeLogoutTask.hasTask(player)) return false;

            // Attempt to start a new logout task
            SafeLogoutTask.run(this, player);
        } else if (cmd.getName().equals("ctliteuntag")) {

            if (args.length < 1) {
                sender.sendMessage(RED + "Please specify a player to untag");
                return true;
            }

            Player player = getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(RED + args[0] + " is not currently online!");
                return true;
            }
            UUID uniqueId = player.getUniqueId();
            if (getTagManager().untag(uniqueId)) {
                sender.sendMessage(GREEN + "Successfully untagged " + player.getName() + ".");
            } else {
                sender.sendMessage(GREEN + player.getName() + " is already untagged.");
            }
        }

        return true;
    }

}
