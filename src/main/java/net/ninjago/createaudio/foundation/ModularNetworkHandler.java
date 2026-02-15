package net.ninjago.createaudio.foundation;

import com.mojang.datafixers.util.Function3;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.tasks.*;
import net.ninjago.createaudio.audio.utility.AudioInputLocation;
import net.ninjago.createaudio.audio.utility.AudioOutputLocation;

import java.util.HashMap;
import java.util.Map;

public class ModularNetworkHandler {
    protected final AudioEngine engine;
    private final Function3<AudioEngine, Map<String, AudioInputLocation>, Map<String, AudioOutputLocation>, AudioNetwork> networkSupplier;

    private final String group;

    private Map<String, AudioInputLocation> inputs = new HashMap<>();
    private Map<String, AudioOutputLocation> outputs = new HashMap<>();

    public record ModularNetworkSocket(String id, ModularNetworkHandler networkHandler) { }

    private final Map<String, ModularNetworkSocket> connections = new HashMap<>();

    private int networkUid;

    public ModularNetworkHandler(
            AudioEngine engine,
            Function3<AudioEngine, Map<String, AudioInputLocation>, Map<String, AudioOutputLocation>, AudioNetwork> networkSupplier,
            String name
    ) {
        this.engine = engine;
        this.networkSupplier = networkSupplier;

        AudioNetwork network = networkSupplier.apply(engine, inputs, outputs);
        networkUid = network.getUid();

        this.group = String.format("%1$s#%2$d", name, network.getUid());
        network.setGroup(group);

        engine.enqueueTask(new AddNetworkTask(network));
    }

    public Map<String, ModularNetworkSocket> getConnections() {
        return Map.copyOf(connections);
    }

    public void remove() {
        engine.enqueueTask(new RemoveGroupTask(group));
    }

    protected void createConnection(AudioOutputLocation fromLocation, AudioInputLocation toLocation) {
        engine.enqueueTask(new MergeNetworkTask(networkUid, toLocation.networkUid()));
        networkUid = toLocation.networkUid();
        engine.enqueueTask(new CreateConnectionTask(fromLocation, toLocation));
    }

    protected void removeConnection(AudioOutputLocation fromLocation, AudioInputLocation toLocation) {
        engine.enqueueTask(new RemoveConnectionTask(fromLocation, toLocation));
    }

    public void connectWith(ModularNetworkSocket fromOutput, String toInput) {
        if (connections.containsKey(toInput)) return;
        connections.put(toInput, fromOutput);
        createConnection(fromOutput.networkHandler.outputs.get(fromOutput.id), inputs.get(toInput));
    }

    public void disconnectWith(ModularNetworkSocket fromOutput, String toInput) {
        if (connections.containsKey(toInput)) return;
        connections.put(toInput, fromOutput);
        removeConnection(fromOutput.networkHandler.outputs.get(fromOutput.id), inputs.get(toInput));
    }
}
