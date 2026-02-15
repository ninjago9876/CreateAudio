package net.ninjago.createaudio.audio.nodes;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.AudioNode;
import net.ninjago.createaudio.audio.utility.AudioOutput;

import java.util.Arrays;

public class DebugLogNode extends AudioNode {
    private AudioOutput loggedSignal;

    public DebugLogNode(AudioEngine engine) {
        super(engine);
    }

    protected DebugLogNode(DebugLogNode otherNode) {
        super(otherNode);
        loggedSignal = otherNode.loggedSignal;
    }

    @Override
    public void attachInput(AudioOutput output, int index) throws IndexOutOfBoundsException {
        if (index == 0) {
            loggedSignal = output;
            return;
        }
        super.attachInput(output, index);
    }

    @Override
    public void detachInput(AudioOutput output, int index) {
        if (index == 0) {
            loggedSignal = null;
            return;
        }
        super.detachInput(output, index);
    }

    @Override
    public void process(long currentFrame) {
        if (loggedSignal == null) {
//            CreateAudio.LOGGER.info("{} : No signal to log!", getUid());
            return;
        }
        CreateAudio.LOGGER.info(Arrays.toString(loggedSignal.getAudioFrame()));
    }

    @Override
    public AudioNode clone() {
        return new DebugLogNode(this);
    }
}
