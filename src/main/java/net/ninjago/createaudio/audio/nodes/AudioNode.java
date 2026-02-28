package net.ninjago.createaudio.audio.nodes;

import net.ninjago.createaudio.audio.utility.AudioBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AudioNode {
    private final List<AudioBuffer> cachedOutputs = new ArrayList<>();
    private final List<AudioBuffer> exposedOutputs = new ArrayList<>();

    private final Map<Integer, AudioBuffer> inputs = new HashMap<>();

    private String group = "";

    private final int uid;

    public AudioNode(int uid) {
        this.uid = uid;
    }

    protected AudioNode(AudioNode otherNode) {
        cachedOutputs.addAll(otherNode.cachedOutputs);
        exposedOutputs.addAll(otherNode.exposedOutputs);
        inputs.putAll(otherNode.inputs);
        group = otherNode.group;
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

    public AudioBuffer getOutput(int index) throws IndexOutOfBoundsException {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (index >= exposedOutputs.size()) {
            throw new IndexOutOfBoundsException();
        }
        return exposedOutputs.get(index);
    }

    protected void pushAudioFrame(float[] frame, int index) {
        cachedOutputs.get(index).push(frame);
    }

    protected void addOutput() {
        exposedOutputs.add(new AudioBuffer());
        cachedOutputs.add(new AudioBuffer());
    }

    public void attachInput(AudioBuffer buffer, int index) {
        inputs.put(index, buffer);
    }

    protected AudioBuffer getInput(int index) {
        return inputs.get(index);
    }

    public void pushOutputCache() {
        int index = 0;
        for (AudioBuffer output : cachedOutputs) {
            exposedOutputs.get(index).push(output.get());
            index++;
        }
    }

    public abstract void process(long currentFrame);

    public abstract AudioNode clone();
}
