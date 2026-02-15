package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioNetwork;

import java.util.HashMap;

public class MergeNetworkTask extends AudioNetworkTask {
    private final int networkUid;
    private final int intoNetworkUid;

    public MergeNetworkTask(int network, int intoNetwork) {
        this.networkUid = network;
        this.intoNetworkUid = intoNetwork;
    }

    @Override
    protected HashMap<Integer, AudioNetwork> modify(HashMap<Integer, AudioNetwork> input) {
        CreateAudio.LOGGER.info("Merging networks!");

        if (networkUid == intoNetworkUid) return input;

        AudioNetwork network = input.remove(networkUid);
        input.get(intoNetworkUid).addNodes(network.getNodes());

        return input;
    }
}
