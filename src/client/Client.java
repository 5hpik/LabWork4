package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Client
{
    InputStream in;
    OutputStream out;
    public Client()
    {
        System.out.print("Initialised a new client\n");

        try
        {
            Socket sckt = new Socket("127.0.0.1",1234);
            System.out.println("Local port: " +  sckt.getLocalPort());
            System.out.println("Remote port: " + sckt.getPort());

            in = sckt.getInputStream();
            out = sckt.getOutputStream();

            try
            {
                Scanner s = new Scanner(System.in);

                while (true)
                {
                    String[] info = new String[3];

                    System.out.println("Enter the first number: ");
                    info[0] = s.nextLine();

                    System.out.println("Enter the second number: ");
                    info[1] = s.nextLine();

                    System.out.println("Enter the sign of the operation (+, -, *, /): ");
                    info[2] = s.nextLine();

                    if (info[1].equals("0") && info[2].equals("/"))
                    {
                        System.out.println("Error! The number cannot be divided by null!");
                    }
                    else
                    {
                        byte[] first_array = new byte[8];
                        ByteBuffer.wrap(first_array).putDouble(Double.parseDouble(info[0]));
                        out.write(first_array);

                        byte[] second_array = new byte[8];
                        ByteBuffer.wrap(second_array).putDouble(Double.parseDouble(info[1]));
                        out.write(second_array);

                        out.write(info[2].getBytes());

                        byte[] array = new byte[8];

                        for (int i = 0; i < 8; i++)
                        {
                            array[i] = (byte) in.read();
                        }

                        double result = ByteBuffer.wrap(array).getDouble();

                        System.out.println("Result: " + info[0] + " " + info[2] + " " + info[1] + " = " + result);
                    }
                }
            }
            catch (IOException e)
            {

            }

            System.out.print("Client connection terminated\n");

            in.close();
            out.close();
            sckt.close();
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e);
        }
    }
}
