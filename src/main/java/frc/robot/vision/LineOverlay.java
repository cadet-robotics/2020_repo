package frc.robot.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import frc.robot.Util;

public class LineOverlay implements VisionProcessor {
    private double minX, maxX, minY, maxY, x1, y1, angle;
    
    /**
     * Creates a line at some point with some angle (0 = horizontal)
     * 
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param x1
     * @param y1
     * @param angle Angle in radians
     */
    public LineOverlay(double minX, double maxX, double minY, double maxY, double x1, double y1, double angle) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.x1 = x1;
        this.y1 = y1;
        this.angle = angle;
    }
    
    public void setAngle(double a) { angle = a; }
    
    @Override
    public void process(Mat source, Mat dest, int width, int height) {
        // Map values from coords to pixels
        double x = Util.map(x1, minX, maxX, 0, width),
               y = Util.map(y1, minY, maxY, height, 0); // Y increases top to bottom
        
        // Each point is extended from the origin point by a bunch so it goes across the whole thing
        double xDistance = Util.map(Math.cos(angle), -1, 1, 0, width),  // Guarantee this goes off the screen no matter what, i hope
               yDistance = Util.map(Math.sin(angle), -1, 1, height, 0);
        
        Imgproc.line(source, new Point(x - xDistance, y - yDistance), new Point(x + xDistance, y + yDistance), new Scalar(0, 0, 0));
        
        source.copyTo(dest);
    }
}




