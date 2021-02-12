package com.happs.ximand.ringcontrol.model.object.response;

public class Response {

    public static final byte RESPONSE_OK = (byte) 0xff;

    private final byte commandCode;
    private final byte responseCode;

    public Response(byte commandCode, byte responseCode) {
        this.commandCode = commandCode;
        this.responseCode = responseCode;
    }

    public static Response getResponseByMessage(byte[] message) {
        if (message.length == 2) {
            return new Response(message[0], message[1]);
        }
        return empty();
    }

    public static Response empty() {
        return new Response((byte) 0, (byte) 0);
    }

    public byte getCommandCode() {
        return commandCode;
    }

    public byte getResponseCode() {
        return responseCode;
    }

    public boolean isSuccess() {
        return responseCode == RESPONSE_OK;
    }
}
