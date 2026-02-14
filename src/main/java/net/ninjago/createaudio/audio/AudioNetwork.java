package net.ninjago.createaudio.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AudioNetwork {
    private final AudioEngine engine;

    private final HashMap<Integer, AudioNode> nodes = new HashMap<>();
    private final int uid;

    public AudioNetwork(AudioEngine engine) {
        this.engine = engine;
        this.uid = engine.allocateUID();
    }

    public AudioNetwork(AudioNetwork network) {
        this.engine = network.engine;
        this.uid = network.uid;
    }

    public void addNode(AudioNode node) {
        if (node.getParentNetwork() != null) {
            nodes.putAll(node.getParentNetwork().nodes);
            return;
        }
        nodes.put(node.getUid(), node);
    }

    public <T extends AudioNode> List<T> getNodes(Class<T> type) {
        List<T> matchingNodes = new ArrayList<>();
        for (AudioNode node : nodes.values()) {
            if (node.getClass().equals(type)) {
                matchingNodes.add((T) node);
            }
        }
        return matchingNodes;
    }

    public void removeNode(int uid) {
        nodes.remove(uid);
    }

    public void setGroup(String group) {
        for (AudioNode node : nodes.values()) {
            node.setGroup(group);
        }
    }

    public HashMap<Integer, AudioNode> getNodes() {
        return new HashMap<>(nodes);
    }

    public List<AudioNode> getNodesByGroup(String group) {
        List<AudioNode> matchingNodes = new ArrayList<>();
        for (AudioNode node : nodes.values()) {
            if (node.getGroup().equals(group)) {
                matchingNodes.add(node);
            }
        }
        return matchingNodes;
    }

    public AudioNode getNode(int uid) {
        return nodes.get(uid);
    }

    public void tickNetwork(long currentFrame) {
        for (AudioNode node : nodes.values()) {
            node.process(currentFrame);
        }
        for (AudioNode node : nodes.values()) {
            node.pushOutput();
        }
    }

    public AudioNetwork clone() {
        AudioNetwork networkClone = new AudioNetwork(this);
        for (AudioNode node : nodes.values()) {
            networkClone.nodes.put(node.getUid(), node.clone());
        }
        return networkClone;
    }

    public int getUid() {
        return uid;
    }
}
