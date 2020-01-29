package frc.robot.colordebug;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.util.Color;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_8UC3;

public class ColorReporter {
    private ColorReporter() {
    }

    public static void reportColor(CvSource s, Color rgb) {
        Scalar sc = new Scalar(rgb.blue * 255, rgb.green * 255, rgb.red * 255);
        //System.out.println(sc);
        Mat m = new Mat(240, 320, CV_8UC3, sc);
        s.putFrame(m);
        m.release();
    }
}