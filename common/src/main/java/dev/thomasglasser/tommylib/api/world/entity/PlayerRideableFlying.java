package dev.thomasglasser.tommylib.api.world.entity;

import net.minecraft.world.entity.PlayerRideable;
import net.minecraft.world.entity.player.Player;

public interface PlayerRideableFlying extends PlayerRideable
{
    void ascend();
    void descend();
    void stop();
    double getVerticalSpeed();
    enum Flight {
        ASCENDING,
        DESCENDING,
        HOVERING
    }

    static boolean isRidingFlyable(Player player) {
        return player.getVehicle() instanceof PlayerRideableFlying && player.getVehicle().getControllingPassenger().is(player);
    }
}