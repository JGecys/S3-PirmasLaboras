import java.io.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by Jurgis on 2015-09-10.
 */
public class Stats {

    public static int mCrossWins = 0;
    public static int mCircleWins = 0;
    public static int mDraws = 0;

    public static void loadStats(){
        System.out.println("Loading statistics...");
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("stats.properties");
            prop.load(input);

            mCrossWins = Integer.parseInt(prop.getProperty("cross"));
            mCircleWins = Integer.parseInt(prop.getProperty("circle"));
            mDraws = Integer.parseInt(prop.getProperty("draw"));

        } catch (IOException | NumberFormatException ex) {
            System.out.println("File might not be found or bad format.");
        } finally {
            if (input != null) {
                try {
                    input.close();
                    System.out.println("Statistics loaded successfuly.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveStats(){
        System.out.println("Saving statistics...");
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("stats.properties");

            // set the properties value
            prop.setProperty("cross", String.valueOf(mCrossWins));
            prop.setProperty("circle", String.valueOf(mCircleWins));
            prop.setProperty("draw", String.valueOf(mDraws));

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                    System.out.println("Saving completed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    //Papraðë gynimui sukurti ðità funkcijà
    public static void masyvasPerPuse( int[] masyvas, int zingsnis ){
        int [] pirmaPuse = Arrays.copyOfRange(masyvas, 0, masyvas.length / 2);
        int [] antraPuse = Arrays.copyOfRange(masyvas, masyvas.length / 2, masyvas.length);
        for(int i = 0; i< pirmaPuse.length; i+=zingsnis){
            if(pirmaPuse[i] == antraPuse[i]){
                System.out.println("sutapes indeksas " + i);
            }
        }
    }

}
