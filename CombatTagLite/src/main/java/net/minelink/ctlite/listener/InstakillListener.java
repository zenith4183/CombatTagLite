package net.minelink.ctlite.listener;

import net.minelink.ctlite.CombatTagLite;
import net.minelink.ctlite.event.CombatLogEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static com.google.common.base.Preconditions.*;

public class InstakillListener implements Listener {
    private final CombatTagLite plugin;

    public InstakillListener(CombatTagLite plugin) {
        this.plugin = checkNotNull(plugin, "Null plugin");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCombatLog(CombatLogEvent event) {
        if (!plugin.getSettings().instantlyKill()) return; // We aren't configured to instakill

        // Kill the player
        if (event.getReason() == CombatLogEvent.Reason.TAGGED) {
            event.getPlayer().setHealth(0);
        }
    }
}
