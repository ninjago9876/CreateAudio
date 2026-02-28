package net.ninjago.createaudio.audio.processors;

import net.ninjago.createaudio.audio.tasks.NodeGraphTask;
import net.ninjago.createaudio.audio.utility.FlatNodeGraph;

public interface IAudioProcessor {
     void start();
     void stop();

     void enqueueTask(NodeGraphTask task);
}
