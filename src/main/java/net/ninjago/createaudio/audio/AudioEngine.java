package net.ninjago.createaudio.audio;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.tasks.AudioNetworkTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioEngine implements Runnable {

    private final Thread thread;
    private volatile boolean running = true;

    public static final int FRAME_SIZE = 480;
    public static final int SAMPLING_RATE = 48_000;
    public static final long FRAME_DURATION_NS = (long) ((1000_000_000f / SAMPLING_RATE) * FRAME_SIZE);

    private volatile HashMap<Integer, AudioNetwork> processingNetworks = new HashMap<>();

    private volatile AudioNetworkTask currentTask;
    private final ConcurrentLinkedQueue<AudioNetworkTask> taskQueue = new ConcurrentLinkedQueue<>();

    private int uidCounter = 0;

    public int allocateUID() {
        uidCounter++;
        return uidCounter;
    }

    public AudioEngine() {
        thread = new Thread(this);
    }

    public void enqueueTask(AudioNetworkTask task) {
        taskQueue.add(task);
    }

    public void start() {
        thread.setName("Audio Process Thread");
        thread.start();
    }

    public void shutdown() {
        running = false;
    }

//    @Override
//    public void run() {
//        long targetFrameTime = (long) ((1000_000_000f / SAMPLING_RATE) * FRAME_SIZE);
//        CreateAudio.LOGGER.info(String.valueOf(targetFrameTime));
//
//        final int ERROR_HISTORY_SIZE = 100;
//        long[] frameErrors = new long[ERROR_HISTORY_SIZE]; // in nanoseconds
//        int errorIndex = 0;
//        long framesCounted = 0;
//
//        long nextFrameTime = System.nanoTime();
//
//        while (running) {
//            long now = System.nanoTime();
//
//            // Calculate how early/late we are
//            long frameError = now - nextFrameTime;
//
//            // --- Track error for averaging ---
//            frameErrors[errorIndex] = frameError;
//            errorIndex = (errorIndex + 1) % ERROR_HISTORY_SIZE;
//            framesCounted++;
//
//            // If we have 100 frames, log average error
//            if (framesCounted % ERROR_HISTORY_SIZE == 0) {
//                long sum = 0;
//                for (long e : frameErrors) sum += e;
//                long averageError = sum / ERROR_HISTORY_SIZE;
//                this.log(averageError); // your logging method
//            }
//
//            // Sleep if ahead of schedule
//            if (frameError < 0) { // negative = we are early
//                long sleepNanos = -frameError;
//                try {
//                    if (sleepNanos > 1_000_000) {
//                        Thread.sleep(sleepNanos / 1_000_000, (int)(sleepNanos % 1_000_000));
//                    }
//                    while (System.nanoTime() < nextFrameTime) {
//                        // spin for the remaining few ns
//                    }
//                } catch (InterruptedException ignored) {}
//            } else {
//                // We are late, optionally catch up
//                nextFrameTime = now;
//            }
//
//            // --- Process one audio frame ---
//            processAudioFrame(framesCounted);
//
//            // Schedule next frame
//            nextFrameTime += targetFrameTime;
//        }
//    }

    @Override
    public void run() {
        long framePosition = 0;
        long startTime = System.nanoTime();

        while (running) {
            framePosition++;
            processAudioFrame(framePosition);
            long waitTimestamp = startTime + framePosition * FRAME_DURATION_NS;

//            CreateAudio.LOGGER.info(String.valueOf(framePosition));
            CreateAudio.LOGGER.info(processingNetworks.toString());

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
        if (currentTask != null) {
            if (currentTask.isFinished()) {
                processingNetworks = currentTask.getResult();
                currentTask = null;
            }
        }
        if (!taskQueue.isEmpty() & currentTask == null) {
            currentTask = taskQueue.poll();
            currentTask.start(processingNetworks);
        }

        for (AudioNetwork network : processingNetworks.values()) {
            network.tickNetwork(currentFrame);
        }
    }
}
