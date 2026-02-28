package net.ninjago.createaudio.voicechat;

import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.ninjago.createaudio.CreateAudio;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@ForgeVoicechatPlugin
public class SimpleVoiceChatPlugin implements VoicechatPlugin {
    public static VoicechatApi api;
    public static VoicechatServerApi serverApi;

    public static final CompletableFuture<VoicechatServerApi> apiFuture = new CompletableFuture<>();

    @Override
    public String getPluginId() {
        return CreateAudio.MODID;
    }

    @Override
    public void initialize(VoicechatApi api) {
        SimpleVoiceChatPlugin.api = api;
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        serverApi = event.getVoicechat();
        apiFuture.complete(serverApi);
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, (MicrophonePacketEvent event) -> {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(
                    Arrays.toString(event.getPacket().getOpusEncodedData())
            ));
        });

        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
    }
}
