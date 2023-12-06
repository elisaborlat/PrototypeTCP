package com.example.prototypetcp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new thread
        Thread thread = new Thread(() -> {
            try {
                // Send the message
//                InetAddress address = InetAddress.getByName("192.168.1.10");
                Socket socket = new Socket("192.168.242.161", 23);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write("Bonsoir".getBytes());
                socket.close();
            } catch (IOException e) {
                Log.e("TCP", "Error sending message", e);
            }
        });

        // Create a new thread
        Thread thread1 = new Thread(() -> {

            try {
                Socket socket = new Socket("192.168.242.161", 23);
                // Obtenir un flux d'entrée
                InputStream inputStream = socket.getInputStream();

                System.out.println("Etat de connexion du socket : " + socket.isConnected());

                // Lire la réponse du serveur
                while (true) {
                byte[] data = new byte[1024];
                int bytesRead = inputStream.read(data);
                    // Si la lecture a échoué, on s'arrête
                    if (bytesRead == -1) {
                        break;
                    }

                // Décoder la réponse
                String response = new String(data, 0, bytesRead);
                System.out.println("My response" + response);}

                // Fermer le socket
                socket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        // Start the thread
        thread.start();
        thread1.start();
    }


}
