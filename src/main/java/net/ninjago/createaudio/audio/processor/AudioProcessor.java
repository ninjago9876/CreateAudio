package net.ninjago.createaudio.audio.processor;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;

public class AudioProcessor implements Runnable, IAudioProcessor {
    private final Thread thread;
    private volatile boolean running = true;

    public AudioProcessor(int graphUid) {
        thread = new Thread(this);
    }

    public void start() {
        thread.setName("Audio Process Thread");
        thread.start();
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        long framePosition = 0;
        long startTime = System.nanoTime();

        while (running) {
            framePosition++;
            processAudioFrame(framePosition);
            long waitTimestamp = startTime + framePosition * AudioEngine.FRAME_DURATION_NS;

            long waitNanos = waitTimestamp - System.nanoTime();

            try {
                if (waitNanos > 0L) {
                    Thread.sleep(waitNanos / 1_000_000L, (int) (waitNanos % 1_000_000));
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void processAudioFrame(long currentFrame) {
        if ((currentFrame % 100) == 0) { // every 1024 frames
            CreateAudio.LOGGER.info("Audio frame: {}", currentFrame);
        }
    }
}
