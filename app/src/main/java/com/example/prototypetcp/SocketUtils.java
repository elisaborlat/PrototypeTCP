package com.example.prototypetcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketUtils {

    private static final int SOCKET_TIMEOUT = 10000;

    public static void send(String host, int port, String message) throws IOException {
        // Créer un socket TCP
        Socket socket = new Socket(host, port);
        socket.setSoTimeout(SOCKET_TIMEOUT);

        // Obtenir un flux de sortie
        OutputStream outputStream = socket.getOutputStream();

        // Convertir la chaîne de caractères en tableau d'octets
        byte[] bytes = message.getBytes();

        // Envoyer la chaîne de caractères
        outputStream.write(bytes);

        // Fermer le flux de sortie
        outputStream.close();

        // Fermer le socket
        socket.close();
    }

    public static String receive(String host, int port) throws IOException {
        // Créer un socket TCP
        Socket socket = new Socket(host, port);
        socket.setSoTimeout(SOCKET_TIMEOUT);

        // Obtenir un flux d'entrée
        InputStream inputStream = socket.getInputStream();

        System.out.println("Etat de connexion du socket : " + socket.isConnected());

        // Lire la réponse du serveur
        byte[] data = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(data)) != -1) {
            // Rien à faire
        }

        // Décoder la réponse
        String response = new String(data, 0, bytesRead);

        // Fermer le flux d'entrée
        inputStream.close();

        // Fermer le socket
        socket.close();

        return response;
    }

    /**
     * Effectue l'envoi et la réception de données via un socket TCP.
     *
     * @param host L'adresse IP du serveur.
     * @param port Le port du serveur.
     * @param message Le message à envoyer.
     * @return La réponse du serveur.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    public static String sendAndReceive(String host, int port, String message) throws IOException {
        // Envoyer le message
        send(host, port, message);

        // Recevoir la réponse
        return receive(host, port);
    }

    /**
     * Effectue l'envoi et la réception de données via un socket TCP en utilisant un thread.
     *
     * @param host L'adresse IP du serveur.
     * @param port Le port du serveur.
     * @param message Le message à envoyer.
     * @param callback Le callback qui sera appelé lorsque la réponse du serveur sera reçue.
     */
    public static void sendAndReceiveAsync(final String host, final int port, final String message, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = sendAndReceive(host, port, message);
                    callback.onResponse(response);
                } catch (IOException e) {
                    callback.onError(e);
                }
            }
        }).start();
    }

    /**
     * Interface du callback utilisé pour recevoir la réponse du serveur.
     */
    public interface Callback {

        /**
         * Appelé lorsque la réponse du serveur est reçue.
         *
         * @param response La réponse du serveur.
         */
        void onResponse(String response);

        /**
         * Appelé en cas d'erreur.
         *
         * @param e L'exception d'erreur.
         */
        void onError(IOException e);
    }
}
