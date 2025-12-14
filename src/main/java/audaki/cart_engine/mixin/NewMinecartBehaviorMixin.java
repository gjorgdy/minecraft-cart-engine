package audaki.cart_engine.mixin;

import audaki.cart_engine.AceGameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.vehicle.minecart.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.IntConsumer;

@Mixin(NewMinecartBehavior.class)
public abstract class NewMinecartBehaviorMixin extends MinecartBehavior {

    protected NewMinecartBehaviorMixin(AbstractMinecart abstractMinecart) {
        super(abstractMinecart);
    }

    @Unique
    private final ServerLevel level = (ServerLevel) ((NewMinecartBehavior) (Object) this).level();

    @Inject(at = @At("HEAD"), method = "getMaxSpeed", cancellable = true)
    public void _getMaxSpeed(ServerLevel level, CallbackInfoReturnable<Double> cir) {
        if (!minecart.isRideable()) {
            return;
        }

        IntConsumer setSpeed = (speed) -> {
            if (speed == 0) {
                return;
            }
            cir.setReturnValue(speed * (this.minecart.isInWater() ? 0.5 : 1.0) / 20.0);
            cir.cancel();
        };

        Entity passenger = minecart.getFirstPassenger();
        if (passenger == null) {
            setSpeed.accept(level.getGameRules().get(AceGameRules.MINECART_MAX_SPEED_EMPTY_RIDER));
            return;
        }

        if (passenger instanceof Player) {
            setSpeed.accept(level.getGameRules().get(AceGameRules.MINECART_MAX_SPEED_PLAYER_RIDER));
            return;
        }

        setSpeed.accept(level.getGameRules().get(AceGameRules.MINECART_MAX_SPEED_OTHER_RIDER));
    }

    @Redirect(
        method = "calculateTrackSpeed(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$TrackIteration;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/properties/RailShape;)Lnet/minecraft/world/phys/Vec3;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior;calculateHaltTrackSpeed(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/phys/Vec3;")
    )
    public Vec3 calculateHaltTrackSpeed(NewMinecartBehavior instance, Vec3 vec3, BlockState blockState) {
        if (blockState.is(Blocks.POWERED_RAIL) && !(Boolean)blockState.getValue(PoweredRailBlock.POWERED)) {
            return vec3.length() * 100 < level.getGameRules().get(AceGameRules.MINECART_HALT_SPEED_THRESHOLD)
                    ? Vec3.ZERO
                    : vec3.scale(level.getGameRules().get(AceGameRules.MINECART_HALT_SPEED_MULTIPLIER) / 100d);
        } else {
            return vec3;
        }
    }
}
