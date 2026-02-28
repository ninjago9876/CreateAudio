package net.ninjago.createaudio.audio.nodes;

import net.ninjago.createaudio.audio.AudioEngine;

public class SynthesizerNode extends AudioNode {

    public SynthesizerNode(int uid) {
        super(uid);
        addOutput();    // 0
    }

    protected SynthesizerNode(AudioNode otherNode) {
        super(otherNode);
    }

    @Override
    public void process(long currentFrame) {
        float[] frame = new float[AudioEngine.FRAME_SIZE];

        for (int i = 0; i < AudioEngine.FRAME_SIZE; i++) {
            long sampleT = i + AudioEngine.FRAME_SIZE * currentFrame;
            float timeSeconds = (float)sampleT / AudioEngine.SAMPLING_RATE;

            frame[i] = (float) Math.sin(timeSeconds * 2 * Math.PI * 10000);
        }

        pushAudioFrame(frame, 0);
    }

    @Override
    public AudioNode clone() {
        return new SynthesizerNode(this);
    }
}
