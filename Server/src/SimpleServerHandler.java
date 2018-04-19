package com.company;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.sql.SQLException;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        dbService db = dbService.INSTANCE;
        System.out.print("Request: " + o.toString()+" - ");
        if (o.toString().equals("1")) {
            System.out.println("Weather All");
            try {
                db.rs = db.stmt.executeQuery("select * from weather");
                while (db.rs.next()) {
                    ctx.writeAndFlush(db.select("All"));
                }
                db.rs.close();
                ctx.close();

            } catch (SQLException sql) {
                System.out.println("Error");
            }
        }
        if (o.toString().equals("2")) {
                System.out.println("Weather Last");
                try {
                    db.rs=db.stmt.executeQuery("select * from weather order by id desc limit 1;");
                    while (db.rs.next()) {
                        ctx.writeAndFlush(db.select("Last"));
                    }
                    db.rs.close();
                    ctx.close();

                } catch (SQLException sql) {
                    System.out.println("Error");
                }
                ctx.close();
            }
        if (o.toString().equals("3")) {
            System.out.println("Weather Delete");
            db.delete();
            ctx.writeAndFlush("Deleted!");
            ctx.close();
        }
        }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

