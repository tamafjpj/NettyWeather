package com.company;

public class Main
{
    public static void main(String[] args)
    {
        Weather pyatigorsk=new Weather("https://yandex.ru/pogoda/pyatigorsk");
        Server tcp = new Server(8080);
        try {
            tcp.run();
        }catch (Exception runtimeErr){
        System.out.println("Cant run the Server");
        }

        System.out.println(pyatigorsk.getPressure());
    }
}


