package net.ninjago.createaudio.audio.nodes;

import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.utility.AudioBuffer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class SVCAudioOutputNode extends AudioNode {

    public record VoiceChatHook(OpusEncoder encoder, AudioChannel channel) {}

    private AtomicReference<Object> hookReference;

    public SVCAudioOutputNode(int uid) {
        super(uid);
    }

    public SVCAudioOutputNode(SVCAudioOutputNode otherNode) {
        super(otherNode);
        this.hookReference = otherNode.hookReference;
    }

    public AtomicReference<Object> getHookReference() {
        return hookReference;
    }

    @Override
    public void process(long currentFrame) {
        AudioBuffer input = getInput(0);
        if (input == null) return;

        if (hookReference == null) return;
        if (hookReference.get() instanceof VoiceChatHook(OpusEncoder encoder, AudioChannel channel)) {
            channel.send(encoder.encode(input.getPCM()));
        }

        if (currentFrame % 100 == 0) {
            CreateAudio.LOGGER.info(Arrays.toString(input.get()));
        }
    }

    @Override
    public AudioNode clone() {
        return new SVCAudioOutputNode(this);
    }
}
