package shpik.jmt3;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ClientReadThread extends Thread
{
    InputStream in;

    ClientReadThread(InputStream in)
    {
        this.in = in;
    }

    @Override
    public void run()
    {
        while (true)
        {
            byte[] array = new byte[8];

            for (int i = 0; i < 8; i++)
            {
                try
                {
                    array[i] = (byte) in.read();
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
            //updater.update(ByteBuffer.wrap(array).getDouble());
        }
    }
}
