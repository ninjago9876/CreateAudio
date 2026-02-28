package net.ninjago.createaudio.content.mechanicalSpeaker;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.voicechat.SimpleVoiceChatPlugin;

public class MechanicalSpeakerBlockEntity extends KineticBlockEntity {
    public MechanicalSpeakerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
//        playing = speed >= 64;
    }
}
