package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.audio.utility.NodeGraph;

public class ConnectNodesTask extends NodeGraphTask {
    private final int fromNodeUid;
    private final int fromNodeOutputIndex;

    private final int toNodeUid;
    private final int toNodeOutputIndex;

    public ConnectNodesTask(int fromNodeUid, int fromNodeOutputIndex, int toNodeUid, int toNodeOutputIndex) {
        this.fromNodeUid = fromNodeUid;
        this.fromNodeOutputIndex = fromNodeOutputIndex;
        this.toNodeUid = toNodeUid;
        this.toNodeOutputIndex = toNodeOutputIndex;
    }

    @Override
    protected NodeGraph modify(NodeGraph input) {
        input.getNode(toNodeUid).attachInput(input.getNode(fromNodeUid).getOutput(fromNodeOutputIndex), toNodeOutputIndex);
        return input;
    }
}
