package GPXFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GPXFile implements Serializable {

    private static final long serialVersionUID = -8051146778562959215L;

    private String userName;
    private float distance;
    private float speed;
    private float hill_climb; // anavasi
    private long time;

    private byte[] GPXFileExtract;

    //default constructor
    public GPXFile() {}

    //Constructor
    public GPXFile(String userName, float distance, float speed, float hill_climb, long time, byte[] GPXFileExtract)
    {
        this.userName = userName;
        this.distance = distance;
        this.hill_climb = hill_climb;
        this.speed = speed;
        this.time = time;
        this.GPXFileExtract = GPXFileExtract;
    }

    //Setters
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }

    public void setHill_climb(float hill_climb)
    {
        this.hill_climb = hill_climb;
    }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public void setGPXFileExtract(byte[] GPXFileExtract)
    {
        this.GPXFileExtract = GPXFileExtract;
    }

    //Getters
    public String getUserName()
    {
        return this.userName;
    }

    public float getDistance()
    {
        return this.distance;
    }

    public float getHill_climb()
    {
        return this.hill_climb;
    }

    public float getSpeed()
    {
        return this.speed;
    }

    public long getTime()
    {
        return this.time;
    }

    public byte[] getGPXFileExtract()
    {
        return this.GPXFileExtract;
    }


    public static List<GPXFile> chunks(byte[] array, GPXFile gpx) throws IOException {

        List<GPXFile> ret = new ArrayList<GPXFile>();

        int bytes = array.length;
        int loops = 0;
        if (bytes % 512000 == 0) {
            loops = bytes / 512000;
        } else {
            loops = (bytes / 512000) + 1;
        }


        for (int i = 0; i < loops; i++) {

            if (i == loops - 1) {

                byte[] a = new byte[array.length - i * 512000];
                for (int j = 0; j < a.length; j++) {
                    a[j] = array[(i * 512000) + j];
                }
                GPXFile m = new GPXFile(gpx.getUserName(),gpx.getDistance(),gpx.getHill_climb(),gpx.getSpeed(),gpx.getTime(),a);
                ret.add(m);

            } else {
                byte[] a = new byte[512000];
                for (int j = 0; j < 512000; j++) {
                    a[j] = array[(i * 512000) + j];
                }
                GPXFile m = new GPXFile(gpx.getUserName(),gpx.getDistance(),gpx.getHill_climb(),gpx.getSpeed(),gpx.getTime(),a);
                ret.add(m);
            }
        }
        return ret;
    }

    public GPXFile reproduce(List<GPXFile> list) {

        int bounds = (list.size() - 1) * 512000;
        int last = list.get(list.size()-1).getGPXFileExtract().length;
        bounds = bounds + last;


        byte[] array = new byte[bounds];

        int counter = 0;
        for (int i = 0; i < list.size(); i++) {
            byte[] chunk = list.get(i).getGPXFileExtract();
            for (int j = 0; j < chunk.length; j++) {
                array[counter] = chunk[j];
                counter++;
            }
        }

        GPXFile gpxfile = new GPXFile();
        gpxfile.setUserName(list.get(0).getUserName());
        gpxfile.setDistance(list.get(0).getDistance());
        gpxfile.setHill_climb(list.get(0).getHill_climb());
        gpxfile.setSpeed(list.get(0).getSpeed());
        gpxfile.setGPXFileExtract(array);

        return gpxfile;
    }

    public static void createGPX(GPXFile m, String path) throws IOException {

        File f = null;

        try	{
            f = new File(path);
        }
        catch (NullPointerException e) {
            System.out.println ("Can't create file");
        }

        FileOutputStream outputstream = new FileOutputStream(f);
        outputstream.write(m.getGPXFileExtract());

        outputstream.close();
    }
}
