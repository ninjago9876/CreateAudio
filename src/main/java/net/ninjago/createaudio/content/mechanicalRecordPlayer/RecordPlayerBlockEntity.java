package net.ninjago.createaudio.content.mechanicalRecordPlayer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.nodes.SynthesizerNode;
import net.ninjago.createaudio.audio.utility.AudioOutputLocation;
import net.ninjago.createaudio.foundation.blockentity.KineticAudioBlockEntity;

public class RecordPlayerBlockEntity extends KineticAudioBlockEntity {
    protected String audioFile = "";
    protected boolean playing = false;

    public RecordPlayerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state, (engine, inputs, outputs) -> {
            AudioNetwork net = new AudioNetwork(engine);
            SynthesizerNode synthesizerNode = new SynthesizerNode(engine);
            outputs.put("0", new AudioOutputLocation(net.getUid(), synthesizerNode.getUid(), 0));
            net.addNode(synthesizerNode);
            return net;
        }, "record_player");
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        playing = speed >= 64;
    }
}
