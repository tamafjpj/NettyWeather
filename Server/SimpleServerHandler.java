package com.company;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        ctx.channel().writeAndFlush(o.toString());
        System.out.println(o.toString());
    }
    @Override
    public void channelActive(final ChannelHandlerContext ctx){
        ctx.writeAndFlush("Im server to client ");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

