package com.company;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        dbService db = dbService.INSTANCE;
        System.out.print("Request: " + o.toString()+" - ");
        String inQuery="";
        if(o.toString().compareTo("1")==0)inQuery="select * from weather";
        else if(o.toString().compareTo("2")==0)inQuery="select * from weather order by id desc limit 1;";
        else if(o.toString().compareTo("3")==0){db.delete();ctx.writeAndFlush("Deleted!");}
        else if(o.toString().contains("stat")){
            String [] args =splitByWords(o.toString());
            inQuery=String.format("select * from weather where date between ' %s ' and ' %s' ;", args[1], args[2]);
        }
        try {
                ArrayList<String>rs=db.select(inQuery);
                for (int i=0;i<rs.size();i++) {
                    ctx.writeAndFlush(rs.get(i));
                }
                db.rs.close();
                //ctx.close();
                } catch (SQLException sql) {
                    System.out.println("Error");
                }
        }

    public String[] splitByWords(String str){
        return str.split("\\s");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

