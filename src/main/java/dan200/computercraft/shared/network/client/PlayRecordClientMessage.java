/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.client;

import dan200.computercraft.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import javax.annotation.Nonnull;

/**
 * Starts or stops a record on the client, depending on if {@link #soundEvent} is {@code null}.
 *
 * Used by disk drives to play record items.
 *
 * @see dan200.computercraft.shared.peripheral.diskdrive.TileDiskDrive
 */
public class PlayRecordClientMessage implements NetworkMessage
{
    private BlockPos pos;
    private String name;
    private SoundEvent soundEvent;

    public PlayRecordClientMessage( BlockPos pos, SoundEvent event, String name )
    {
        this.pos = pos;
        this.name = name;
        soundEvent = event;
    }

    public PlayRecordClientMessage( BlockPos pos )
    {
        this.pos = pos;
        name = null;
        soundEvent = null;
    }

    @Override
    public void encodeToUniversalPacket( @Nonnull UniversalPacket buf )
    {
        buf.writeInt( pos.x );
        buf.writeInt( pos.y );
        buf.writeInt( pos.z );
        if( soundEvent == null )
        {
            buf.writeBoolean( false );
        }
        else
        {
            buf.writeBoolean( true );
            buf.writeString( name );
            buf.writeString( soundEvent.getEventID() );
        }
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket buf) {
        final int x = buf.readInt();
        final int y = buf.readInt();
        final int z = buf.readInt();
        pos = new BlockPos(x, y, z);
        if( buf.readBoolean() )
        {
            name = buf.readString();
            soundEvent = null;
        }
        else
        {
            name = null;
            soundEvent = null;
        }
    }

    @Override
    public void handle(NetworkContext context)
    {
        Minecraft mc = Minecraft.getMinecraft();
//        mc.worldRenderer.playSong( soundEvent, pos );
//        mc
//            .currentWorld
//            .playSoundEffect(
//                null,
//                SoundCategory.WEATHER_SOUNDS,
//                d,
//                d1,
//                d2,
//                "ambient.weather.rain",
//                0.1F * this.mc.currentWorld.weatherManager.getWeatherIntensity() * this.mc.currentWorld.weatherManager.getWeatherPower() * 0.5F,
//                0.5F
        if( name != null )
        {
            mc.hudIngame.setRecordPlayingMessage(name);
        }
    }
}
