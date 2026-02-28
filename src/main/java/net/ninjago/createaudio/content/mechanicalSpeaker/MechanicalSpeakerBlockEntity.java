package net.ninjago.createaudio.content.mechanicalSpeaker;

import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.audiochannel.AudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.LocationalAudioChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.nodes.SVCAudioOutputNode;
import net.ninjago.createaudio.audio.utility.NodeGraph;
import net.ninjago.createaudio.foundation.ModularNetworkHandler;
import net.ninjago.createaudio.foundation.blockentity.KineticAudioBlockEntity;
import net.ninjago.createaudio.voicechat.SimpleVoiceChatPlugin;

import java.util.UUID;

public class MechanicalSpeakerBlockEntity extends KineticAudioBlockEntity {
    public MechanicalSpeakerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state, (engine, inputs, outputs, references) -> {
            NodeGraph graph = new NodeGraph(engine.allocateUID());

            SVCAudioOutputNode svcAudioOutputNode = new SVCAudioOutputNode(engine.allocateUID());
            references.put("hook", svcAudioOutputNode.getHookReference());

            inputs.add(new ModularNetworkHandler.ModularNetworkSocket(svcAudioOutputNode.getUid(), 0)); //  Input: 0

            graph.addNode(svcAudioOutputNode);
            return graph;
        }, "mechanical_speaker");
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level == null || level.isClientSide() || level.getServer() == null) return;

        // wait for the VoiceChat API to be ready
        SimpleVoiceChatPlugin.apiFuture.thenAccept(api ->
                level.getServer().execute(() -> acceptApi(api))
        );
    }

    public void acceptApi(VoicechatServerApi api) {
        CreateAudio.LOGGER.info("Accepting VC API");

        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = getBlockPos().getCenter();
            LocationalAudioChannel channel = api.createLocationalAudioChannel(UUID.randomUUID(), api.fromServerLevel(serverLevel), api.createPosition(center.x, center.y, center.z));
            getNetworkHandler().getReference("hook").set(new SVCAudioOutputNode.VoiceChatHook(api.createEncoder(), channel));
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
//        playing = speed >= 64;
    }
}
