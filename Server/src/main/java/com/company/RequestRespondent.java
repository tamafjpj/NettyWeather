package com.company;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Queue;

public class RequestRespondent implements Runnable {
    private Queue<Request> requests;
    public Thread executor;
    private String[] cities=new String[]{"Moscow","Kaliningrad","Pyatigorsk","Saint-petersburg","Chelyabinsk",
            "Nizhny-novgorod","Novosibirsk","Vladivostok","Krasnoyarsk","Kazan",
            "Ufa","Rostov-na-donu","Samara","Yekaterinburg","Omsk"};
    public RequestRespondent(Queue<Request> requests){
        this.requests=requests;
        this.executor=new Thread(this,"RequestExecutor");
    }

    @Override
    public void run() {
        respondRequests();
    }

    private void respondRequests(){
        Request currRequest;
        while(!executor.isInterrupted()){
            if(!requests.isEmpty()) {
                synchronized (requests) {
                    if(!requests.isEmpty()){
                        currRequest=requests.poll();
                        System.out.println("Request: " + currRequest.getRequest());
                        respond(currRequest);
                    }
                }
            }
        }
    }
    public String[] splitByWords(String str){
        return str.split("\\s");
    }
    private void respond(Request currRequest){
        String inQuery="";
        dbService db = dbService.INSTANCE;
        if(currRequest.getRequest().contains("weather")){
            String [] args =splitByWords(currRequest.getRequest());
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
                                currRequest.getCtx().writeAndFlush("MIN: "+min+ " MAX: "+max+" AVG: "+avg);
                                inQuery=String.format("select * from weather where city = '%s' and date between '%s' and '%s';", args[2], args[3],args[4]);
                            }catch (SQLException e){e.printStackTrace();//ctx.close();
                            }
                        }
                    }
                    if(!isInList){currRequest.getCtx().writeAndFlush("No such city in list");currRequest.getCtx().close();}
                }
                break;
                case "delete":
                {
                    db.delete();
                    currRequest.getCtx().writeAndFlush("DELETED ALL DATA");
                    inQuery="select * from weather";
                    currRequest.getCtx().close();
                }
                default:{currRequest.getCtx().writeAndFlush("No such command.");currRequest.getCtx().close();}
            }
        }
        try {
            ArrayList<String> rs=db.select(inQuery);
            for (int i=0;i<rs.size();i++) {
                currRequest.getCtx().writeAndFlush(rs.get(i));
            }
            db.rs.close();
            //ctx.close();
        } catch (SQLException sql) {
            System.out.println("Error");
        }
    }

}
