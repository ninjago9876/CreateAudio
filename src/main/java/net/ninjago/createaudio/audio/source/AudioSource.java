package net.ninjago.createaudio.audio.source;

import net.ninjago.createaudio.audio.utility.AudioBuffer;

public abstract class AudioSource {
    private final AudioBuffer frontBuffer = new AudioBuffer();
    protected final AudioBuffer buffer = new AudioBuffer();

    public final int uid;

    protected AudioSource(int uid) {
        this.uid = uid;
    }

    public AudioBuffer getFrontBuffer() {
        return frontBuffer;
    }

    public void pushBackBuffer() {
        frontBuffer.push(buffer.get());
    }

    public abstract void tick(long currentFrame);
}
