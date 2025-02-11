/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.PacketByteBuf;
import dan200.computercraft.fabric.Helper;
import dan200.computercraft.shared.network.client.ComputerDataClientMessage;
import dan200.computercraft.shared.network.client.ComputerTerminalClientMessage;
import dan200.computercraft.shared.network.client.OpenComputerGuiClientMessage;
import dan200.computercraft.shared.network.client.OpenGuiContainerMessage;
import dan200.computercraft.shared.network.server.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.controller.PlayerControllerMP;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.handler.PacketHandler;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class NetworkHandler
{
    private static final Int2ObjectMap<BiConsumer<NetworkMessage.NetworkContext, PacketByteBuf>> packetReaders = new Int2ObjectOpenHashMap<>();
    private static final Object2IntMap<Class<?>> packetIds = new Object2IntOpenHashMap<>();

    //private static final Identifier ID = new Identifier( ComputerCraft.MOD_ID, "main" );

    private NetworkHandler()
    {
    }

    public static void setup()
    {
        Packet.addMapping(88, true, true, PacketByteBuf.class);

//        ServerSidePacketRegistry.INSTANCE.register( ID, NetworkHandler::receive );
//        if( FabricLoader.getInstance()
//            .getEnvironmentType() == EnvType.CLIENT )
//        {
//            ClientSidePacketRegistry.INSTANCE.register( ID, NetworkHandler::receive );
//        }

//        // Server messages
        registerMainThread( 0, ComputerActionServerMessage::new );
        registerMainThread( 1, QueueEventServerMessage::new );
        registerMainThread( 2, RequestComputerMessage::new );
        registerMainThread( 3, KeyEventServerMessage::new );
        registerMainThread( 4, MouseEventServerMessage::new );

        // Client messages
        registerMainThread( 8, OpenGuiContainerMessage::new );
        registerMainThread( 9, OpenComputerGuiClientMessage::new );
//        registerMainThread( 10, ChatTableClientMessage::new );
        registerMainThread( 11, ComputerDataClientMessage::new );
//        registerMainThread( 12, ComputerDeletedClientMessage::new );
        registerMainThread( 13, ComputerTerminalClientMessage::new );
//        registerMainThread( 14, PlayRecordClientMessage.class, PlayRecordClientMessage::new );
//        registerMainThread( 15, TerminalDimensionsClientMessage.class, TerminalDimensionsClientMessage::new );
    }

    public static void receive(NetworkMessage.NetworkContext context, PacketByteBuf buffer )
    {
        int type = buffer.readByte();
        packetReaders.get( type )
            .accept( context, buffer );
    }

    /**
     * /** Register packet, and a thread-unsafe handler for it.
     *
     * @param <T>     The type of the packet to send.
     * @param id      The identifier for this packet type
     * @param factory The factory for this type of packet.
     */
    private static <T extends NetworkMessage> void registerMainThread( int id, Supplier<T> factory )
    {
        registerMainThread( id, getType( factory ), buf -> {
            T instance = factory.get();
            instance.fromBytes( buf );
            return instance;
        } );
    }

    /**
     * /** Register packet, and a thread-unsafe handler for it.
     *
     * @param <T>     The type of the packet to send.
     * @param type    The class of the type of packet to send.
     * @param id      The identifier for this packet type
     * @param decoder The factory for this type of packet.
     */
    private static <T extends NetworkMessage> void registerMainThread( int id, Class<T> type, Function<PacketByteBuf, T> decoder )
    {
        packetIds.put( type, id );
        packetReaders.put( id, ( context, buf ) -> {
            T result = decoder.apply( buf );
                 result.handle(context);
        } );
    }

    @SuppressWarnings( "unchecked" )
    private static <T> Class<T> getType( Supplier<T> supplier )
    {
        return (Class<T>) supplier.get()
            .getClass();
    }

    private static PacketByteBuf encode(NetworkMessage message )
    {
        PacketByteBuf buf = new PacketByteBuf();
        buf.writeByte( packetIds.getInt( message.getClass() ) );
        message.toBytes( buf );
        return buf;
    }

    @Environment(EnvType.CLIENT)
    public static void sendToPlayerLocal(NetworkMessage packet)
    {
        packet.handle(new NetworkMessage.NetworkContext(Minecraft.getMinecraft().thePlayer));
    }

    @Environment(EnvType.SERVER)
    public static void sendToPlayerServer(Player player, NetworkMessage packet )
    {
        ((PlayerServer)player).playerNetServerHandler.sendPacket(encode(packet));
    }

    public static void sendToPlayer(Player player, NetworkMessage packet )
    {
        if (!Helper.isServerEnvironment()){
            sendToPlayerLocal(packet);
            return;
        }
        sendToPlayerServer(player, packet);
    }

    public static void sendToAllPlayers( NetworkMessage packet )
    {
        if (!Helper.isServerEnvironment()){
            sendToPlayerLocal(packet);
            return;
        }
        MinecraftServer.getInstance().playerList.sendPacketToAllPlayers(encode(packet));
    }

    @Environment( EnvType.CLIENT )
    public static void sendToServer( NetworkMessage packet )
    {
        if (Helper.isSinglePlayer()){
            sendToPlayerLocal(packet);
            return;
        }
        Minecraft.getMinecraft().getSendQueue().addToSendQueue(encode(packet));
    }

    public static void sendToAllAround(NetworkMessage packet, World world, Vec3 pos, double range )
    {
//        world.getServer()
//            .getPlayerManager()
//            .sendToAround( null, pos.x, pos.y, pos.z, range, world.getRegistryKey(), new CustomPayloadS2CPacket( ID, encode( packet ) ) );
    }
}
