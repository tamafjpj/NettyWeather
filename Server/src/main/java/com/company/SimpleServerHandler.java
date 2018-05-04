package com.company;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) {
        dbService db = dbService.INSTANCE;
        String[] cities=new String[]{"Moscow","Kaliningrad","Pyatigorsk","Saint-petersburg","Chelyabinsk",
                "Nizhny-novgorod","Novosibirsk","Vladivostok","Krasnoyarsk","Kazan",
                "Ufa","Rostov-na-donu","Samara","Yekaterinburg","Omsk"};
        System.out.println("Request: " + o.toString());
        String inQuery="";
        if(o.toString().toLowerCase().contains("weather")){
            String [] args =splitByWords(o.toString());
            switch (args[1]){
                case "all":inQuery="select * from weather";
                break;
                case "stat":
                {
                    boolean isInList=false;
                    for(String i:cities){
                        if(args[2].toLowerCase().equals(i.toLowerCase())){
                            isInList=true;
                            try {
                                String min="",max="",avg="";
                                db.rs=db.stmt.executeQuery(String.format("select min(temperature) as temperature from weather where city = '%s' and date between '%s' and '%s';",args[2],args[3],args[4]));
                                if(db.rs.next())min=db.rs.getString(1);
                                db.rs=db.stmt.executeQuery(String.format("select max(temperature) as temperature from weather where city = '%s' and date between '%s' and '%s';",args[2],args[3],args[4]));
                                if(db.rs.next())max=db.rs.getString(1);
                                db.rs=db.stmt.executeQuery(String.format("select avg(temperature) as temperature from weather where city = '%s' and date between '%s' and '%s';",args[2],args[3],args[4]));
                                if(db.rs.next())avg=db.rs.getString(1);
                                ctx.writeAndFlush("MIN: "+min+ " MAX: "+max+" AVG: "+avg);
                                inQuery=String.format("select * from weather where city = '%s' and date between '%s' and '%s';", args[2], args[3],args[4]);
                            }catch (SQLException e){e.printStackTrace();//ctx.close();
                                 }
                        }
                    }
                    if(!isInList){ctx.writeAndFlush("No such city in list");ctx.close();}
                }
                break;
                case "delete":
                {
                    db.delete();
                    ctx.writeAndFlush("DELETED ALL DATA");
                    inQuery="select * from weather";
                    ctx.close();
                }
                default:{ctx.writeAndFlush("No such command.");ctx.close();}
            }
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

