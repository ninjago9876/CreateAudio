package net.ninjago.createaudio.audio.sink;

import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import net.ninjago.createaudio.audio.utility.AudioBuffer;
import net.ninjago.createaudio.voicechat.SimpleVoiceChatPlugin;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SVCAudioSink extends AudioSink {
    public record SVCHook(AudioChannel channel, OpusEncoder encoder) {}

    private volatile SVCHook hook;

    private float[] buffer = new float[SimpleVoiceChatPlugin.OPUS_FRAME_SIZE * 4];
    private int bufferSize = 0; // how many valid samples are in buffer

    public SVCAudioSink(int uid, CompletableFuture<SVCHook> hookFuture) {
        super(uid);
        hookFuture.thenAccept(hook -> this.hook = hook);
    }

    @Override
    public void tick(AudioBuffer input, long currentFrame) {
        SVCHook h = hook;
        if (h == null) {
            return;
        }

        float[] samples = input.get();
        ensureCapacity(bufferSize + samples.length);

        // Append incoming samples
        System.arraycopy(samples, 0, buffer, bufferSize, samples.length);
        bufferSize += samples.length;

        int frameSize = SimpleVoiceChatPlugin.OPUS_FRAME_SIZE;

        // Process while enough samples are available
        while (bufferSize >= frameSize) {
            float[] svcFrame = new float[frameSize];

            // Copy frame out
            System.arraycopy(buffer, 0, svcFrame, 0, frameSize);

            // Shift remaining samples left
            System.arraycopy(buffer, frameSize, buffer, 0, bufferSize - frameSize);
            bufferSize -= frameSize;

            hook.channel.send(hook.encoder.encode(AudioBuffer.getPCM(svcFrame)));
        }
    }

    private void ensureCapacity(int requiredCapacity) {
        if (requiredCapacity <= buffer.length) {
            return;
        }

        int newSize = Math.max(requiredCapacity, buffer.length * 2);
        buffer = Arrays.copyOf(buffer, newSize);
    }
}
