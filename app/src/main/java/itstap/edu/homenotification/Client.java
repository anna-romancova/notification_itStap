package itstap.edu.homenotification;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client implements Runnable {
    private Socket s;
    private Scanner networkScanner;
    private PrintWriter pw;

    public Client(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            networkScanner = new Scanner(s.getInputStream());
            pw = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
            pw.write("Hello \n");
            pw.flush();
            while (true) {
                String fromClient = networkScanner.nextLine();
                MainActivity.newTextView(fromClient);
                if (fromClient.equals("exit")) break;
                System.out.println("Answer:");
                String toClient = MainActivity.messageE.toString();
                pw.write(toClient + "\n");
                pw.flush();
            }
            pw.close();
            networkScanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

