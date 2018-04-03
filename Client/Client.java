package com.company;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;



public class Client {
    ChannelFuture future;
    public void init()throws Exception {
        String host = "localhost";
        int port =8080;
        String msg="Hello World!";
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new SimpleClientHandler());
                }
            });
            this.future = b.connect(host, port).sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
        future.channel().writeAndFlush(msg);
        future.channel().closeFuture().sync();
    }
    public static void main(String[] args){
        Client cl=new Client();
        try {cl.init();}catch (Exception l){return;}
    }
}



