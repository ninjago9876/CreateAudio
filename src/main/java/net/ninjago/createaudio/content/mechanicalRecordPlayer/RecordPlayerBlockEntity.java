package net.ninjago.createaudio.content.mechanicalRecordPlayer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.source.AudioSource;
import net.ninjago.createaudio.audio.source.SynthesizerAudioSource;

public class RecordPlayerBlockEntity extends KineticBlockEntity {
    protected String audioFile = "";
    protected boolean playing = false;

    private AudioSource audioSource;

    public RecordPlayerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level instanceof ServerLevel && !level.isClientSide()) {
            AudioEngine engine = CreateAudio.audioEngine;

            audioSource = new SynthesizerAudioSource(engine.allocateUID());
            engine.submitTask((graph) -> graph.addSource(audioSource));
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        playing = speed >= 64;
    }
}
