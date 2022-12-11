package shpik.jmt3;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;

public class Model implements IModel{
    static Updatable updater;
    Socket s;
    InputStream in;
    OutputStream out;

    @Override
    public void some_calc(Updatable updater) {
        this.updater = updater;
        if (s!=null) Send("CalcRestart", 11);
        try {
            if (s==null) {
                s = new Socket("127.0.0.1", 1111);
                System.out.println("Connected");
                in = s.getInputStream();
                out = s.getOutputStream();
                TakeDataThread TakeData = new TakeDataThread();
                new Thread(TakeData, "TakeThread").start();
            }
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }

    }

    @Override
    public void Send(String str, int len)
    {
        System.out.println("Client Send");
        char[] chars = new char[11];
        for (int i = 0; i < 11; i++)
        {
            chars[i] = (i < len) ? str.charAt(i) : '0';
            try
            {
                out.write(chars[i]);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
    class TakeDataThread implements Runnable {

        @Override
        public void run() {
            System.out.println("Client Take");
            while (true) {
                byte[] array = new byte[8];
                for (int i = 0; i < 8; i++) {
                    try {
                        array[i] = (byte) in.read();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                updater.update(ByteBuffer.wrap(array).getDouble());
            }
        }
    }

    @Override
    public void stop(){
        Send("CalcStop",8);
    }

    @Override
    public void pause() {
        Send("CalcPause",9);
    }

    @Override
    public void resume() {
        Send("CalcResume",10);
    }
}




