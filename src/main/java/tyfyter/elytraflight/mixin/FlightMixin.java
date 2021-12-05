package tyfyter.elytraflight.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class FlightMixin {
	@Shadow public Input input;
	@Final
	@Shadow protected MinecraftClient client;
	
	Vec3d lastVelocity = Vec3d.ZERO;
	
	@Inject(at = @At("HEAD"), method = "tickMovement")
	private void tickMovement(CallbackInfo info) {
		PlayerEntity self = ((PlayerEntity)(Object)this);
		if(self.isFallFlying()){
			if(self.getAbilities().allowFlying && this.input.hasForwardMovement()){
				Vec3d look = self.getRotationVector();
				Vec3d vel = lastVelocity;
				//1.5 is fireworks
				double mult = self.getAbilities().getFlySpeed() * (this.client.options.keySprint.isPressed() ? 22 : 17);
				self.setVelocity(vel.add(look.x * 0.1D + (look.x * mult - vel.x) * 0.5D, look.y * 0.1D + (look.y * mult - vel.y) * 0.5D, look.z * 0.1D + (look.z * mult - vel.z) * 0.5D));
				/*Vec3d vel = self.getVelocity();
				if (this.client.options.getPerspective().isFirstPerson()) {
					float g = self.getYaw() * 0.017453292F;
					float h = self.getPitch() * 0.017453292F;
					Vec3d vec3d = new Vec3d(0, 0, 1);
					//System.out.println(vec3d.length());
					vel = vec3d.rotateX(-h).rotateY(-g);
				}
				Vec3d look = ((EntityATInvoker)self).invokeGetRotationVector(self.getPitch(), self.getHeadYaw());
				double dot = vel.normalize().dotProduct(look);
				look = look.multiply(Math.max(self.getAbilities().getFlySpeed()*8, vel.length()*dot));
				//self.setVelocity(look);
				self.setVelocity(look.lerp(vel, dot * (vel.length() / look.length())));
				//System.out.println(vel.length()+" -> "+self.getVelocity().length());*/
			}
		}
		lastVelocity = self.getVelocity();
	}
}