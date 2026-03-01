package net.ninjago.createaudio.audio.sink;

import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.utility.AudioBuffer;

import java.util.Arrays;

public class DebugLogAudioSink extends AudioSink {
    public DebugLogAudioSink(int uid) {
        super(uid);
    }

    @Override
    public void tick(AudioBuffer input, long currentFrame) {
        CreateAudio.LOGGER.info(Arrays.toString(input.get()));
    }
}
