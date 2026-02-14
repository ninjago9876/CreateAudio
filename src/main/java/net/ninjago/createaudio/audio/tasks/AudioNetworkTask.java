package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.audio.AudioNetwork;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class AudioNetworkTask implements Runnable {
    private HashMap<Integer, AudioNetwork> result;
    private HashMap<Integer, AudioNetwork> input;

    private volatile boolean finished = false;

    private Thread thread;

    protected abstract HashMap<Integer, AudioNetwork> modify(HashMap<Integer, AudioNetwork> input);

    public @Nullable HashMap<Integer, AudioNetwork> getResult() {
        return result;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        HashMap<Integer, AudioNetwork> clonedInput = new HashMap<>();
        for (Map.Entry<Integer, AudioNetwork> entry : input.entrySet()) {
            clonedInput.put(entry.getKey(), entry.getValue().clone());
        }

        result = modify(clonedInput);
        finished = true;
    }

    public void start(HashMap<Integer, AudioNetwork> input) {
        thread = new Thread(this);
        this.input = input;
        thread.start();
    }
}
