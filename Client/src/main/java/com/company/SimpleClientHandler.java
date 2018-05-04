package com.company;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Scanner;

public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    Scanner sc =new Scanner(System.in);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg.toString());
    }
    @Override
    public void channelActive(final ChannelHandlerContext ctx){
        System.out.println("Connected!");
        System.out.println("weather [stat]/[all]/[delete(FOR ADMINS ONLY)] [city] [yyyy-mm-dd] [yyyy-mm-dd]");
        String cmd=sc.nextLine();
        if(cmd.equals("quit")) {
            ctx.close();
        }else {
            ChannelFuture f = ctx.writeAndFlush(cmd);
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    assert f == future;
                    System.out.println("Request has been sent...");
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

