package frc.robot.vision;

import org.opencv.core.Mat;

/**
 * An interface for working with the vision thread
 * 
 * @author Alex Pickering
 */
public interface VisionProcessor {
    
    /**
     * Processes a single frame
     * 
     * @param source The source matrix, has the original frame
     * @param dest The destination matrix, is sent to the output
     * @param width The width of the video
     * @param height The height of the video
     */
    public void process(Mat source, Mat dest, int width, int height);
}
