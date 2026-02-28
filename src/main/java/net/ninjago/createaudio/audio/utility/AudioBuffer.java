package net.ninjago.createaudio.audio.utility;

public class AudioBuffer {
    private float[] frame;

    public void push(float[] frame) {
        this.frame = frame.clone();
    }

    public float[] get() {
        return this.frame.clone();
    }

    public short[] getPCM() {
        if (frame == null) {
            return new short[0];
        }

        short[] pcm = new short[frame.length];

        for (int i = 0; i < frame.length; i++) {
            float sample = frame[i];

            // Clamp just in case
            if (sample > 1.0f) sample = 1.0f;
            if (sample < -1.0f) sample = -1.0f;

            pcm[i] = (short) (sample * 32767f);
        }

        return pcm;
    }
}
