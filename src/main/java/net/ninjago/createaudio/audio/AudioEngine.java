package net.ninjago.createaudio.audio;

import net.ninjago.createaudio.audio.processor.IAudioProcessor;
import net.ninjago.createaudio.audio.processor.AudioProcessor;

public class AudioEngine {
    private final IAudioProcessor audioProcessor;

    public static final int FRAME_SIZE = 480;
    public static final int SAMPLING_RATE = 48_000;
    public static final long FRAME_DURATION_NS = (long) ((1000_000_000f / SAMPLING_RATE) * FRAME_SIZE);

    public AudioEngine() {
        audioProcessor = new AudioProcessor(allocateUID());
    }

    public void start() {
        audioProcessor.start();
    }

    public void shutdown() {
        audioProcessor.stop();
    }

    private int uidCounter = 0;

    public int allocateUID() {
        uidCounter++;
        return uidCounter;
    }
}
