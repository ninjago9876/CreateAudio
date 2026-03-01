package net.ninjago.createaudio.audio.sink;

import net.ninjago.createaudio.audio.utility.AudioBuffer;

public abstract class AudioSink {
    public final int uid;

    public AudioSink(int uid) {
        this.uid = uid;
    }

    public abstract void tick(AudioBuffer input, long currentFrame);
}
