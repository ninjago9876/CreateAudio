package net.ninjago.createaudio.voicechat;

import com.sun.source.util.Plugin;
import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.ninjago.createaudio.CreateAudio;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.CompletableFuture;

@ForgeVoicechatPlugin
public class SimpleVoiceChatPlugin implements VoicechatPlugin {
    public static VoicechatApi api;
    public static VoicechatServerApi serverApi;

    public static final CompletableFuture<VoicechatServerApi> apiFuture = new CompletableFuture<>();

    public static String SPEAKER_CATEGORY = "speakers";

    public static int OPUS_FRAME_SIZE = 960;

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

        VolumeCategory speakers = api.volumeCategoryBuilder()
                .setId(SPEAKER_CATEGORY)
                .setName("Speakers")
                .setDescription("The volume of all speakers")
//                .setIcon(getIcon("category_speakers.png"))
                .build();

        serverApi.registerVolumeCategory(speakers);
    }

    @Nullable
    private int[][] getIcon(String path) {
        try {
            Enumeration<URL> resources = Plugin.class.getClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                BufferedImage bufferedImage = ImageIO.read(resources.nextElement().openStream());
                if (bufferedImage.getWidth() != 16) {
                    continue;
                }
                if (bufferedImage.getHeight() != 16) {
                    continue;
                }
                int[][] image = new int[16][16];
                for (int x = 0; x < bufferedImage.getWidth(); x++) {
                    for (int y = 0; y < bufferedImage.getHeight(); y++) {
                        image[x][y] = bufferedImage.getRGB(x, y);
                    }
                }
                return image;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
