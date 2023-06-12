import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Worker {
    private final String masterHost;
    private final int masterPort;

    public Worker(String masterHost, int masterPort) {
        this.masterHost = masterHost;
        this.masterPort = masterPort;
    }

    public void start() {
        try {
            Socket socket = new Socket(masterHost, masterPort);
            System.out.println("Connected to master node at " + masterHost + ":" + masterPort);

            while (true) {
                // Receiving data
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                String data = (String) in.readObject();

                // Creating new thread to process data
                WorkerThread workerThread = new WorkerThread(socket, data);
                workerThread.start();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class WorkerThread extends Thread {
        private final Socket socket;
        private final String data;

        public WorkerThread(Socket socket, String data) {
            this.socket = socket;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                Map<String, String> results = processData(data);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(results);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Map<String, String> processData(String data) {
            // Here you could perform computations on the data
            Map<String, String> results = new HashMap<>();
            results.put("data", data);  // For now, simply echo the received data
            return results;
        }
    }

    public static void main(String[] args) {
        Worker worker = new Worker("localhost", 8080);
        worker.start();
    }
}