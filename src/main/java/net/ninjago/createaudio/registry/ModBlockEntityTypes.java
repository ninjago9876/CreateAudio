package net.ninjago.createaudio.registry;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.world.level.block.Blocks;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.content.mechanicalRecordPlayer.RecordPlayerBlockEntity;
import net.ninjago.createaudio.content.mechanicalRecordPlayer.RecordPlayerBlockEntityRenderer;
import net.ninjago.createaudio.content.mechanicalRecordPlayer.RecordPlayerVisual;

public class ModBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = CreateAudio.registrate();

    public static final BlockEntityEntry<RecordPlayerBlockEntity> MECHANICAL_RECORD_PLAYER = REGISTRATE
            .blockEntity("mechanical_record_player", RecordPlayerBlockEntity::new)
            .visual(() -> RecordPlayerVisual::new)
            .validBlocks(ModBlocks.MECHANICAL_RECORD_PLAYER)
            .renderer(() -> RecordPlayerBlockEntityRenderer::new)
            .register();

    public static void register() {
    }
}
