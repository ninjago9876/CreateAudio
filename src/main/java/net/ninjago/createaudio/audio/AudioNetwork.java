package net.ninjago.createaudio.audio;

import java.util.ArrayList;
import java.util.List;

public class AudioNetwork {
    private final List<AudioNode> nodes = new ArrayList<>();

    public AudioNetwork(AudioNode startNode) {
        nodes.add(startNode);
    }

    public void addNode(AudioNode node) {
        nodes.addLast(node);
    }

    public void tickNetwork(long currentFrame) {
        for (AudioNode node : nodes) {
            node.process(currentFrame);
        }
        for (AudioNode node : nodes) {
            node.exposeOutput();
        }
    }
}
