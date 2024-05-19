package xyz.nucleoid.packettweaker.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.nucleoid.packettweaker.ContextProvidingPacketListener;

@Mixin(ClientCommonNetworkHandler.class)
public class ClientCommonNetworkHandlerMixin implements ContextProvidingPacketListener {
    @Shadow @Final protected MinecraftClient client;

    @Shadow @Final protected ClientConnection connection;

    @Override
    public @Nullable RegistryWrapper.WrapperLookup getWrapperLookupForPacketTweaker() {
        return this.client.world != null ? this.client.world.getRegistryManager() : null;
    }

    @Override
    public @Nullable ClientConnection getClientConnectionForPacketTweaker() {
        return this.connection;
    }
}
