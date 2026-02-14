package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.AudioNode;

import java.util.HashMap;

public class RemoveGroupTask extends AudioNetworkTask {
    private final String group;

    public RemoveGroupTask(String group) {
        this.group = group;
    }

    @Override
    protected HashMap<Integer, AudioNetwork> modify(HashMap<Integer, AudioNetwork> input) {
        for (AudioNetwork network : input.values()) {
            for (AudioNode node : network.getNodesByGroup(group)) {
                network.removeNode(node.getUid());
            }
        }
        return input;
    }
}
