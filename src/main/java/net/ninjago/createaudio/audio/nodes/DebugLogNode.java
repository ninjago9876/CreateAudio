package net.ninjago.createaudio.audio.nodes;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.utility.AudioBuffer;

import java.util.Arrays;

public class DebugLogNode extends AudioNode {

    public DebugLogNode(int uid) {
        super(uid);
    }

    public DebugLogNode(DebugLogNode otherNode) {
        super(otherNode);
    }

    @Override
    public void process(long currentFrame) {
        AudioBuffer input = getInput(0);
        if (input == null) {
            if (currentFrame % 100 == 0) {
                CreateAudio.LOGGER.info("No signal to log!");
            }
            return;
        }
        CreateAudio.LOGGER.info(Arrays.toString(input.get()));
    }

    @Override
    public AudioNode clone() {
        return new DebugLogNode(this);
    }
}
