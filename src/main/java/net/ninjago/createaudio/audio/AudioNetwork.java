package net.ninjago.createaudio.audio;

import java.util.ArrayList;
import java.util.List;

public class AudioNetwork {
    private final List<AudioNode> nodes = new ArrayList<>();

    public AudioNetwork(AudioNode startNode) {
        nodes.add(startNode);
    }

    public AudioNetwork() { }

    public void addNode(AudioNode node) {
        if (node.getParentNetwork() != null) {
            nodes.addAll(node.getParentNetwork().nodes);
            return;
        }
        nodes.addLast(node);
    }

    public <T extends AudioNode> List<T> getNodes(Class<T> type) {
        List<T> matchingNodes = new ArrayList<>();
        for (AudioNode node : nodes) {
            if (node.getClass().equals(type)) {
                matchingNodes.add((T) node);
            }
        }
        return matchingNodes;
    }

    public List<AudioNode> getNodes() {
        return nodes.stream().toList();
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
