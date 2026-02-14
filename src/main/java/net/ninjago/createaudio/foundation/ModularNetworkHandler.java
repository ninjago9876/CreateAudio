package net.ninjago.createaudio.foundation;

import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.AudioNode;
import net.ninjago.createaudio.audio.tasks.AddNetworkTask;
import net.ninjago.createaudio.audio.tasks.AudioNetworkTask;
import net.ninjago.createaudio.audio.tasks.RemoveGroupTask;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class ModularNetworkHandler {
    protected final AudioEngine engine;
    private final Supplier<AudioNetwork> networkSupplier;

    private final String group;

    public ModularNetworkHandler(AudioEngine engine, Supplier<AudioNetwork> networkSupplier, String name) {
        this.engine = engine;
        this.networkSupplier = networkSupplier;

        AudioNetwork network = networkSupplier.get();

        this.group = String.format("%1$s#%2$d", name, network.getUid());
        network.setGroup(group);

        engine.enqueueTask(new AddNetworkTask(network));
    }

    public void remove() {
        engine.enqueueTask(new RemoveGroupTask(group));
    }
}
