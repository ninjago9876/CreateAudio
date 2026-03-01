package net.ninjago.createaudio.audio.processor;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.utility.ProcessorGraph;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

public class AudioProcessor implements Runnable, IAudioProcessor {
    private final Thread thread;
    private volatile boolean running = true;

    private final AtomicReference<ProcessorGraph> processingGraph = new AtomicReference<>(new ProcessorGraph());

    private final ExecutorService taskWorker =
            Executors.newSingleThreadExecutor();

    public AudioProcessor() {
        thread = new Thread(this);
    }

    public void start() {
        thread.setName("Audio Process Thread");
        thread.start();
    }

    @Override
    public void stop() {
        running = false;
        taskWorker.shutdown();
    }

    @Override
    public void submitTask(UnaryOperator<ProcessorGraph> task) {
        taskWorker.submit(() -> {
            ProcessorGraph current = processingGraph.get();
            ProcessorGraph updated = task.apply(current);
            processingGraph.set(updated);
        });
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
        ProcessorGraph currentGraph = processingGraph.get();

        if ((currentFrame % 100) == 0) { // every 1024 frames
            CreateAudio.LOGGER.info("Audio frame: {}", currentFrame);
        }

        currentGraph.tick(currentFrame);
    }
}
