package com.company;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Server {
    private int port;
    dbService dBase = dbService.INSTANCE;
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
                           ch.pipeline().addLast(new StringEncoder() ,new StringDecoder());
                           ch.pipeline().addLast(new SimpleServerHandler());

                       }catch (Exception IOException){
                           System.out.println("Cant configure the server");
                           return;
                       }
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true); //Создание и настройка сервера
             ChannelFuture f = bs.bind(port).sync();
            ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
            e.scheduleAtFixedRate(() -> {
                Weather weather = new Weather("Moscow");
                Date date=new Date();
                SimpleDateFormat dt = new SimpleDateFormat ("yyyy-MM-dd");
                SimpleDateFormat tm = new SimpleDateFormat ("kk:mm:ss");
                System.out.println("Writing to Data Base...");
                dBase.insert(weather.getCity(),weather.getWindSpeed(),
                             weather.getTemperature(),weather.getPressure(),
                             dt.format(date),tm.format(date));
            }, 0, 3600, TimeUnit.SECONDS);
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public void writeToFile(String path,String data){
        try(FileWriter writer = new FileWriter(path, true))
        {
            writer.write(data);
            writer.write("\r\n");
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void main(String[] args)
    {
        Server tcp = new Server(4747);
        try {
            tcp.run();
        }catch (Exception runtimeErr){
            System.out.println("Cant run the Server");
        }
    }
}

