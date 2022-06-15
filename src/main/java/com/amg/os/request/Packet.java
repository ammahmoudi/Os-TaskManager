package com.amg.os.request;

import com.amg.os.util.serialize.Deserializer;
import com.amg.os.util.serialize.Serializer;

import java.io.Serializable;

public class Packet implements Serializable {
    int senderId;
    boolean response;
    PacketType type;
    String data;

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public Packet(int senderId, PacketType type, boolean response, String data) {
        this.senderId = senderId;
        this.type = type;
        this.data = data;
        this.response =response;
    }
    public Packet(int senderId, PacketType type, boolean response, Serializable object) {
        this.senderId = senderId;
        this.type = type;
        this.data = new Serializer().serialize(object);
        this.response=response;
    }


    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public PacketType getType() {
        return type;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
     public Object getObject(){
return new Deserializer().deserialize(data);
    }

    @Override
    public String toString() {
        return "{" +
                "" + senderId +
                "," + response +
                "," + type +
                ", '" + ((data.length()<50) ? data:"Object")+ '\'' +
                '}';
    }
}
//((data.length()<50) ? data:"Object")
