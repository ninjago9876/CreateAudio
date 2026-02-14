package net.ninjago.createaudio.content.mechanicalSpeaker;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.AudioEngineRegistry;
import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.nodes.DebugLogNode;
import net.ninjago.createaudio.audio.nodes.SynthesizerNode;
import net.ninjago.createaudio.foundation.ModularNetworkHandler;

import java.util.List;

public class MechanicalSpeakerBlockEntity extends KineticBlockEntity {
    private AudioEngine engine;

    private ModularNetworkHandler networkHandler;

    public MechanicalSpeakerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (getLevel() != null) {
            if (getLevel().isClientSide()) return;
        }

        engine = CreateAudio.audioEngine;
        if (engine == null) {
            return;
        }

        networkHandler = new ModularNetworkHandler(engine, () -> {
            AudioNetwork net = new AudioNetwork(engine);
            net.addNode(new DebugLogNode(engine));
            return net;
        }, "mechanical_speaker");
    }

    @Override
    public void remove() {
        if (getLevel() != null) {
            if (getLevel().isClientSide()) return;
        }
        super.remove();
        networkHandler.remove();
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
//        playing = speed >= 64;
    }
}
