package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.audio.AudioNetwork;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AudioNetworkTask implements Runnable {
    private List<AudioNetwork> result;
    private List<AudioNetwork> currentNetworks;

    private boolean finished = false;

    private Thread thread;

    protected abstract AudioNetwork modifyNetwork(AudioNetwork network, int index);

    public @Nullable List<AudioNetwork> getResult() {
        return result;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        result = new ArrayList<>();
        int index = 0;
        for (AudioNetwork network : currentNetworks) {
            result.add(modifyNetwork(network, index));
            index++;
        }
        finished = true;
    }

    public void start(List<AudioNetwork> currentNetworks) {
        thread = new Thread(this);
        this.currentNetworks = currentNetworks;
        thread.start();
    }
}
