package net.ninjago.createaudio.voicechat;

import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.ninjago.createaudio.CreateAudio;

import java.util.Arrays;

@ForgeVoicechatPlugin
public class SimpleVoiceChatPlugin implements VoicechatPlugin {
    @Override
    public String getPluginId() {
        return CreateAudio.MODID;
    }

    @Override
    public void initialize(VoicechatApi api) {
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, (MicrophonePacketEvent event) -> {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(
                    Arrays.toString(event.getPacket().getOpusEncodedData())
            ));
        });
    }
}
