package frc.robot.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import frc.robot.Util;

/**
 * Draws a line on the image based on angle and origin
 * 
 * @author Alex Pickering
 */
public class LineOverlay implements VisionProcessor {
    private double minX, maxX, minY, maxY, x, y, angle;
    
    private Scalar color;
    
    /**
     * Creates a line at some point with some angle (0 = horizontal) and a color
     * 
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param x
     * @param y
     * @param angle Angle in radians
     * @param color
     */
    public LineOverlay(double minX, double maxX, double minY, double maxY, double x, double y, double angle, Scalar color) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.color = color;
    }
    
    /**
     * Creates a line at some point with some angle (0 = horizontal)
     * 
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param x
     * @param y
     * @param angle Angle in radians
     */
    public LineOverlay(double minX, double maxX, double minY, double maxY, double x, double y, double angle) {
        this(minX, maxX, minY, maxY, x, y, angle, new Scalar(0, 0, 0));
    }
    
    public void setAngle(double a) { angle = a; }
    
    @Override
    public void process(Mat source, Mat dest, int width, int height) {
        
        // Get line endpoints such that they go outside the screen
        double xDist = Math.cos(angle) * (maxX - minX),
               yDist = Math.sin(angle) * (maxY - minY),
               x1 = x - xDist,
               y1 = y - yDist,
               x2 = x + xDist,
               y2 = y + yDist;
        
        // Convert to pixel coordinates
        x1 = Util.map(x1, minX, maxX, 0, width);
        x2 = Util.map(x2, minX, maxX, 0, width);
        y1 = Util.map(y1, minY, maxY, height, 0);
        y2 = Util.map(y2, minY, maxY, height, 0);
        
        //Imgproc.line(source, new Point(x - xDistance, y - yDistance), new Point(x + xDistance, y + yDistance), new Scalar(0, 0, 0));
        Imgproc.line(source, new Point(x1, y1), new Point(x2, y2), color);
        
        source.copyTo(dest);
    }
}




