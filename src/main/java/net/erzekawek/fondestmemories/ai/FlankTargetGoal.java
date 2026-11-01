package net.erzekawek.fondestmemories.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FlankTargetGoal extends Goal {
    private static final double FLANK_RADIUS = 9.0;
    private static final double STEP_ANGLE = Math.toRadians(55); // how far around the arc per waypoint
    private static final double DONE_ANGLE = Math.toRadians(135); // "behind enough" -> hand off to melee

    private final PathfinderMob mob;
    private final double speed;
    private int repathTicks;

    public FlankTargetGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = mob.getTarget();
        return t != null && t.isAlive() && needsFlanking(t);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity t = mob.getTarget();
        return t != null && t.isAlive() && needsFlanking(t);
    }

    @Override
    public void start() {
        repathTicks = 0;
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity t = mob.getTarget();
        if (t == null) return;

        mob.getLookControl().setLookAt(t, 30f, 30f);

        if (--repathTicks > 0) return;
        repathTicks = 8;

        Vec3 wp = nextWaypoint(t);
        Path path = mob.getNavigation().createPath(wp.x, wp.y, wp.z, 1);
        if (path != null) {
            mob.getNavigation().moveTo(path, speed);
        } else {
            // flank point unreachable (cliff, wall, water): just chase directly
            mob.getNavigation().moveTo(t, speed);
        }
    }

    /** True while the mob is in front of / beside the player and not yet close enough to strike. */
    private boolean needsFlanking(LivingEntity t) {
        if (mob.distanceTo(t) < 4.0) return false;
        return Math.abs(relativeAngle(t)) < DONE_ANGLE;
    }

    /** Angle of the mob around the target, relative to the target's facing. 0 = in front, ±PI = directly behind. */
    private double relativeAngle(LivingEntity t) {
        Vec3 look = t.getLookAngle();
        double facing = Math.atan2(look.z, look.x);
        double mobAngle = Math.atan2(mob.getZ() - t.getZ(), mob.getX() - t.getX());
        return Mth.wrapDegrees((float) Math.toDegrees(mobAngle - facing)) * Math.PI / 180.0;
    }

    private Vec3 nextWaypoint(LivingEntity t) {
        Vec3 look = t.getLookAngle();
        double facing = Math.atan2(look.z, look.x);
        double rel = relativeAngle(t);

        // Rotate around the player toward their back, taking the shorter side
        double dir = rel >= 0 ? 1 : -1;
        double newRel = rel + dir * STEP_ANGLE;
        if (Math.abs(newRel) > Math.PI) newRel = dir * Math.PI; // clamp to exactly behind

        double a = facing + newRel;
        double dist = mob.distanceTo(t);
        double r = Math.min(FLANK_RADIUS, Math.max(5.0, dist)); // don't swing wider than needed

        return new Vec3(t.getX() + Math.cos(a) * r, t.getY(), t.getZ() + Math.sin(a) * r);
    }
}