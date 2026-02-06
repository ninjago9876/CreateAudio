package net.ninjago.createaudio.item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ninjago.createaudio.CreateAudio;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateAudio.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
