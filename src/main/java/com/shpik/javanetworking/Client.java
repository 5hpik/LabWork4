package com.shpik.javanetworking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client
{
    private final Socket s;

    private final InputStream in;

    private final OutputStream out;

    private boolean isClosed = false; // Is socket closed

    public Client()
    {
        try
        {
            System.out.println("Client start");

            Socket S = new Socket("127.0.0.1",1234); // Creating of the first client socket

            // Getting input and output streams
            InputStream IN = S.getInputStream();
            OutputStream OUT = S.getOutputStream();

            // Saving socket, input and output stream in private variables
            s = S;
            in = IN;
            out = OUT;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String play() // Starting the game
    {
        transfer("Play", 4); // Sending signal to the server to start the game
        return getRequest(); // Getting the answer, who goes first
    }

    public void take(int value) throws IOException // Sending information about what number of matches player chose to the server
    {
        out.write(value);
    }

    public int getMatchesCount() throws IOException // Getting information about current matches count from the server
    {
        return in.read();
    }

    public void close() throws IOException // Closing client socket
    {
        isClosed = true; // Changing value of the private boolean variable
        s.close(); // Closing client socket
    }

    public boolean isClosed() // Checking if the socket is closed
    {
        return isClosed;
    }

    public void transfer(String str, int len)
    {
        char[] chars = new char[7]; // 7 - max str length with 0 on the end

        for (int i = 0; i < 7; i++)
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

    public String getRequest()
    {
        char[] chars = new char[7]; // First char array to get information from the input stream
        char temp; // Every char from the input stream
        int len = 0; // Length of the string

        for (int i = 0; i < 7; i++) {
            try {
                temp = (char) in.read(); // Reading every char from the input stream
                if (temp != '0') {
                    chars[i] = temp; // Saving chars excepting '0'
                    len++; // Calculating length of the string
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        char[] string = new char[len]; // Second char array to get final result of the string transferred from the client
        System.arraycopy(chars, 0, string, 0, len); // Copying the values from the first char array
        return new String(string); // Creating of the result string object
    }
}

