package frc.robot.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import frc.robot.Util;

public class LineOverlay implements VisionProcessor {
    private double minX, maxX, minY, maxY, x1, y1, m;
    
    /**
     * Creates a line with the form y - y1 = m(x - x1)
     * 
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param x1
     * @param y1
     * @param m Slope
     */
    public LineOverlay(double minX, double maxX, double minY, double maxY, double x1, double y1, double m) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.x1 = x1;
        this.y1 = y1;
        this.m = m;
    }
    
    @Override
    public void process(Mat source, Mat dest, int width, int height) {
        // same stuff as ParabolaOverlay but with a different equation
        Point[] points = new Point[width];
        
        for(int i = 0; i < width; i++) {
            // use y = m(x - x1) + y1
            // map from pixel to x
            double x = Util.map(i, 0, width - 1, minX, maxX),
                   y = (m * (x - x1)) + y1;
            
            // map from y to pixel
            y = height - Util.map(y, minY, maxY, 0, height);
            
            points[i] = new Point(i, (int) y);
        }
        
        // plot the points
        for(int i = 1; i < points.length; i++) {
            Imgproc.line(source, points[i - 1], points[i], new Scalar(0, 0, 0));
        }
        
        source.copyTo(dest);
    }
}




