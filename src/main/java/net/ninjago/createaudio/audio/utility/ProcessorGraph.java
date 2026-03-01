package net.ninjago.createaudio.audio.utility;

import net.ninjago.createaudio.audio.sink.AudioSink;
import net.ninjago.createaudio.audio.source.AudioSource;

import java.util.HashMap;
import java.util.Map;

public class ProcessorGraph {
    public record Connection(int sinkUid, int sourceUid) { }

    private final Map<Integer, AudioSource> sources = new HashMap<>();
    private final Map<Integer, AudioSink> sinks = new HashMap<>();

    private final Map<Integer, Connection> connections = new HashMap<>(); // SinkUID -> Connection

    public void addSource(AudioSource source) {
        sources.put(source.uid, source);
    }

    public void removeSource(int sourceUid) {
        sources.remove(sourceUid);
    }

    public void addSink(AudioSink sink) {
        sinks.put(sink.uid, sink);
    }

    public void removeSink(int sinkUid) {
        sinks.remove(sinkUid);
    }

    public void connect(int sourceUid, int sinkUid) {
        connections.put(sinkUid, new Connection(sinkUid, sourceUid));
    }

    public void tick(long currentFrame) {
        for (AudioSource source : sources.values()) {
            source.tick(currentFrame);
        }

        for (AudioSource source : sources.values()) {
            source.pushBackBuffer();
        }

        for (int sinkUid : connections.keySet()) {
            AudioSink sink = sinks.get(sinkUid);
            sink.tick(sources.get(connections.get(sinkUid).sourceUid).getFrontBuffer(), currentFrame);
        }
    }
}
