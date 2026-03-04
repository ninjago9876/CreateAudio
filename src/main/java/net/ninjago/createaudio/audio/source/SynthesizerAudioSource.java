package net.ninjago.createaudio.audio.source;

import net.ninjago.createaudio.audio.AudioEngine;

public class SynthesizerAudioSource extends AudioSource {
    public SynthesizerAudioSource(int uid) {
        super(uid);
    }

    @Override
    public void tick(long currentFrame) {
        float[] frame = new float[AudioEngine.FRAME_SIZE];

        for (int t = 0; t < AudioEngine.FRAME_SIZE; t++) {
            long sampleT = t + AudioEngine.FRAME_SIZE * currentFrame;
            float timeSeconds = (float)sampleT / AudioEngine.SAMPLING_RATE;

            frame[t] = (float) Math.sin(timeSeconds * 2 * Math.PI * 1000);
        }

        buffer.push(frame);
    }
}
