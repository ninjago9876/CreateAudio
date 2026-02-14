package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.audio.AudioNetwork;

import java.util.HashMap;

public class AddNetworkTask extends AudioNetworkTask {
    private final AudioNetwork network;

    public AddNetworkTask(AudioNetwork network) {
        this.network = network;
    }

    @Override
    protected HashMap<Integer, AudioNetwork> modify(HashMap<Integer, AudioNetwork> input) {
        input.put(network.getUid(), network);
        return input;
    }
}
