package frc.robot.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * An overlay that puts text on the image
 * 
 * @author Alex Pickering
 */
public class TextOverlay implements VisionProcessor {
    
    private Scalar color;
    
    private String text;
    private double scale;
    private int x, y;
    
    /**
     * Create a text overlay with some text and its scale
     * 
     * @param text
     * @param x
     * @param y
     * @param scale
     * @param color
     */
    public TextOverlay(String text, int x, int y, double scale, Scalar color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
    }
    
    /**
     * Create a text overlay with text and a scale
     * 
     * @param text
     * @param x
     * @param y
     * @param scale
     */
    public TextOverlay(String text, int x, int y, double scale) {
       this(text, x, y, scale, new Scalar(0, 0, 0)); 
    }
    
    /**
     * Create a text overlay with text and a color
     * 
     * @param text
     * @param x
     * @param y
     * @param color
     */
    public TextOverlay(String text, int x, int y, Scalar color) {
        this(text, x, y, 1, color);
    }
    
    /**
     * Create a text overlay with some text
     * 
     * @param text
     * @param x
     * @param y
     */
    public TextOverlay(String text, int x, int y) {
        this(text, x, y, 1, new Scalar(0, 0, 0));
    }
    
    public void setText(String text) { this.text = text; }
    public void setScale(double scale) { this.scale = scale; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setColor(Scalar color) { this.color = color; }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void process(Mat source, Mat dest) {
        Imgproc.putText(source, text, new Point(x, y), Core.FONT_HERSHEY_PLAIN, scale, color);
        source.copyTo(dest);
    }
}





