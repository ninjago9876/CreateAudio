package net.ninjago.createaudio.audio.source;

import net.ninjago.createaudio.audio.AudioEngine;

public class SynthesizerAudioSource extends AudioSource {
    public SynthesizerAudioSource(int uid) {
        super(uid);
    }

    @Override
    public void tick(long currentFrame) {
        float[] frame = new float[AudioEngine.FRAME_SIZE];

        for (int i = 0; i < AudioEngine.FRAME_SIZE; i++) {
            long sampleT = i + AudioEngine.FRAME_SIZE * currentFrame;
            float timeSeconds = (float)sampleT / AudioEngine.SAMPLING_RATE;

            frame[i] = (float) Math.sin(timeSeconds * 2 * Math.PI * 10000);
        }

        buffer.push(frame);
    }
}
