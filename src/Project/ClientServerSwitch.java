package Project;

import calculator.Calculator;
import client.Client;
import server.Server;

import java.util.Scanner;

public class ClientServerSwitch
{
    public ClientServerSwitch()
    {
        int type = 0;

        Scanner in = new Scanner(System.in);

        while (type == 0)
        {
            System.out.println("Please specify whether you want to create a server or a client: \n");
            System.out.println("1 - client\n2 - server");
            type = in.nextInt();

            if (type != 1 && type != 2)
            {
                System.out.println("Error! Wrong input\n");
                type = 0;
            }
        }

        switch (type)
        {
            case 1:
                Client clnt = new Client();
                break;
            case 2:
                Calculator calc = new Calculator();
                Server srv = new Server(1234);
                break;
        }
    }
}
