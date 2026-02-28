package net.ninjago.createaudio.foundation.blockentity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.foundation.AudioManager;
import net.ninjago.createaudio.foundation.ModularNetworkHandler;

import java.util.*;

public class KineticAudioBlockEntity extends KineticBlockEntity implements AudioManager {
    protected AudioEngine engine;
    private ModularNetworkHandler networkHandler;

    private final ModularNetworkHandler.ModularNetworkGraphSupplier graphSupplier;

    public record BlockModularNetworkSocket(BlockPos pos, int index) { }
    protected final Map<Integer, BlockModularNetworkSocket> worldConnections = new HashMap<>();    // InputSocketIndex -> OutputSocket

    private final String name;

    public KineticAudioBlockEntity(
            BlockEntityType<?> typeIn,
            BlockPos pos,
            BlockState state,
            ModularNetworkHandler.ModularNetworkGraphSupplier graphSupplier,
            String name
    ) {
        super(typeIn, pos, state);
        this.graphSupplier = graphSupplier;
        this.name = name;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (getLevel() != null) {
            if (getLevel().isClientSide()) return;
        }

        engine = CreateAudio.audioEngine;
        if (engine == null) {
            return;
        }

       networkHandler = new ModularNetworkHandler(engine, graphSupplier, name);

        for (int inputIndex : worldConnections.keySet()) {
            BlockModularNetworkSocket outputSocket = worldConnections.get(inputIndex);
            createConnection(outputSocket, inputIndex);
        }
    }

    @Override
    public void remove() {
        if (networkHandler != null) {
            networkHandler.remove();
        }
        super.remove();
    }

    @Override
    public ModularNetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    protected void createConnection(BlockModularNetworkSocket fromOutputSocket, int toInputIndex) {
        worldConnections.put(toInputIndex, fromOutputSocket);

        if (level == null || level.isClientSide)
            return;
        CreateAudio.LOGGER.info("Creating connection from {} : {} to {} : {}!", fromOutputSocket.pos, fromOutputSocket.index, getBlockPos(), toInputIndex);

        BlockEntity blockEntity = level.getBlockEntity(fromOutputSocket.pos);

        if (blockEntity instanceof AudioManager fromAudioManager) {
            networkHandler.connect(fromAudioManager.getNetworkHandler(), fromOutputSocket.index, toInputIndex);
        }

    }

    protected void removeConnection(int toInputIndex) {
        worldConnections.remove(toInputIndex);

        if (level == null || level.isClientSide)
            return;
        CreateAudio.LOGGER.info("Removing connection to input {}", toInputIndex);
    }

    protected void updateConnections(Map<Integer, BlockModularNetworkSocket> newWorldConnections) {
        Map<Integer, BlockModularNetworkSocket> currentWorldConnections = new HashMap<>(worldConnections);

        Set<Integer> toRemove = new HashSet<>(currentWorldConnections.keySet());
        toRemove.removeAll(newWorldConnections.keySet());
        for (Integer socketId : toRemove) {
            removeConnection(socketId);
        }

        Set<Integer> toAdd = new HashSet<>(newWorldConnections.keySet());
        toAdd.removeAll(currentWorldConnections.keySet());
        for (Integer inputIndex : toAdd) {
            createConnection(newWorldConnections.get(inputIndex), inputIndex);
        }

        for (Integer inputIndex : currentWorldConnections.keySet()) {
            if (newWorldConnections.containsKey(inputIndex) && !Objects.equals(currentWorldConnections.get(inputIndex), newWorldConnections.get(inputIndex))) {
                removeConnection(inputIndex);
                createConnection(newWorldConnections.get(inputIndex), inputIndex);
            }
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        Map<Integer, BlockModularNetworkSocket> newWorldConnections = new HashMap<>();

        CompoundTag connections = compound.getCompound("Connections");
        for (String inputSocketIndex : connections.getAllKeys()) {
            CompoundTag outputSocketTag = connections.getCompound(inputSocketIndex);
            BlockModularNetworkSocket inputSocket = new BlockModularNetworkSocket(
                    NBTHelper.readBlockPos(outputSocketTag, "Pos"),
                    outputSocketTag.getInt("OutputSocketIndex")
            );
            newWorldConnections.put(Integer.valueOf(inputSocketIndex), inputSocket);
        }

        updateConnections(newWorldConnections);
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        CompoundTag connections = new CompoundTag();
        for (Integer fromSocketIndex : worldConnections.keySet()) {
            CompoundTag worldInputSocket = new CompoundTag();
            worldInputSocket.put("Pos", NbtUtils.writeBlockPos(worldConnections.get(fromSocketIndex).pos));
            worldInputSocket.putInt("OutputSocketIndex", worldConnections.get(fromSocketIndex).index);
            connections.put(String.valueOf(fromSocketIndex), worldInputSocket);
        }
        compound.put("Connections", connections);
        super.write(compound, registries, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag connections = new CompoundTag();
        for (Integer fromSocketIndex : worldConnections.keySet()) {
            CompoundTag worldInputSocket = new CompoundTag();
            worldInputSocket.put("Pos", NbtUtils.writeBlockPos(worldConnections.get(fromSocketIndex).pos));
            worldInputSocket.putInt("OutputSocketIndex", worldConnections.get(fromSocketIndex).index);
            connections.put(String.valueOf(fromSocketIndex), worldInputSocket);
        }
        tag.put("Connections", connections);
        super.writeSafe(tag, registries);
    }
}
