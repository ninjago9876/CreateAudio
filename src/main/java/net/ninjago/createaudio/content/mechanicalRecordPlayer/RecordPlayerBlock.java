package net.ninjago.createaudio.content.mechanicalRecordPlayer;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.registry.ModBlockEntityTypes;

public class RecordPlayerBlock extends HorizontalKineticBlock
        implements IBE<RecordPlayerBlockEntity>, ICogWheel {
    public RecordPlayerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<RecordPlayerBlockEntity> getBlockEntityClass() {
        return RecordPlayerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RecordPlayerBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.MECHANICAL_RECORD_PLAYER.get();
    }
}
