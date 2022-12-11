package shpik.jmt3.Server;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class Model {
    private static ServerSocket ss;
    private static Socket s;

    private static InputStream in;
    private static OutputStream out;

    private static Calc c;

    public static void main(String[] args) throws IOException {
        try {
            ss = new ServerSocket(1111);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            s = ss.accept();
            System.out.println("Client accepted");
            in = s.getInputStream();
            out = s.getOutputStream();
        }
        catch (Exception e){
            System.out.println("Error: " + e);
        }
        if(c != null) c.CalcStop();

        Runnable TakeThread = () ->
        {
            Thread.currentThread().setName("TakeThread");
            System.out.println("Server Take");
            while (true) {

                    char[] chars = new char[11];
                    char temp;
                    int len = 0;
                    for (int i = 0; i < 11; i++) {
                        try {
                            temp = (char) in.read();
                            if (temp != '0') {
                                chars[i] = temp;
                                len++;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    char[] string = new char[len];
                    for (int i = 0; i < len; i++) {
                        string[i] = chars[i];
                    }
                    String str = new String(string);
                    System.out.println(str);

                    switch (str) {
                        case ("CalcRestart") -> {
                            if (c!=null) c.CalcStop();
                            c = new Calc();
                            c.start();
                            System.out.println("Calc Thread Started"); }
                        case ("CalcResume") -> c.CalcResume();
                        case ("CalcPause") -> c.CalcPause();
                        case ("CalcStop") -> c.CalcStop();
                    }
                }

        };

        Thread Take = new Thread(TakeThread);
        Take.start();
        System.out.println("Take Thread Started");

        c = new Calc();
        c.start();
        System.out.println("Calc Thread Started");
    }

    public static void Send(double value) throws IOException
    {
        System.out.println("Server Send");
        byte[] array = new byte[8];
        ByteBuffer.wrap(array).putDouble(value);
        out.write(array);
    }
}


class Calc extends Thread{

    boolean Stop;
    boolean Pause;
    boolean Resume;

    Double updater = 0.0;


    void CalcStop(){
        Stop=true;
    }

    void CalcPause(){
        Pause=true;
        Resume=false;
    }

    void CalcResume(){
        Pause=false;
        Resume=true;
        synchronized (System.out)
        {
            System.out.notify();
        }
    }

    @Override
    public void run() {
        for(int i = 0; i< 1000; i++){
            if (Stop) {
                System.out.println("Calc Thread break");
                break;
            }
            if (Pause)
            {
                try {
                    synchronized (System.out)
                    {
                        System.out.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            updater = (double) i / 1000;
            try {
                Model.Send(updater);
            }catch (Exception e)
            {
                System.out.println("Error: " + e);
            }
            System.out.println(updater);
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

