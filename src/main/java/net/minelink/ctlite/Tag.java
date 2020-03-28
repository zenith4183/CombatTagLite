package net.minelink.ctlite;

import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.UUID;

public final class Tag {

    private long expireTime;

    private UUID victimId;

    private String victimName;

    private UUID attackerId;

    private String attackerName;

    Tag(long expireTime, Player victim, Player attacker) {
        this.expireTime = expireTime;

        // Determine victim identity
        if (victim != null) {
            this.victimId = victim.getUniqueId();
            this.victimName = victim.getName();
        }

        // Determine attacker identity
        if (attacker != null) {
            this.attackerId = attacker.getUniqueId();
            this.attackerName = attacker.getName();
        }
    }

    public UUID getVictimId() {
        return victimId;
    }

    public String getVictimName() {
        return victimName;
    }

    public UUID getAttackerId() {
        return attackerId;
    }

    public String getAttackerName() {
        return attackerName;
    }

    public int getTagDuration() {
        long currentTime = System.currentTimeMillis();
        return expireTime > currentTime ? NumberConversions.ceil((expireTime - currentTime) / 1000D) : 0;
    }

    public boolean isExpired() {
        return getTagDuration() < 1;
    }

}
