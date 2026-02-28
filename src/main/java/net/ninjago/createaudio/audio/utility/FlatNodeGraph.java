package net.ninjago.createaudio.audio.utility;

import net.ninjago.createaudio.audio.nodes.AudioNode;

public class FlatNodeGraph {
    private final AudioNode[] nodes;

    public FlatNodeGraph(AudioNode[] nodes) {
        this.nodes = nodes;
    }

    public FlatNodeGraph(NodeGraph nodeGraph) {
        var values = nodeGraph.getNodes().values();
        AudioNode[] cloned = new AudioNode[values.size()];

        int i = 0;
        for (AudioNode node : values) {
            cloned[i++] = node.clone(); // defensive deep copy
        }

        this.nodes = cloned;
    }

    public AudioNode[] getNodes() {
        return nodes;
    }

    public void tick(long currentFrame) {
        for (AudioNode node : nodes) {
            node.process(currentFrame);
        }
        for (AudioNode node : nodes) {
            node.pushOutputCache();
        }
    }
}
