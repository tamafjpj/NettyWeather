package com.company;

import io.netty.channel.ChannelHandlerContext;


public class Request {
    private ChannelHandlerContext ctx;
    private String request;

    public Request(ChannelHandlerContext ctx, String request) {
        this.ctx = ctx;
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }
}
