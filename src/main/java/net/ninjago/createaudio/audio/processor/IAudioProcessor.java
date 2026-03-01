package net.ninjago.createaudio.audio.processor;

import net.ninjago.createaudio.audio.utility.ProcessorGraph;

import java.util.function.UnaryOperator;

public interface IAudioProcessor {
     void start();
     void stop();

     void submitTask(UnaryOperator<ProcessorGraph> task);
}
