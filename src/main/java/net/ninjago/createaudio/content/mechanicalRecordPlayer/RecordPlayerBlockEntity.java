package net.ninjago.createaudio.content.mechanicalRecordPlayer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import de.maxhenkel.voicechat.api.Position;
import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import de.maxhenkel.voicechat.api.opus.OpusEncoderMode;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.sink.DebugLogAudioSink;
import net.ninjago.createaudio.audio.sink.SVCAudioSink;
import net.ninjago.createaudio.audio.source.SynthesizerAudioSource;
import net.ninjago.createaudio.voicechat.SimpleVoiceChatPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RecordPlayerBlockEntity extends KineticBlockEntity {
    protected String audioFile = "";
    protected boolean playing = false;

    private SynthesizerAudioSource audioSource;
    private SVCAudioSink svcAudioSink;

    private CompletableFuture<SVCAudioSink.SVCHook> hookFuture = new CompletableFuture<>();

    private LocationalAudioChannel audioChannel;

    public RecordPlayerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level instanceof ServerLevel && !level.isClientSide()) {
            SimpleVoiceChatPlugin.apiFuture.thenAccept(serverApi -> {
                Vec3 center = getBlockPos().getCenter();
                Position pos = serverApi.createPosition(center.x, center.y, center.z);

                audioChannel = serverApi.createLocationalAudioChannel(
                        UUID.randomUUID(),
                        serverApi.fromServerLevel(level),
                        pos
                );
                if (audioChannel == null) {
                    CreateAudio.LOGGER.info("Failed to create audio channel!");
                    return;
                }
                hookFuture.complete(new SVCAudioSink.SVCHook(audioChannel, serverApi.createEncoder(OpusEncoderMode.AUDIO)));
                audioChannel.setDistance(10);
                audioChannel.setCategory(SimpleVoiceChatPlugin.SPEAKER_CATEGORY);
            });

            AudioEngine engine = CreateAudio.audioEngine;

            audioSource = new SynthesizerAudioSource(engine.allocateUID());
            svcAudioSink = new SVCAudioSink(engine.allocateUID(), hookFuture);

            engine.submitTask((graph) -> graph
                    .addSource(audioSource)
                    .addSink(svcAudioSink)
                    .connect(audioSource.uid, svcAudioSink.uid)
            );
        }
    }

    @Override
    public void remove() {
        if (level instanceof ServerLevel && !level.isClientSide()) {
            AudioEngine engine = CreateAudio.audioEngine;

            engine.submitTask((graph) -> graph
                    .removeSource(audioSource.uid)
                    .removeSink(svcAudioSink.uid)
            );

            audioChannel.flush();
        }

        super.remove();
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        playing = speed >= 64;
    }
}
