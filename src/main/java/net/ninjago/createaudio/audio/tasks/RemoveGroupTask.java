package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.audio.nodes.AudioNode;
import net.ninjago.createaudio.audio.utility.NodeGraph;

public class RemoveGroupTask extends NodeGraphTask {
    private final String group;

    public RemoveGroupTask(String group) {
        this.group = group;
    }

    @Override
    protected NodeGraph modify(NodeGraph input) {
        for (int uid : input.getNodes().keySet()) {
            if (input.getNode(uid).getGroup().equals(group)) {
                input.removeNode(uid);
            }
        }
        return input;
    }
}
