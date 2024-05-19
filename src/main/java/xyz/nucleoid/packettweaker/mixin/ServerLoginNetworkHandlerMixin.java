package xyz.nucleoid.packettweaker.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.nucleoid.packettweaker.ContextProvidingPacketListener;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandlerMixin implements ContextProvidingPacketListener {

    @Shadow @Final
    MinecraftServer server;

    @Shadow @Final
    ClientConnection connection;

    @Shadow @Nullable private GameProfile profile;

    @Override
    public @Nullable RegistryWrapper.WrapperLookup getWrapperLookupForPacketTweaker() {
        return this.server.getRegistryManager();
    }

    @Override
    public @Nullable ClientConnection getClientConnectionForPacketTweaker() {
        return this.connection;
    }

    @Override
    public @Nullable GameProfile getGameProfileForPacketTweaker() {
        return this.profile;
    }
}
