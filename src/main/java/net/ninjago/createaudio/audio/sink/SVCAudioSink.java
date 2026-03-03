package net.ninjago.createaudio.audio.sink;

import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.utility.AudioBuffer;
import net.ninjago.createaudio.voicechat.SimpleVoiceChatPlugin;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SVCAudioSink extends AudioSink {
    public record SVCHook(AudioChannel channel, OpusEncoder encoder) {}

    private volatile SVCHook hook;

    private final int OPUS_FRAMES_PER_FRAME = Math.ceilDiv(AudioEngine.FRAME_SIZE, SimpleVoiceChatPlugin.OPUS_FRAME_SIZE);

    public SVCAudioSink(int uid, CompletableFuture<SVCHook> hookFuture) {
        super(uid);
        hookFuture.thenAccept(hook -> this.hook = hook);
    }

    @Override
    public void tick(AudioBuffer input, long currentFrame) {
        SVCHook h = hook;
        if (h == null) return;

        short[] pcm = input.getPCM();

        short[] window = new short[SimpleVoiceChatPlugin.OPUS_FRAME_SIZE];
        for (int i = 0; i < OPUS_FRAMES_PER_FRAME; i++) {
            int length = Math.min(SimpleVoiceChatPlugin.OPUS_FRAME_SIZE, pcm.length - i * SimpleVoiceChatPlugin.OPUS_FRAME_SIZE);
            System.arraycopy(pcm, i * SimpleVoiceChatPlugin.OPUS_FRAME_SIZE, window, 0, length);
            if (length < SimpleVoiceChatPlugin.OPUS_FRAME_SIZE) {
                Arrays.fill(window, length, SimpleVoiceChatPlugin.OPUS_FRAME_SIZE, (short) 0);
            }
            hook.channel.send(hook.encoder.encode(window));
        }
    }
}
