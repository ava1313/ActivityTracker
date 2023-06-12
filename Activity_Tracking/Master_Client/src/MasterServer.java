import java.io.*;
import java.net.*;
import java.util.*;

public class MasterServer extends Thread {
    private final List<Socket> workerSockets = new ArrayList<>(); // List to store worker sockets

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Give number of workers");
        Scanner scanner=new Scanner(System.in);
        String input = scanner.nextLine();
        int workerNum = Integer.parseInt(input);
        new MasterServer().openServer(workerNum);
    }

    public void openServer(int workerNum) throws IOException, ClassNotFoundException {
        ServerSocket workerServerSocket = new ServerSocket(8080);
        int j=0;
        while (j<workerNum) {
            System.out.println("Server started and listening on port 8080");
            System.out.println("Waiting for worker no."+(j+1));
            Socket workerConnectionSocket = workerServerSocket.accept();
            System.out.println("Worker no."+(j+1)+" connected");
            synchronized (workerSockets) {
                workerSockets.add(workerConnectionSocket);
            }
            j++;
        }

        // Listening to the client.
        ServerSocket clientServerSocket = new ServerSocket(5056);
        System.out.println("Server started and listening for client on port 5056");
        Socket clientSocket = clientServerSocket.accept();
        System.out.println("Client connected");

        ObjectInputStream clientIn = new ObjectInputStream(clientSocket.getInputStream());
        ExtendedGPXFile gpxFile = (ExtendedGPXFile) clientIn.readObject();
        System.out.println("Received GPX file from client: " + gpxFile.getFilename());

        // Forwarding the file to a worker.
        ObjectOutputStream workerOut = new ObjectOutputStream(workerSockets.get(0).getOutputStream());
        workerOut.writeObject(gpxFile);
        workerOut.flush();
        System.out.println("Forwarded GPX file to worker");

        // Waiting for the result from a worker.
        ObjectInputStream workerIn = new ObjectInputStream(workerSockets.get(0).getInputStream());
        GPXInfos results = (GPXInfos) workerIn.readObject();
        System.out.println("Received results from worker");

        // Sending the result back to the client.
        ObjectOutputStream clientOut = new ObjectOutputStream(clientSocket.getOutputStream());
        clientOut.writeObject(results);
        clientOut.flush();
        System.out.println("Sent results back to client");
    }
}
