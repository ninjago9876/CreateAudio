package net.ninjago.createaudio.content.mechanicalSpeaker;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.AudioEngineRegistry;
import net.ninjago.createaudio.audio.nodes.DebugLogNode;
import net.ninjago.createaudio.audio.nodes.SynthesizerNode;

import java.util.List;

public class MechanicalSpeakerBlockEntity extends KineticBlockEntity {
    private AudioEngine engine;

    public MechanicalSpeakerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        engine = CreateAudio.audioEngine;
        if (engine == null) {
            return;
        }

        DebugLogNode logNode = new DebugLogNode();
        engine.testNetwork.addNode(logNode);
        List<SynthesizerNode> synthesizerNodes = engine.testNetwork.getNodes(SynthesizerNode.class);
        if (!synthesizerNodes.isEmpty()) {
            logNode.attachInput(synthesizerNodes.getFirst().getOutput(0), 0);
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
//        playing = speed >= 64;
    }
}
