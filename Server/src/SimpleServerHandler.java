package com.company;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.sql.SQLException;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        dbService db = dbService.INSTANCE;
        System.out.print("Request: " + o.toString()+" - ");
        switch (o.toString()){
            case "1":
            {
                System.out.println("Weather All");
                try {
                    db.rs = db.stmt.executeQuery("select * from weather");
                    while (db.rs.next()) {
                        ctx.writeAndFlush(db.select());
                    }
                    db.rs.close();
                    ctx.close();

                } catch (SQLException sql) {
                    System.out.println("Error");
                }
            }
            case "2":
            {
                System.out.println("Weather Last");
                try {
                    db.rs=db.stmt.executeQuery("select * from weather order by id desc limit 1;");
                    while (db.rs.next()) {
                        ctx.writeAndFlush(db.select());
                    }
                    db.rs.close();
                    ctx.close();

                } catch (SQLException sql) {
                    System.out.println("Error");
                }
                ctx.close();
            }
            case "3":
            {
                System.out.println("Weather Delete");
                db.delete();
                ctx.writeAndFlush("Deleted!");
                ctx.close();
            }
        }
        if(o.toString().contains("stat")){
            String [] args =splitByWords(o.toString());
            try {
                db.rs = db.stmt.executeQuery(String.format("select * from weather where date between ' %s ' and ' %s' ;", args[1], args[2]));
                while (db.rs.next()) {
                    ctx.writeAndFlush(db.select());
                }
                db.rs.close();
                //ctx.close();
            }
            catch (SQLException e){ctx.writeAndFlush("Select error");}
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

