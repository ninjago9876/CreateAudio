package net.ninjago.createaudio.content.mechanicalSpeaker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.nodes.DebugLogNode;
import net.ninjago.createaudio.audio.utility.AudioInputLocation;
import net.ninjago.createaudio.foundation.blockentity.KineticAudioBlockEntity;

public class MechanicalSpeakerBlockEntity extends KineticAudioBlockEntity {
    public MechanicalSpeakerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state, (engine, inputs, outputs) -> {
            AudioNetwork net = new AudioNetwork(engine);
            DebugLogNode debugLogNode = new DebugLogNode(engine);
            inputs.put("0", new AudioInputLocation(net.getUid(), debugLogNode.getUid(), 0));
            net.addNode(debugLogNode);
            return net;
        }, "mechanical_speaker");
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
//        playing = speed >= 64;
    }
}
