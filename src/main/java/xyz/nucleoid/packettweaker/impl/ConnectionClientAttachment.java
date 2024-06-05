package xyz.nucleoid.packettweaker.impl;

import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public interface ConnectionClientAttachment {
    <T> T packetTweaker$get(PacketContext.Key<T> key);
    @Nullable
    <T> T packetTweaker$set(PacketContext.Key<T> key, @Nullable T data);
}
