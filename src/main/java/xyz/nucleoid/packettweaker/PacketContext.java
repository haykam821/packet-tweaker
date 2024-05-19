package xyz.nucleoid.packettweaker;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public final class PacketContext {
    private static final ThreadLocal<PacketContext> INSTANCE = ThreadLocal.withInitial(PacketContext::new);

    private ContextProvidingPacketListener target = ContextProvidingPacketListener.EMPTY;
    @Nullable
    private Packet<?> encodedPacket = null;
    @Nullable
    private ClientConnection connection = null;

    public static PacketContext get() {
        return INSTANCE.get();
    }

    public static void runWithContext(@Nullable ClientConnection connection, @Nullable PacketListener networkHandler, @Nullable Packet<?> packet, Runnable runnable) {
        if (networkHandler == null) {
            runnable.run();
            return;
        }

        PacketContext context = PacketContext.get();
        var oldTarget = context.target;
        var oldPacket = context.encodedPacket;
        var oldConnection = context.connection;
        context.target = (ContextProvidingPacketListener) networkHandler;
        context.encodedPacket = packet;
        context.connection = connection;
        runnable.run();
        context.target = oldTarget;
        context.encodedPacket = oldPacket;
        context.connection = oldConnection;
    }
    public static void runWithContext(@Nullable PacketListener networkHandler, @Nullable Packet<?> packet, Runnable runnable) {
        runWithContext(((ContextProvidingPacketListener) networkHandler).getClientConnectionForPacketTweaker(), networkHandler, packet, runnable);
    }

    public static void runWithContext(@Nullable PacketListener networkHandler, Runnable runnable) {
        runWithContext(networkHandler, null, runnable);
    }

    @ApiStatus.Internal
    public static void setContext(@Nullable ClientConnection connection, @Nullable Packet<?> packet) {
        if (connection == null) {
            clearContext();
            return;
        }

        PacketContext context = PacketContext.get();
        context.target = (ContextProvidingPacketListener) connection.getPacketListener();
        context.connection = connection;
        context.encodedPacket = packet;
    }
    public static void clearContext() {
        PacketContext context = PacketContext.get();
        context.target = ContextProvidingPacketListener.EMPTY;
        context.connection = null;
        context.encodedPacket = null;
    }

    @Nullable
    @Deprecated
    public ServerPlayerEntity getTarget() {
        return this.getPlayer();
    }

    @Nullable
    public ServerPlayerEntity getPlayer() {
        return this.target.getPlayerForPacketTweaker();
    }
    @Nullable
    public SyncedClientOptions getClientOptions() {
        return this.target.getClientOptionsForPacketTweaker();
    }
    @Nullable
    public GameProfile getGameProfile() {
        return this.target.getGameProfileForPacketTweaker();
    }

    @Nullable
    public RegistryWrapper.WrapperLookup getRegistryWrapperLookup() {
        return this.target.getWrapperLookupForPacketTweaker();
    }

    public ContextProvidingPacketListener getPacketListener() {
        return this.target;
    }

    @Nullable
    public ClientConnection getClientConnection() {
        return this.connection;
    }

    @Nullable
    public Packet<?> getEncodedPacket() {
        return this.encodedPacket;
    }
}
