package net.ninjago.createaudio.audio.utility;

import net.ninjago.createaudio.audio.sink.AudioSink;
import net.ninjago.createaudio.audio.source.AudioSource;

import java.util.HashMap;
import java.util.Map;

public class ProcessorGraph {
    public record Connection(int sinkUid, int sourceUid) { }

    private final Map<Integer, AudioSource> sources;
    private final Map<Integer, AudioSink> sinks;

    private final Map<Integer, Connection> connections; // SinkUID -> Connection

    public ProcessorGraph() {
        sources = new HashMap<>();
        sinks = new HashMap<>();
        connections = new HashMap<>();
    }

    public ProcessorGraph(ProcessorGraph otherGraph) {
        sources = new HashMap<>(otherGraph.sources);
        sinks = new HashMap<>(otherGraph.sinks);
        connections = new HashMap<>(otherGraph.connections);
    }

    public ProcessorGraph addSource(AudioSource source) {
        ProcessorGraph graphClone = new ProcessorGraph(this);
        graphClone.sources.put(source.uid, source);
        return graphClone;
    }

    public ProcessorGraph removeSource(int sourceUid) {
        ProcessorGraph graphClone = new ProcessorGraph(this);
        graphClone.sources.remove(sourceUid);
        graphClone.connections.values()
                .removeIf(c -> c.sourceUid() == sourceUid);
        return graphClone;
    }

    public ProcessorGraph addSink(AudioSink sink) {
        ProcessorGraph graphClone = new ProcessorGraph(this);
        graphClone.sinks.put(sink.uid, sink);
        return graphClone;
    }

    public ProcessorGraph removeSink(int sinkUid) {
        ProcessorGraph graphClone = new ProcessorGraph(this);
        graphClone.sinks.remove(sinkUid);
        graphClone.connections.remove(sinkUid);
        return graphClone;
    }

    public ProcessorGraph connect(int sourceUid, int sinkUid) {
        if (!sources.containsKey(sourceUid) || !sinks.containsKey(sinkUid)) {
            return this; // or throw
        }

        ProcessorGraph graphClone = new ProcessorGraph(this);
        graphClone.connections.put(sinkUid, new Connection(sinkUid, sourceUid));
        return graphClone;
    }

    public void tick(long currentFrame) {
        for (AudioSource source : sources.values()) {
            source.tick(currentFrame);
        }

        for (AudioSource source : sources.values()) {
            source.pushBackBuffer();
        }

        for (Connection connection : connections.values()) {
            AudioSink sink = sinks.get(connection.sinkUid());
            AudioSource source = sources.get(connection.sourceUid());
            sink.tick(source.getFrontBuffer(), currentFrame);
        }
    }
}
