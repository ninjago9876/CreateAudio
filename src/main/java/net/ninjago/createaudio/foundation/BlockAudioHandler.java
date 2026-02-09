package net.ninjago.createaudio.foundation;

import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.AudioNode;

import java.util.List;
import java.util.function.Supplier;

public class BlockAudioHandler {
    protected final AudioEngine engine;
    private final Supplier<AudioNetwork> networkSupplier;

    public BlockAudioHandler(AudioEngine engine, Supplier<AudioNetwork> networkSupplier) {
        this.engine = engine;
        this.networkSupplier = networkSupplier;
    }

    public void initialise() {
        List<AudioNode> nodes = networkSupplier.get().getNodes();
        engine.addNetwork(networkSupplier.get());
    }

    public
}
