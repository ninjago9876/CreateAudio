package net.ninjago.createaudio.content.mechanicalRecordPlayer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.nodes.SynthesizerNode;
import net.ninjago.createaudio.foundation.ModularNetworkHandler;

public class RecordPlayerBlockEntity extends KineticBlockEntity {
    protected String audioFile = "";
    protected boolean playing = false;

    private AudioEngine engine;

    private ModularNetworkHandler networkHandler;

    public RecordPlayerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
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
            net.addNode(new SynthesizerNode(engine));
            return net;
        }, "record_player");
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
        playing = speed >= 64;
    }
}
