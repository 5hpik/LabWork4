package shpik.jmt3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server
{
    class Server {
        static Calc c; // Object of the calc loop to do some things with loop

        //Information of the server socket
        static ServerSocket ss;
        static Socket s;
        static InputStream in;
        static OutputStream out;

        static double value;

        public static void getRequest()
        {
            new Thread(() -> {
                synchronized (System.out) {
                    char[] chars = new char[12]; // First char array to get information from the input stream
                    char temp; // Every char from the input stream
                    int len = 0; // Length of the string
                    for (int i = 0; i < 12; i++) {
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
                    for (int i = 0; i < len; i++) {
                        string[i] = chars[i]; // Copying the values from the first char array
                    }
                    String str = new String(string); // Creating of the result string object


                    // Do something with the loop depending on the received message
                    switch (str) {
                        case ("CalcRestart") -> {
                            c.Restart();
                            restart();
                        }
                        case ("CalcResume") -> c.Resume();
                        case ("CalcPause") -> c.Pause();
                        case ("CalcStop") -> c.Stop();
                    }
                }
            }).start();
        }

        public static void restart() {
            // Restarting the loop after its destruction by stop button
            c = new Calc();
            c.start();
        }

        public static void sendValue(double value) throws IOException {
            byte[] array = new byte[8];
            ByteBuffer.wrap(array).putDouble(value);
            out.write(array);
        }
    }
}
