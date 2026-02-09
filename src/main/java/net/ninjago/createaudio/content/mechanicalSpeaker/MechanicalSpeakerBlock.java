package net.ninjago.createaudio.content.mechanicalSpeaker;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.registry.ModBlockEntityTypes;

public class MechanicalSpeakerBlock extends DirectionalKineticBlock implements IBE<MechanicalSpeakerBlockEntity>, SimpleWaterloggedBlock {
    public MechanicalSpeakerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }

    @Override
    public Class<MechanicalSpeakerBlockEntity> getBlockEntityClass() {
        return MechanicalSpeakerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalSpeakerBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.MECHANICAL_SPEAKER.get();
    }
}
