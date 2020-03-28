package net.minelink.ctlite.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.minelink.ctlite.BlockPosition;
import net.minelink.ctlite.util.LruCache;
import org.bukkit.Location;

public final class HookManager {

    private final LruCache<BlockPosition, PvpBlock> pvpBlocks = new LruCache<>(100000);

    public HookManager() {}


    public boolean isPvpEnabledAt(Location location) {
        long currentTime = System.currentTimeMillis();
        BlockPosition position = new BlockPosition(location);
        PvpBlock pvpBlock;

        synchronized (pvpBlocks) {
            pvpBlock = pvpBlocks.get(position);

            if (pvpBlock != null && pvpBlock.expiry > currentTime) {
                return pvpBlock.enabled;
            }

            pvpBlock = new PvpBlock(currentTime + 60000);
            pvpBlocks.put(position, pvpBlock);
        }

        if (!isPvpEnabledAtRegion(location)) {
            pvpBlock.enabled = false;
        }

        return pvpBlock.enabled;
    }

    public boolean isPvpEnabledAtRegion(Location loc) {
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet applicableRegionSet = query.getApplicableRegions(BukkitAdapter.adapt(loc));

        return applicableRegionSet.testState(null, Flags.PVP);
    }


    private static class PvpBlock {

        private final long expiry;

        private boolean enabled = true;

        PvpBlock(long expiry) {
            this.expiry = expiry;
        }

    }

}
