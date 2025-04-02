package dan200.computercraft.shared.common;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.pocket.peripherals.PocketModem;
import dan200.computercraft.shared.pocket.peripherals.PocketSpeaker;

public final class ComputerCraftPocketUpgrades
{
    public static PocketModem wirelessModemNormal = new PocketModem( false, ComputerCraftBlocks.WIRELESS_MODEM_NORMAL.id() );
    public static PocketModem wirelessModemAdvanced = new PocketModem( true, ComputerCraftBlocks.WIRELESS_MODEM_ADVANCED.id() );
    public static PocketSpeaker speaker = new PocketSpeaker(ComputerCraftBlocks.SPEAKER.id());

    public static void registerPocketUpgrades()
    {
        ComputerCraftAPI.registerPocketUpgrade( wirelessModemNormal );
        ComputerCraftAPI.registerPocketUpgrade( wirelessModemAdvanced );
        ComputerCraftAPI.registerPocketUpgrade( speaker );
    }
}
