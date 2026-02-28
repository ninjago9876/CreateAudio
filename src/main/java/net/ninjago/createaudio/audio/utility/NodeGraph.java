package net.ninjago.createaudio.audio.utility;

import net.ninjago.createaudio.audio.nodes.AudioNode;
import net.ninjago.createaudio.audio.tasks.NodeGraphTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeGraph {
    private final Map<Integer, AudioNode> nodes;

    private final int uid;

    public NodeGraph(NodeGraph input) {
        nodes = new HashMap<>(input.nodes);
        uid = input.uid;
    }

    public NodeGraph(Integer uid) {
        nodes = new HashMap<>();
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void addNode(AudioNode node) {
        nodes.put(node.getUid(), node);
    }

    public void addNodes(Map<Integer, AudioNode> nodes) {
        this.nodes.putAll(nodes);
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
}
