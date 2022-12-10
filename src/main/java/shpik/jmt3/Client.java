package shpik.jmt3;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client
{
    Socket s;
    InputStream in;
    OutputStream out;


    public void initClient()
    {

    }

    public void sendCommand(String str, int len)
    {
        char[] chars = new char[12]; // 12 - max str length with 0 on the end
        for (int i = 0; i < 12; i++)
        {
            // Creating an array of characters from a query string and filling the empty cells with '0'
            chars[i] = (i < len) ? str.charAt(i) : '0';
            try
            {
                out.write(chars[i]); // Transfer every char in the array like an integer number
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public void restart() {
        sendCommand("CalcRestart", 11);
    }

    public void pause() {
        sendCommand("CalcPause", 9);
    }

    public void resume() {
        sendCommand("CalcResume", 10);
    }

    public void stop() {
        sendCommand("CalcStop", 8);
    }

    public void readData()
    {
        Thread crt = new ClientReadThread(in);
        crt.run();
    }
}
