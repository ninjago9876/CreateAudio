package net.ninjago.createaudio.audio.utility;

import net.ninjago.createaudio.audio.AudioEngine;

public class AudioBuffer {
    private float[] frame;

    public void push(float[] frame) {
        this.frame = frame.clone();
    }

    public float[] get() {
        return this.frame.clone();
    }

    public short[] getPCM() {
        if (this.frame == null) {
            return new short[AudioEngine.FRAME_SIZE];
        }

        short[] pcm = new short[this.frame.length];

        for (int i = 0; i < this.frame.length; i++) {
            // Clamp to [-1.0, 1.0] to avoid overflow
            float sample = Math.max(-1.0f, Math.min(1.0f, this.frame[i]));

            // Convert to 16-bit signed PCM
            pcm[i] = (short) (sample * Short.MAX_VALUE);
        }

        return pcm;
    }
}
