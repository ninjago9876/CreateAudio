package net.ninjago.createaudio.content.mechanicalRecordPlayer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.nodes.SynthesizerNode;

public class RecordPlayerBlockEntity extends KineticBlockEntity {
    protected String audioFile = "";
    protected boolean playing = false;

    private AudioEngine engine;

    public RecordPlayerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        engine = CreateAudio.audioEngine;
        if (engine == null) {
            return;
        }

        SynthesizerNode synthesizerNode = new SynthesizerNode();
        engine.testNetwork.addNode(synthesizerNode);
    }



    @Override
    public void onSpeedChanged(float previousSpeed) {
        playing = speed >= 64;
    }
}
