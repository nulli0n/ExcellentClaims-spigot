package su.nightexpress.excellentclaims.hook;

import su.nightexpress.nightcore.util.Plugins;

public class Hooks {

    public static final String PROTOCOL_LIB = "ProtocolLib";
    public static final String PACKET_EVENTS = "packetevents";

    public static boolean hasPacketLibrary() {
        return Plugins.isInstalled(PACKET_EVENTS) || Plugins.isInstalled(PROTOCOL_LIB);
    }
}