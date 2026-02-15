package net.ninjago.createaudio.registry;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.content.mechanicalRecordPlayer.RecordPlayerBlock;
import net.ninjago.createaudio.content.mechanicalSpeaker.MechanicalSpeakerBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {
    private static final CreateRegistrate REGISTRATE = CreateAudio.registrate();

    public static final BlockEntry<Block> TEST =
            REGISTRATE.block("test", Block::new)
                    .properties(properties -> properties
                            .requiresCorrectToolForDrops()
                            .strength(5)
                    )
                    .loot(RegistrateBlockLootTables::dropSelf)
                    .lang("Block of Test")
                    .item().build()
                    .register();

    public static final BlockEntry<RecordPlayerBlock> MECHANICAL_RECORD_PLAYER = REGISTRATE.block("mechanical_record_player", RecordPlayerBlock::new)
            .properties(properties -> properties
                    .strength(5)
                    .noOcclusion()
                    .mapColor(MapColor.WOOD)
            )
            .addLayer(() -> RenderType::cutoutMipped)
            .loot(RegistrateBlockLootTables::dropSelf)
            .blockstate((ctx, prov) ->
                    prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .lang("Mechanical Record Player")
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<MechanicalSpeakerBlock> MECHANICAL_SPEAKER = REGISTRATE.block("mechanical_speaker", MechanicalSpeakerBlock::new)
            .properties(properties -> properties
                    .strength(5)
                    .noOcclusion()
                    .mapColor(MapColor.STONE)
            )
            .loot(RegistrateBlockLootTables::dropSelf)
            .blockstate((ctx, prov) ->
                    prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
            .lang("Mechanical Speaker")
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {
    }
}
