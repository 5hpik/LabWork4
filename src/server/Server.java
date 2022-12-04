package server;

import calculator.Calculator;

import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;

public class Server
{
    int port;

    InputStream in;
    OutputStream out;

    public Server(int port)
    {
        this.port = port;

        try
        {
            System.out.print("Initialised a new server on port + " + port + "\n");
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Waiting for connection...\n");
            Socket s = ss.accept();

            in = s.getInputStream();
            out = s.getOutputStream();

            try
            {
                System.out.print("A new server thread has been started: \n");

                while (true)
                {
                    double[] numbers = new double[3];

                    for (int i = 0; i < 2; i++)
                    {
                        System.out.println("Waiting for the number\n");

                        byte[] array = new byte[8];

                        for (int j = 0; j < 8; j++)
                        {
                            array[j] = (byte) in.read();
                        }

                        double dubNumber = ByteBuffer.wrap(array).getDouble();

                        numbers[i] = dubNumber;
                    }

                    double first = numbers[0];
                    double second = numbers[1];
                    char operation = (char) in.read();

                    System.out.println("The operation type is: " + operation + "\n");

                    double result;

                    switch (operation)
                    {
                        case ('+'):
                            result = Calculator.addition(first, second);
                            break;
                        case ('-'):
                            result = Calculator.subtraction(first, second);
                            break;
                        case ('*'):
                            result = Calculator.multiplication(first, second);
                            break;
                        case ('/'):
                            result = Calculator.division(first, second);
                            break;
                        default:
                            result = 0;
                            break;
                    };

                    System.out.println(result);

                    byte[] answer = new byte[8];
                    ByteBuffer.wrap(answer).putDouble(result);
                    out.write(answer);
                }
            }
            catch (IOException e)
            {
                System.out.print("Error: " + e);
            }

            in.close();
            out.close();
            s.close();
            ss.close();

        }
        catch (Exception e)
        {
            System.out.println("Error: " + e);
        }
    }
}
