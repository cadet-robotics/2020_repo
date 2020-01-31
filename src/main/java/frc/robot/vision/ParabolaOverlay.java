package frc.robot.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import frc.robot.Util;

/**
 * Testing overlay 
 * 
 * @author Alex Pickering
 */
public class ParabolaOverlay implements VisionProcessor {
    
    private double minX, maxX, minY, maxY,
                   a, b, c;
    
    /**
     * Creates the overlay with graph bounds and initial parabola
     * 
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param a
     * @param b
     * @param c
     */
    public ParabolaOverlay(double minX, double maxX, double minY, double maxY, double a, double b, double c) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    /**
     * Creates the overlay with its graph bounds and a default (x^2) parabola
     * <p> These values are the range of graphed values
     * 
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     */
    public ParabolaOverlay(double minX, double maxX, double minY, double maxY) {
        this(minX, maxX, minY, maxY, 1, 0, 0);
    }
    
    /**
     * Changes the parabola
     * 
     * @param a
     * @param b
     * @param c
     */
    public void setParabola(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    /**
     * Sets the parabola with the format {a, b, c}
     * 
     * @param par The parabola as the array {a, b, c}
     */
    public void setParabola(double[] par) {
        a = par[0];
        b = par[1];
        c = par[2];
    }
    
    public void setA(double a) { this.a = a; }
    public void setB(double b) { this.b = b; }
    public void setC(double c) { this.c = c; }
    
    /**
     * Gets the parabola as the array {a, b, c}
     * 
     * @return The parabola as an array
     */
    public double[] getParabola() {
        return new double[] {a, b, c};
    }
    
    public double getA() { return a; }
    public double getB() { return b; }
    public double getC() { return c; }
    
    @Override
    public void process(Mat source, Mat dest) {
        // Assemble points
        Point[] points = new Point[320]; // camera width
        for(int i = 0; i < points.length; i++) {
            double x = Util.map(i, 0, points.length - 1, minX, maxX); // map to x value
            double y = (a * x * x) + (b * x) + c;   // standard form
            
            y = 240 - Util.map(y, minY, maxY, 0, 240);    // map to pixel
            
            points[i] = new Point(i, (int) y);
        }
        
        // Plot points
        // we could do this better but >:(
        for(int i = 1; i < points.length; i++) {
            Imgproc.line(source, points[i - 1], points[i], new Scalar(0, 0, 255));
        }
        
        // uuuuuuhhhh copy mat
        source.copyTo(dest);
    }

}
