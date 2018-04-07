package com.company;

public class Main {
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
