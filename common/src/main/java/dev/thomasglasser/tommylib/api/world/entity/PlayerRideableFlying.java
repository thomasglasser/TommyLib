package dev.thomasglasser.tommylib.api.world.entity;

import net.minecraft.world.entity.PlayerRideable;
import net.minecraft.world.entity.player.Player;

/**
 * Extension of {@link PlayerRideable} for entities that can fly.
 */
public interface PlayerRideableFlying extends PlayerRideable {
    void ascend();

    void descend();

    void stop();

    double getVerticalSpeed();

    enum Flight {
        ASCENDING,
        DESCENDING,
        HOVERING
    }

    /**
     * Check if the player is riding a flying entity.
     * 
     * @param player The player rider to check.
     * @return True if the player is riding a flying entity.
     */
    static boolean isRidingFlyable(Player player) {
        return player.getVehicle() instanceof PlayerRideableFlying && player.getVehicle().getControllingPassenger() == player;
    }
}
