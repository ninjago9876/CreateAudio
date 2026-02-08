package net.ninjago.createaudio.content.mechanicalRecordPlayer;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;

public class RecordPlayerVisual extends SingleAxisRotatingVisual<RecordPlayerBlockEntity> implements SimpleDynamicVisual {
    private final RecordPlayerBlockEntity record_player;

    public RecordPlayerVisual(VisualizationContext context, RecordPlayerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.SHAFTLESS_COGWHEEL));

        record_player = blockEntity;
    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {

    }
}
