package net.ninjago.createaudio.audio;

import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;

public class AudioEngineRegistry {
    private static final Map<MinecraftServer, AudioEngine> audioEngineMap = new HashMap<>();

    public static void attach(MinecraftServer server, AudioEngine engine) {
        audioEngineMap.put(server, engine);
    }
    public static AudioEngine remove(MinecraftServer server) {
        AudioEngine engine = get(server);
        audioEngineMap.remove(server);
        return engine;
    }

    public static AudioEngine get(MinecraftServer server) {
        return audioEngineMap.get(server);
    }
}
