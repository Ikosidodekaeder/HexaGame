package com.hexagon.game.network.packets;

import java.util.UUID;

/**
 * Created by Johannes on 19.02.2018.
 */

public class PacketJoin extends Packet {


    /**
     * Nickname of the player
     * The combination of username & localClientID must be unique!
     */
    private String Username;

    /**
     * Issued by the Client-Server
     */
    private String localClientID;
    private String Version;


    public PacketJoin(PacketType type, String username,String clientID,String version) {
        super(type);

        this.localClientID = clientID;
        this.Username = username;
        this.Version = version;
    }

    public PacketJoin(PacketType type, UUID globalClientID, String username,String clientID,String version) {
        super(type, globalClientID);

        this.Username = username;
        this.localClientID = clientID;
        this.Version = version;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getLocalClientID() {
        return localClientID;
    }

    public void setLocalClientID(String localClientID) {
        this.localClientID = localClientID;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }
}
