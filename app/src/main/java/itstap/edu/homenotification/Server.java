package itstap.edu.homenotification;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
     private int port;
        private ServerSocket ss;
        public Server(int port)
        {
            this.port = port;
            try
            {
                ss = new ServerSocket(port);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public void listen()
        {
            System.out.println("connect for listening");
            while(true)
            {
                try
                {
                    Socket s = ss.accept();
                    Client c = new Client(s);
                    Thread t = new Thread(c);
                    t.start();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }



}
