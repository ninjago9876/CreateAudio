package net.ninjago.createaudio.foundation;

import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.tasks.ConnectNodesTask;
import net.ninjago.createaudio.audio.tasks.MergeGraphTask;
import net.ninjago.createaudio.audio.tasks.RemoveGroupTask;
import net.ninjago.createaudio.audio.utility.NodeGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ModularNetworkHandler {
    protected final AudioEngine engine;
    private final ModularNetworkGraphSupplier graphSupplier;

    public record ModularNetworkSocket(int nodeUid, int nodeSocketIndex) { }

    private final ArrayList<ModularNetworkSocket> inputs = new ArrayList<>();
    private final ArrayList<ModularNetworkSocket> outputs = new ArrayList<>();

    private final HashMap<String, AtomicReference<Object>> references = new HashMap<>();

    private final String group;
    private final int networkUid;

    public ModularNetworkHandler(
            AudioEngine engine,
            ModularNetworkGraphSupplier graphSupplier,
            String name
    ) {
        this.engine = engine;
        this.graphSupplier = graphSupplier;

        NodeGraph graph = graphSupplier.get(engine, inputs, outputs, references);
        networkUid = graph.getUid();

        this.group = String.format("%1$s#%2$d", name, graph.getUid());
        graph.setGroup(group);

        engine.enqueueTask(new MergeGraphTask(graph));
    }

    public AtomicReference<Object> getReference(String key) {
        return references.get(key);
    }

    public void remove() {
        engine.enqueueTask(new RemoveGroupTask(group));
    }

    public void connect(ModularNetworkHandler fromNetworkHandler, int fromOutputIndex, int toInputIndex) {
        ModularNetworkSocket fromOutputSocket = fromNetworkHandler.outputs.get(fromOutputIndex);
        ModularNetworkSocket toInputSocket = inputs.get(toInputIndex);
        engine.enqueueTask(new ConnectNodesTask(fromOutputSocket.nodeUid, fromOutputSocket.nodeSocketIndex, toInputSocket.nodeUid, toInputSocket.nodeSocketIndex));
    }

    @FunctionalInterface
    public interface ModularNetworkGraphSupplier {
        NodeGraph get(
                AudioEngine engine,
                ArrayList<ModularNetworkHandler.ModularNetworkSocket> inputs,
                ArrayList<ModularNetworkHandler.ModularNetworkSocket> outputs,
                HashMap<String, AtomicReference<Object>> references
        );
    }
}