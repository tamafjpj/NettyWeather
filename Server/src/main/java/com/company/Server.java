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
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Server {
    private int port;
    public Queue<String> tasks = new PriorityQueue<>(50);

    public Server(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();//Обрабатывает траффик
        try {
            //Создание сервера с заданной конфигурацией(неблокирующий,асинхронный,TCP протокол,простые сокеты)
            ServerBootstrap bs = new ServerBootstrap();
            bs.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            try {
                                //Строковый кодировщик->Строковый декодировщик->Обработчик
                                ch.pipeline().addLast(new StringEncoder(), new StringDecoder());
                                ch.pipeline().addLast(new SimpleServerHandler());

                            } catch (Exception IOException) {
                                System.out.println("Cant configure the server");
                                return;
                            }
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = bs.bind(port).sync();
            //Создание тасков и их запись в БД с заданным периодом
            ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
            e.scheduleAtFixedRate(() -> {
                new TaskGenerator(tasks);
                System.out.println(tasks);
                for (int i = 0; i < tasks.size(); i++) {
                    new TaskExecutor(tasks);
                }
            }, 0, 3600, TimeUnit.SECONDS);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void writeToFile(String path, String data) {
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(data);
            writer.write("\r\n");
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Server tcp = new Server(4747);
        try {
            tcp.run();
        } catch (Exception runtimeErr) {
            System.out.println("Cant run the Server");
        }
    }
}

