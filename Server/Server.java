package com.company;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;


public class Server {
    private int port;
    public Server(int port){this.port=port;}
    public void run()throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();//Обрабатывает траффик
        try{
        ServerBootstrap bs = new ServerBootstrap();
        bs.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)throws Exception{
                       try{
                           ch.pipeline().addLast(new SimpleHandler());
                       }catch (Exception IOException){
                           System.out.println("Cant addlast in pipeline");
                           return;
                       }
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true); //Создание и настройка сервера
        ChannelFuture f =bs.bind(port).sync();
        f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public class SimpleHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object o) {
            ByteBuf buf =(ByteBuf)o;
            try{
                String request=buf.toString(CharsetUtil.UTF_8);
                if(request=="1"){/*HERE WILL BE DB REQUEST CODE*/
                }
                System.out.println(request);
                ctx.close();
            }finally {
                buf.release();
            }
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}