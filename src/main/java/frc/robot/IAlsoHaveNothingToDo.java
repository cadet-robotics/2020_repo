package frc.robot;

import edu.wpi.first.wpilibj.Filesystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

/**
 * Hee Hoo. Penut.
 *
 * @author Matt Robinson
 */
public class IAlsoHaveNothingToDo {

    public static void bruh(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String ln = "";
            try {
                while((ln = br.readLine()) != null) {
                    System.out.println(ln);
                }
            } catch(IOException e) {
                System.out.println(":(");
            } finally {
                try {
                    br.close();
                } catch(IOException e) {
                    System.out.println("penut failed");
                }
            }

        } catch (Exception e) {
            System.out.println("[EXCEPTION] " + e);
        }
    }
}
