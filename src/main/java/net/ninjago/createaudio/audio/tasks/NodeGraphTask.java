package net.ninjago.createaudio.audio.tasks;

import net.ninjago.createaudio.audio.utility.FlatNodeGraph;
import net.ninjago.createaudio.audio.utility.NodeGraph;
import org.w3c.dom.Node;

import javax.annotation.Nullable;

public abstract class NodeGraphTask implements Runnable {
    private volatile NodeGraph result;
    private volatile FlatNodeGraph flatResult;

    private volatile NodeGraph input;

    private volatile boolean finished = false;

    private Thread thread;

    protected abstract NodeGraph modify(NodeGraph input);

    public @Nullable NodeGraph getResult() {
        return result;
    }

    public @Nullable FlatNodeGraph getFlatResult() {
        return flatResult;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        NodeGraph clonedInput = new NodeGraph(input);

        result = modify(clonedInput);
        flatResult = new FlatNodeGraph(result);
        finished = true;
    }

    public void start(NodeGraph input) {
        thread = new Thread(this);
        this.input = input;
        thread.start();
    }
}
