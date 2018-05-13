package com.company;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    Queue<Request> requests = new PriorityQueue<>();
    RequestRespondent requestRespondent = new RequestRespondent(requests);

    SimpleServerHandler() {
        requestRespondent.executor.start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        requests.add(new Request(ctx, o.toString()));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

