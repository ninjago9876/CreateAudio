package net.ninjago.createaudio.foundation.blockentity;

import com.mojang.datafixers.util.Function3;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ninjago.createaudio.CreateAudio;
import net.ninjago.createaudio.audio.AudioEngine;
import net.ninjago.createaudio.audio.AudioNetwork;
import net.ninjago.createaudio.audio.utility.AudioInputLocation;
import net.ninjago.createaudio.audio.utility.AudioOutputLocation;
import net.ninjago.createaudio.foundation.AudioManager;
import net.ninjago.createaudio.foundation.ModularNetworkHandler;

import java.util.*;

public class KineticAudioBlockEntity extends KineticBlockEntity implements AudioManager {
    private AudioEngine engine;
    private ModularNetworkHandler networkHandler;

    private final Function3<AudioEngine, Map<String, AudioInputLocation>, Map<String, AudioOutputLocation>, AudioNetwork> networkSupplier;

    public record WorldBlockAudioOutputSocket(BlockPos pos, String socketId) { }
    protected final Map<String, WorldBlockAudioOutputSocket> worldConnections = new HashMap<>();    // InputSocketID -> OutputSocket

    private final String name;

    public KineticAudioBlockEntity(
            BlockEntityType<?> typeIn,
            BlockPos pos,
            BlockState state,
            Function3<AudioEngine, Map<String, AudioInputLocation>, Map<String, AudioOutputLocation>, AudioNetwork> networkSupplier,
            String name
    ) {
        super(typeIn, pos, state);
        this.networkSupplier = networkSupplier;
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

        networkHandler = new ModularNetworkHandler(engine, networkSupplier, name);
    }

    @Override
    public void remove() {
        if (getLevel() != null) {
            if (getLevel().isClientSide()) return;
        }
        super.remove();
        networkHandler.remove();
    }

    @Override
    public ModularNetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    protected void createConnection(WorldBlockAudioOutputSocket fromOutputSocket, String toInputId) {
        CreateAudio.LOGGER.info("Creating connection!");
        worldConnections.put(toInputId, fromOutputSocket);
        assert level != null;
        BlockEntity sourceBlockEntity = level.getBlockEntity(fromOutputSocket.pos);
        if (!(sourceBlockEntity instanceof AudioManager sourceBlockAudioManager)) return;
        networkHandler.connectWith(
                new ModularNetworkHandler.ModularNetworkSocket(
                        fromOutputSocket.socketId,
                        sourceBlockAudioManager.getNetworkHandler()
                ),
                toInputId
        );
    }

    protected void removeConnection(String toInputId) {
        CreateAudio.LOGGER.info("Removing connection!");
        WorldBlockAudioOutputSocket outputSocket = worldConnections.remove(toInputId);
        assert level != null;
        BlockEntity sourceBlockEntity = level.getBlockEntity(outputSocket.pos);
        if (!(sourceBlockEntity instanceof AudioManager sourceBlockAudioManager)) return;
        networkHandler.disconnectWith(new ModularNetworkHandler.ModularNetworkSocket(outputSocket.socketId, sourceBlockAudioManager.getNetworkHandler()), toInputId);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        Map<String, WorldBlockAudioOutputSocket> newWorldConnections = new HashMap<>();

        if (level == null) {
            super.read(compound, registries, clientPacket);
            return;
        }

        CompoundTag connections = compound.getCompound("Connections");
        for (String outputSocketId : connections.getAllKeys()) {
            CompoundTag outputSocketTag = connections.getCompound(outputSocketId);
            WorldBlockAudioOutputSocket inputSocket = new WorldBlockAudioOutputSocket(
                    getPosFromTag(outputSocketTag.getCompound("Pos")),
                    Objects.requireNonNull(outputSocketTag.get("OutputSocketID")).getAsString()
            );
            newWorldConnections.put(outputSocketId, inputSocket);
        }

        Set<String> toRemove = new HashSet<>(worldConnections.keySet());
        toRemove.removeAll(newWorldConnections.keySet());
        for (String socketId : toRemove) {
            removeConnection(socketId);
        }

        Set<String> toAdd = new HashSet<>(newWorldConnections.keySet());
        toAdd.removeAll(worldConnections.keySet());
        for (String socketId : toAdd) {
            createConnection(newWorldConnections.get(socketId), socketId);
        }

        for (String inputId : worldConnections.keySet()) {
            if (newWorldConnections.containsKey(inputId) && !Objects.equals(worldConnections.get(inputId), newWorldConnections.get(inputId))) {
                removeConnection(inputId);
                createConnection(newWorldConnections.get(inputId), inputId);
            }
        }

        super.read(compound, registries, clientPacket);
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        CompoundTag connections = new CompoundTag();
        for (String fromSocketId : worldConnections.keySet()) {
            CompoundTag worldInputSocket = new CompoundTag();
            worldInputSocket.put("Pos", NbtUtils.writeBlockPos(worldConnections.get(fromSocketId).pos));
            worldInputSocket.putString("OutputSocketID", worldConnections.get(fromSocketId).socketId);
            connections.put(fromSocketId, worldInputSocket);
        }
        compound.put("Connections", connections);
        super.write(compound, registries, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag connections = new CompoundTag();
        for (String fromSocketId : worldConnections.keySet()) {
            CompoundTag worldInputSocket = new CompoundTag();
            worldInputSocket.put("Pos", NbtUtils.writeBlockPos(worldConnections.get(fromSocketId).pos));
            worldInputSocket.putString("OutputSocketID", worldConnections.get(fromSocketId).socketId);
            connections.put(fromSocketId, worldInputSocket);
        }
        tag.put("Connections", connections);
        super.writeSafe(tag, registries);
    }
}
