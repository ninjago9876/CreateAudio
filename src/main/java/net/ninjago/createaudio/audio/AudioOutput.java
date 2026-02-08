package net.ninjago.createaudio.audio;

public class AudioOutput {
    private float[] audioFrame;

    public AudioOutput(float[] frame) {
        audioFrame = frame;
    }

    public float[] getAudioFrame() {
        return audioFrame;
    }

    public void setFrame(float[] newFrame) {
        audioFrame = newFrame;
    }
}
