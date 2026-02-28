package net.ninjago.createaudio.content.mechanicalSpeaker;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.ninjago.createaudio.content.mechanicalRecordPlayer.RecordPlayerBlockEntity;

public class MechanicalSpeakerVisual extends SingleAxisRotatingVisual<MechanicalSpeakerBlockEntity> implements SimpleDynamicVisual {
    private final MechanicalSpeakerBlockEntity speaker;

    public MechanicalSpeakerVisual(VisualizationContext context, MechanicalSpeakerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.SHAFTLESS_COGWHEEL));

        speaker = blockEntity;
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {

    }
}
