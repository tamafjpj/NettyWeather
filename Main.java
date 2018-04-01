package com.company;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class Main
{
	private int port;

	public Main(int port){this.port=port;}//Конструктор на прослушку заданного порта
	/*public void init() throws Exception{
		EventLoopGroup workerGroup = new NioEventLoopGroup();//Группа событий (испульзуется для создания каналов между серверами и клиентом)
		try{
	Bootstrap bs = new Bootstrap();
		bs.channel(NioDatagramChannel.class)//используем datagram канал для общения
		.group(workerGroup)
		.handler(new ChannelInitializer<NioDatagramChannel>(){
			@Override
			public void initChannel(NioDatagramChannel ch)throws Exception {
				ch.pipeline().addLast(new ClientHandler());
			}
		});
		}finally{
			workerGroup.shutdownGracefully();
		}*/
    public static void main(String[] args)
    {
        Weather pyatigorsk=new Weather("https://yandex.ru/pogoda/pyatigorsk");
        System.out.println(pyatigorsk.getPressure());
    }
}


