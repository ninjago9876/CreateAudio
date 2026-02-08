package net.ninjago.createaudio.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.registry.ModBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.TEST.get());
        dropSelf(ModBlocks.MECHANICAL_RECORD_PLAYER.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return CreateAudio.registrate().getAll(Registries.BLOCK).stream().map(Supplier::get).collect(Collectors.toList());
    }
}
