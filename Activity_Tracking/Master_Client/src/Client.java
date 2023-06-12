import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Client extends Thread {

    private static int masterPort;
    private static String masterHost;
    private Socket socket;
    private String gpxFilePath;

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: java WorkerNode <master_host> <master_port> <gpx_file_path>");
            System.exit(1);
        }

        masterHost = args[0];
        masterPort = Integer.parseInt(args[1]);
        String gpxFilePath = args[2];
        new Client(gpxFilePath).start();
    }

    public Client(String gpxFilePath) {
        this.gpxFilePath = gpxFilePath;
    }
    @Override
     public void run() {
        try {
            socket = new Socket(masterHost, masterPort);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            // Resolve the relative path to the current working directory
            Path currentWorkingDir = Paths.get("").toAbsolutePath();
            Path fullPath = currentWorkingDir.resolve(gpxFilePath);

            // Read the content of the GPX file
            byte[] fileContent = Files.readAllBytes(fullPath);

            // Create an ExtendedGPXFile object with the desired data
            ExtendedGPXFile gpxFile = new ExtendedGPXFile();
            gpxFile.setFilename(gpxFilePath);
            gpxFile.setFileContent(fileContent);

            // Send the ExtendedGPXFile object to the MasterServer
            outputStream.writeObject(gpxFile);
            outputStream.flush();
            System.out.println("GPX file sent to MasterServer: " + gpxFile.getFilename());

              try {
                HashMap<Integer, ArrayList<String>> result = (HashMap<Integer, ArrayList<String>>) inputStream.readObject();
                System.out.println("Received results:\n");
                for (Map.Entry<Integer, ArrayList<String>> entry : result.entrySet()) {
                    System.out.println("Thread: " + entry.getKey());
                    for (String str : entry.getValue()) {
                        System.out.println(str);
                    }
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Error reading result from server");
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    private void closeResources() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}