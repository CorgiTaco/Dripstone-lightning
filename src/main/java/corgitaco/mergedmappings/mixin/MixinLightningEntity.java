package corgitaco.mergedmappings.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public class MixinLightningEntity {


    @Inject(method = "clearCopperOnLightningStrike", at = @At("HEAD"))
    private static void transformDripStone(Level world, BlockPos pos, CallbackInfo ci) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        int rodOffset = 0;
        if (world.getBlockState(pos).getBlock() == Blocks.LIGHTNING_ROD) {
            rodOffset = -1;
        }

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = rodOffset; yOffset <= 0; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    mutableBlockPos.setWithOffset(pos, xOffset, yOffset, zOffset);

                    BlockState blockState = world.getBlockState(mutableBlockPos);

                    if (blockState.getBlock() == Blocks.DRIPSTONE_BLOCK) {
                        world.setBlockAndUpdate(mutableBlockPos, Blocks.LAVA.defaultBlockState());
                        world.getLiquidTicks().scheduleTick(mutableBlockPos, Fluids.LAVA, 0);
                    }
                }
            }
        }
    }
}
