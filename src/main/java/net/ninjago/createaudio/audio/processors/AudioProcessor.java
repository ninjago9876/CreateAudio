package net.ninjago.createaudio.audio.processors;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.tasks.NodeGraphTask;
import net.ninjago.createaudio.audio.utility.FlatNodeGraph;
import net.ninjago.createaudio.audio.utility.NodeGraph;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioProcessor implements Runnable, IAudioProcessor {
    private final Thread thread;
    private volatile boolean running = true;

    private FlatNodeGraph processingGraph;
    private NodeGraph taskGraph;

    private NodeGraphTask currentTask;
    private final ConcurrentLinkedQueue<NodeGraphTask> tasks = new ConcurrentLinkedQueue<>();

    private ExecutorService taskWorker = Executors.newSingleThreadExecutor();

    public AudioProcessor(int graphUid) {
        thread = new Thread(this);
        taskGraph = new NodeGraph(graphUid);
        processingGraph = new FlatNodeGraph(taskGraph);
    }

    public void start() {
        thread.setName("Audio Process Thread");
        thread.start();
    }

    @Override
    public void enqueueTask(NodeGraphTask task) {
        tasks.add(task);
    }

    @Override
    public void stop() {
        running = false;
        taskWorker.shutdownNow();
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
        processingGraph.tick(currentFrame);

        if (currentTask != null) {
            if (currentTask.isFinished()) {
                processingGraph = currentTask.getFlatResult();
                taskGraph = currentTask.getResult();
                currentTask = null;
            }
        }
        if (currentTask == null) {
            NodeGraphTask next = tasks.poll();
            if (next != null) {
                currentTask = next;
                NodeGraph base = taskGraph;
                taskWorker.submit(() -> next.start(base));
            }
        }
    }
}
