package net.minelink.ctlite;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerCache {

    private final Map<UUID, Player> uuidCache = new HashMap<>();


    public void addPlayer(Player player) {
        uuidCache.put(player.getUniqueId(), player);
    }

    public void removePlayer(Player player) {
        uuidCache.remove(player.getUniqueId());
    }

    public boolean isOnline(UUID id) {
        return uuidCache.containsKey(id);
    }

    public Player getPlayer(UUID id) {
        return uuidCache.get(id);
    }

    public Collection<Player> getPlayers() {
        return Collections.unmodifiableCollection(uuidCache.values());
    }

}
