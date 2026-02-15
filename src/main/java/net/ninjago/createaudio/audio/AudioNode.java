package net.ninjago.createaudio.audio;

import net.ninjago.createaudio.audio.utility.AudioOutput;

import java.util.ArrayList;
import java.util.List;

public abstract class AudioNode {
    private final AudioEngine engine;

    private final List<AudioOutput> nextOutputs = new ArrayList<>();
    private final List<AudioOutput> outputs = new ArrayList<>();

    private String group = "";

    private AudioNetwork parentNetwork;
    private final int uid;

    public AudioNode(AudioEngine engine) {
        this.engine = engine;
        uid = engine.allocateUID();
    }

    protected AudioNode(AudioNode otherNode) {
        engine = otherNode.engine;
        nextOutputs.addAll(otherNode.nextOutputs);
        outputs.addAll(otherNode.outputs);
        group = otherNode.group;
        parentNetwork = otherNode.parentNetwork;
        uid = otherNode.uid;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public int getUid() {
        return uid;
    }

    public AudioOutput getOutput(int index) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (index >= outputs.size()) {
            throw new IndexOutOfBoundsException();
        }
        return outputs.get(index);
    }

    public void setParentNetwork(AudioNetwork network) {
        parentNetwork = network;
    }

    public AudioNetwork getParentNetwork() {
        return parentNetwork;
    }

    protected void pushAudioFrame(float[] frame, int index) {
        nextOutputs.get(index).setFrame(frame);
    }

    protected void addOutput(AudioOutput output) {
        outputs.add(output);
        nextOutputs.add(output);
    }

    public void pushOutput() {
        int index = 0;
        for (AudioOutput output : nextOutputs) {
            outputs.get(index).setFrame(output.getAudioFrame());
            index++;
        }
    }

    // TODO - Use Map of inputs instead
    public void attachInput(AudioOutput output, int index) throws IndexOutOfBoundsException {
        throw new IndexOutOfBoundsException();
    }

    public void detachInput(AudioOutput output, int index) {
        throw new IndexOutOfBoundsException();
    }

    public abstract void process(long currentFrame);

    public abstract AudioNode clone();
}
