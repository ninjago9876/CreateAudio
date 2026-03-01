package net.ninjago.createaudio.content.mechanicalSpeaker;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.sink.AudioSink;
import net.ninjago.createaudio.audio.sink.DebugLogAudioSink;

public class MechanicalSpeakerBlockEntity extends KineticBlockEntity {
    private AudioSink audioSink;

    public MechanicalSpeakerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level instanceof ServerLevel && !level.isClientSide()) {
            AudioEngine engine = CreateAudio.audioEngine;

            audioSink = new DebugLogAudioSink(engine.allocateUID());
            engine.submitTask((graph) -> graph.addSink(audioSink));
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
//        playing = speed >= 64;
    }
}
