package audaki.cart_engine.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends VehicleEntity {

    @Shadow
    public abstract boolean isRideable();

    @Mutable
    @Final
    @Shadow
    private MinecartBehavior behavior;

    public AbstractMinecartMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Unique
    protected void juiceUpBehavior() {
        if (this.behavior instanceof OldMinecartBehavior && isRideable()) {
            AbstractMinecart instance = (AbstractMinecart) (Object) this;
            this.behavior = new NewMinecartBehavior(instance);
        }
    }

    @Inject(at = @At("HEAD"), method = "setInitialPos")
    public void _setInitialPos(double d, double e, double f, CallbackInfo ci) {
		this.juiceUpBehavior();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void _tick(CallbackInfo ci) {
        this.juiceUpBehavior();
    }

	@Redirect(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at=@At(value = "INVOKE", target="Lnet/minecraft/world/entity/vehicle/AbstractMinecart;useExperimentalMovement(Lnet/minecraft/world/level/Level;)Z"))
	private boolean redirectExperimentalMovement(Level level) {
		return isRideable();
	}

	@Redirect(method = "getCurrentBlockPosOrRailBelow()Lnet/minecraft/core/BlockPos;", at=@At(value = "INVOKE", target="Lnet/minecraft/world/entity/vehicle/AbstractMinecart;useExperimentalMovement(Lnet/minecraft/world/level/Level;)Z"))
	private boolean getCurrentBlockPosOrRailBelow(Level level) {
		return isRideable();
	}

	@Redirect(method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", at=@At(value = "INVOKE", target="Lnet/minecraft/world/entity/vehicle/AbstractMinecart;useExperimentalMovement(Lnet/minecraft/world/level/Level;)Z"))
	private boolean move(Level level) {
		return isRideable();
	}

	@Redirect(method = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;applyEffectsFromBlocks()V", at=@At(value = "INVOKE", target="Lnet/minecraft/world/entity/vehicle/AbstractMinecart;useExperimentalMovement(Lnet/minecraft/world/level/Level;)Z"))
	private boolean applyEffectsFromBlocks(Level level) {
		return isRideable();
	}

	@Redirect(method = "pushOtherMinecart(Lnet/minecraft/world/entity/vehicle/AbstractMinecart;DD)V", at=@At(value = "INVOKE", target="Lnet/minecraft/world/entity/vehicle/AbstractMinecart;useExperimentalMovement(Lnet/minecraft/world/level/Level;)Z"))
	private boolean pushOtherMinecart(Level level) {
		return isRideable();
	}
}
