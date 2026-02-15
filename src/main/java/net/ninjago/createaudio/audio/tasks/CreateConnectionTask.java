package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.AudioNode;
import net.ninjago.createaudio.audio.utility.AudioInputLocation;
import net.ninjago.createaudio.audio.utility.AudioOutputLocation;

import java.util.HashMap;

public class CreateConnectionTask extends AudioNetworkTask {
    private final AudioInputLocation toLocation;
    private final AudioOutputLocation fromLocation;

    public CreateConnectionTask(AudioOutputLocation fromLocation, AudioInputLocation toLocation) {
        this.toLocation = toLocation;
        this.fromLocation = fromLocation;
    }

    @Override
    protected HashMap<Integer, AudioNetwork> modify(HashMap<Integer, AudioNetwork> input) {
        if (toLocation.networkUid() != fromLocation.networkUid()) {
            CreateAudio.LOGGER.error("Can not create connection between different networks directly");
            return input;
        }
        try {
            AudioNode toNode = input.get(toLocation.networkUid()).getNode(toLocation.nodeUid());
            AudioNode fromNode = input.get(toLocation.networkUid()).getNode(fromLocation.nodeUid());
            toNode.attachInput(fromNode.getOutput(fromLocation.outputIndex()), toLocation.inputIndex());
        } catch (IndexOutOfBoundsException e) {
            CreateAudio.LOGGER.info("Could not route output!");
        }
        return input;
    }
}
