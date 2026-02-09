package net.ninjago.createaudio.audio;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public abstract class AudioNode {
    private final List<AudioOutput> nextOutputs = new ArrayList<>();
    private final List<AudioOutput> outputs = new ArrayList<>();

    private String group = "";

    private AudioNetwork parentNetwork;

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
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

    public void exposeOutput() {
        int index = 0;
        for (AudioOutput output : nextOutputs) {
            outputs.get(index).setFrame(output.getAudioFrame());
            index++;
        }
    }

    public void attachInput(AudioOutput output, int index) throws IndexOutOfBoundsException {
        throw new IndexOutOfBoundsException();
    }

    public abstract void process(long currentFrame);
}
