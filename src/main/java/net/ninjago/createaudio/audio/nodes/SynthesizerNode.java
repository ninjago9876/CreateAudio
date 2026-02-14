package net.ninjago.createaudio.audio.nodes;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.AudioEngineRegistry;
import net.ninjago.createaudio.audio.AudioNode;
import net.ninjago.createaudio.audio.AudioOutput;
import net.ninjago.createaudio.voicechat.SimpleVoiceChatPlugin;

public class SynthesizerNode extends AudioNode {

    public SynthesizerNode(AudioEngine engine) {
        super(engine);
        addOutput(new AudioOutput(new float[AudioEngine.FRAME_SIZE]));
    }

    protected SynthesizerNode(AudioNode node) {
        super(node);
    }

    @Override
    public void process(long currentFrame) {
        float[] frame = new float[AudioEngine.FRAME_SIZE];

        for (int i = 0; i < AudioEngine.FRAME_SIZE; i++) {
            long sampleT = i + AudioEngine.FRAME_SIZE * currentFrame;
            float timeSeconds = (float)sampleT / AudioEngine.SAMPLING_RATE;

            frame[i] = (float) Math.sin(timeSeconds * 2 * Math.PI * 10000);
        }

        pushAudioFrame(frame, 0);
    }

    @Override
    public AudioNode clone() {
        return new SynthesizerNode(this);
    }
}
