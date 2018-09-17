package itstap.edu.homenotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class MyService extends Service {
    private NotificationManager notifManager;
    PrintWriter pw;
    Socket s;
    ServerSocket ss;
    String msg;
    Scanner networkScanner;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {


//          ss =new ServerSocket(4568);
//            InetAddress   addr = InetAddress.getByName("192.168.1.218");
            ss =new ServerSocket(58974);
            Log.e("host",getIpAddress());
            Log.e("host2",ss.getLocalSocketAddress().toString() +": "+ss.getLocalPort()+"&"+ss.getInetAddress());
//            s=new Socket("10.0.2.2",6948);



        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("int",intent.getFlags()+"");
        if((msg = intent.getStringExtra("str"))!=null) {
            Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(msg!=null){
                        try {
                            s=ss.accept();
                            pw = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
                            pw.write(msg);
                            pw.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
        });
            tr.start();
            Log.d("tr1","start");
        }else {
            Thread tr2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        s=ss.accept();
                        pw = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
                        pw.write("Hi, it's I");
                        pw.flush();
                        networkScanner = new Scanner(s.getInputStream());
                        String fromClient = networkScanner.nextLine();
                        if (fromClient != null) {
                            Intent in = new Intent(getBaseContext(), MainActivity.class);
//                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            in.putExtra("f", fromClient);
                            PendingIntent pi = PendingIntent.getActivity(getBaseContext(), 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
                            createNotification("Notification Text ", pi);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });
            tr2.start();
            Log.d("tr2","start");

        }

//        InetAddress addr = null;

//            addr = InetAddress.getByName("106.02.12.12");
//            ss =new ServerSocket(2565,50,InetAddress.getByName("10.0.2.2"));
          /*  InetAddress locIP = InetAddress.getByName("0.0.0.0");
            ss = new ServerSocket(4568, 0, locIP);
            Log.e("host",ss.getLocalSocketAddress().toString());*/

        return START_STICKY;


    }

    public void createNotification(String aMessage, PendingIntent pi) {
        final int NOTIFY_ID = 1; // ID of notification
        String id = getString(R.string.default_notification_channel_id);
        String title = getString(R.string.default_notification_channel_title);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);


            builder.setContentTitle(aMessage)  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(this, id);

            builder.setContentTitle(aMessage)                           // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pi);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

    @Override
    public void onDestroy() {
        pw.close();
        networkScanner.close();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
