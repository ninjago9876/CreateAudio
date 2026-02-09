package net.ninjago.createaudio.audio;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.nodes.DebugLogNode;
import net.ninjago.createaudio.audio.nodes.SynthesizerNode;

import java.util.ArrayList;
import java.util.List;

public class AudioEngine implements Runnable {

    private final Thread thread;
    private volatile boolean running = true;

    public static final int FRAME_SIZE = 480;
    public static final int SAMPLING_RATE = 48_000;

    private final List<AudioNetwork> networks = new ArrayList<>();

    public final AudioNetwork testNetwork = new AudioNetwork();

    public AudioEngine() {
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void shutdown() {
        running = false;
    }

    public void addNetwork(AudioNetwork network) {
        networks.add(network);
    }

    @Override
    public void run() {
        networks.addLast(testNetwork);

        long targetFrameTime = (long) ((1000_000_000f / SAMPLING_RATE) * FRAME_SIZE);
        CreateAudio.LOGGER.info(String.valueOf(targetFrameTime));

        final int ERROR_HISTORY_SIZE = 100;
        long[] frameErrors = new long[ERROR_HISTORY_SIZE]; // in nanoseconds
        int errorIndex = 0;
        long framesCounted = 0;

        long nextFrameTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();

            // Calculate how early/late we are
            long frameError = now - nextFrameTime;

            // --- Track error for averaging ---
            frameErrors[errorIndex] = frameError;
            errorIndex = (errorIndex + 1) % ERROR_HISTORY_SIZE;
            framesCounted++;

            // If we have 100 frames, log average error
            if (framesCounted % ERROR_HISTORY_SIZE == 0) {
                long sum = 0;
                for (long e : frameErrors) sum += e;
                long averageError = sum / ERROR_HISTORY_SIZE;
                this.log(averageError); // your logging method
            }

            // Sleep if ahead of schedule
            if (frameError < 0) { // negative = we are early
                long sleepNanos = -frameError;
                try {
                    if (sleepNanos > 1_000_000) {
                        Thread.sleep(sleepNanos / 1_000_000, (int)(sleepNanos % 1_000_000));
                    }
                    while (System.nanoTime() < nextFrameTime) {
                        // spin for the remaining few ns
                    }
                } catch (InterruptedException ignored) {}
            } else {
                // We are late, optionally catch up
                nextFrameTime = now;
            }

            // --- Process one audio frame ---
            processAudioFrame(framesCounted);

            // Schedule next frame
            nextFrameTime += targetFrameTime;
        }
    }

    void log(long averageError) {
        CreateAudio.LOGGER.info(String.valueOf(averageError));
    }

    private void processAudioFrame(long currentFrame) {
        for (AudioNetwork network : networks) {
            network.tickNetwork(currentFrame);
        }
    }
}
