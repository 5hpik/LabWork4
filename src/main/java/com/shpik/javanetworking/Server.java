package com.shpik.javanetworking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private static ServerSocket ss;

    private static InputStream in1;
    private static InputStream in2;

    private static OutputStream out1;
    private static OutputStream out2;

    private static int matches_counter = 37; // Current number of matches

    private static boolean isEnded = false; // Is game ended

    public static void createServer()
    {
        try
        {
            System.out.println("Server start");

            ServerSocket SS = new ServerSocket(1234); // Creating the server socket

            System.out.println("Waiting for the connection of the first player...");
            Socket S = SS.accept();
            System.out.println("First player connected successfully!");
            InputStream IN1 = S.getInputStream();
            OutputStream OUT1 = S.getOutputStream();

            System.out.println("Waiting for the connection of the second player...");
            S = SS.accept();
            System.out.println("Second player connected successfully!");
            InputStream IN2 = S.getInputStream();
            OutputStream OUT2 = S.getOutputStream();

            // Saving sockets, input and output stream in private variables
            ss = SS;
            in1 = IN1;
            in2 = IN2;
            out1 = OUT1;
            out2 = OUT2;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void startGame()
    {
        String str1 = getRequest(in1); // Listening to the first input stream
        String str2 = getRequest(in2); // Listening to the second input stream
        if (str1.equals("Play") && str2.equals("Play"))
        {
            transfer("Go", 2, out1); // Transferring "your turn" to the first player
            transfer("Wait", 4, out2); // Transferring "turn of the opponent" to the second player
        }
    }

    public static void game(InputStream in, OutputStream out) throws IOException // Process of the game
    {
        int choice = in.read(); // Getting information about player choice from the client
        matches_counter -= choice; // Changing current matches counter

        out1.write(matches_counter); // Sending new number of matches to the client1
        out2.write(matches_counter); // Sending new number of matches to the client2

        if (matches_counter == 0)
        {
            isEnded = true; // Game was ended because number of matches equals null

            // Sending information about win or defeat to the clients
            if (out == out1)
            {
                transfer("Win", 3, out1);
                transfer("Defeat", 6, out2);
            }
            else
            {
                transfer("Win", 3, out2);
                transfer("Defeat", 6, out1);
            }
        }
        else
        {
            // Sending information about next turn to the clients
            if (out == out1)
            {
                transfer("Wait", 4, out1);
                transfer("Go", 2, out2);
            }
            else
            {
                transfer("Wait", 4, out2);
                transfer("Go", 2, out1);
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
        createServer(); // Creating the server
        startGame(); // Starting the game
        while (!ss.isClosed()) // While server socket is not closed
        {
            game(in1, out1); // Process of the game
            game(in2, out2); // Process of the game

            if (isEnded) // If game was ended
            {
                ss.close(); // Closing server socket
            }
        }
    }

    public static void transfer(String str, int len, OutputStream out)
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

    public static String getRequest(InputStream in)
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
