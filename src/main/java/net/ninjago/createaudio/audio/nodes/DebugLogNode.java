package net.ninjago.createaudio.audio.nodes;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioNode;
import net.ninjago.createaudio.audio.AudioOutput;

import java.util.Arrays;

public class DebugLogNode extends AudioNode {
    private AudioOutput loggedSignal;

    @Override
    public void attachInput(AudioOutput output, int index) throws IndexOutOfBoundsException {
        if (index == 0) {
            loggedSignal = output;
            return;
        }
        super.attachInput(output, index);
    }

    @Override
    public void process(long currentFrame) {
        CreateAudio.LOGGER.info(Arrays.toString(loggedSignal.getAudioFrame()));
    }
}
