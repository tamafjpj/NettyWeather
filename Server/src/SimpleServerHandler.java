package com.company;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        System.out.print("Request: " + o.toString()+" - ");
        if (o.toString().equals("1")) {
            System.out.println("Weather forecast.");
            Weather weather = new Weather("Pyatigorsk");
            Date date=new Date();
            SimpleDateFormat dt = new SimpleDateFormat ("dd-MM-yyyy");
            SimpleDateFormat tm = new SimpleDateFormat ("kk:mm:ss");
            String buf = "Date: "+ dt.format(date)+"\n"
                        +"Time: "+ tm.format(date)+"\n"
                        +"Temp: " + weather.getTemperature() + "\n"
                        +"Wind Speed: " + weather.getWindSpeed() + "\n"
                        +"Humidity: " + weather.getHumidity() +"%"+"\n"
                        +"Pressure: " + weather.getPressure();
            ctx.writeAndFlush(buf);
            ctx.close();
        }
        if (o.toString().equals("2")){
            ctx.writeAndFlush("Request: 2");
            ctx.close();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

