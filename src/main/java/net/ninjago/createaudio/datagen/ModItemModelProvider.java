package net.ninjago.createaudio.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.ninjago.createaudio.CreateAudio;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CreateAudio.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
