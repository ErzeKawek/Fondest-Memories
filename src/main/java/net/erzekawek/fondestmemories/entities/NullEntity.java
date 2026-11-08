package net.erzekawek.fondestmemories.entities;

import net.erzekawek.fondestmemories.ai.FlankTargetGoal;
import net.erzekawek.fondestmemories.registry.FondestMemoriesSFX;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NullEntity extends Monster {
    public NullEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    private boolean hasSpotted = false;
    private int lostSightTicks = 0;
    private static final int FORGET_AFTER_TICKS = 100;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FlankTargetGoal(this, 1.0));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.6, true));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false) {
            {
                this.targetConditions.range(48.0);
            }
        });
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide() && this.getTarget() == null && this.tickCount % 20 == 0) {
            Player p = level().getNearestPlayer(this, 256.0);
            if (p != null && !p.isCreative() && !p.isSpectator()) this.setTarget(p);
        }
        LivingEntity target = this.getTarget();
        if (target == null) {
            hasSpotted = false;
            lostSightTicks = 0;
            return;
        }

        if (this.hasLineOfSight(target)) {
            lostSightTicks = 0;
            if (!hasSpotted) {
                hasSpotted = true;
                onSpotTarget(target);
            }
        } else if (hasSpotted && ++lostSightTicks > FORGET_AFTER_TICKS) {
            hasSpotted = false;
        }
    }

    private void onSpotTarget(LivingEntity target) {
        this.playSound(FondestMemoriesSFX.STALKER_SPOT, 1.5f, 1.0f);
    }
}
