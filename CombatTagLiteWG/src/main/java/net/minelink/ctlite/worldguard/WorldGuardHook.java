package net.minelink.ctlite.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.minelink.ctlite.hook.Hook;
import org.bukkit.Location;

public final class WorldGuardHook implements Hook {

    @Override
    public boolean isPvpEnabledAt(Location loc) {
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet applicableRegionSet = query.getApplicableRegions(BukkitAdapter.adapt(loc));

        return applicableRegionSet.testState(null, Flags.PVP);
    }

}
