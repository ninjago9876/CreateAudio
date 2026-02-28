package net.ninjago.createaudio.content.mechanicalRecordPlayer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.audio.nodes.SynthesizerNode;
import net.ninjago.createaudio.audio.utility.NodeGraph;
import net.ninjago.createaudio.foundation.ModularNetworkHandler;
import net.ninjago.createaudio.foundation.blockentity.KineticAudioBlockEntity;

public class RecordPlayerBlockEntity extends KineticAudioBlockEntity {
    protected String audioFile = "";
    protected boolean playing = false;

    public RecordPlayerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state, (engine, inputs, outputs, references) -> {
            NodeGraph graph = new NodeGraph(engine.allocateUID());
            SynthesizerNode synthesizerNode = new SynthesizerNode(engine.allocateUID());

            outputs.add(new ModularNetworkHandler.ModularNetworkSocket(synthesizerNode.getUid(), 0)); // Output: 0

            graph.addNode(synthesizerNode);
            return graph;
        }, "record_player");
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        playing = speed >= 64;
    }
}
