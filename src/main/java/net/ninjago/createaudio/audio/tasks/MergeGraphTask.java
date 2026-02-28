package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.audio.nodes.AudioNode;
import net.ninjago.createaudio.audio.utility.NodeGraph;

import java.util.function.Supplier;

public class MergeGraphTask extends NodeGraphTask {
    private final NodeGraph nodeGraph;

    public MergeGraphTask(NodeGraph nodeGraph) {
        this.nodeGraph = nodeGraph;
    }

    @Override
    protected NodeGraph modify(NodeGraph input) {
        input.addNodes(nodeGraph.getNodes());
        return input;
    }
}
