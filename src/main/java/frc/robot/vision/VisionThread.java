package frc.robot.vision;

import java.util.ArrayList;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * Manages the vision thread and such
 * oops! its already a thing in wpilib. but i found that later and this is a very different implementation so im keepin it
 * 
 * @author Alex Pickering
 */
public class VisionThread implements Runnable {

    private ArrayList<VisionProcessor> processors;
    
    private String name;
    
    private int width, height;
    
    /**
     * Sets up the thread with a set of interfaces to be applied sequentially
     * 
     * @param processors The VisionProcessors to use
     * @param name The name of the output video
     * @param width The width of the output video
     * @param height The height of the output video
     */
    public VisionThread(ArrayList<VisionProcessor> processors, String name, int width, int height) {
        this.processors = processors;
        this.name = name;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Creates a VisionThread without any processors
     * 
     * @param name The name of the output video
     * @param width The width of the output video
     * @param height The height of the output video
     */
    public VisionThread(String name, int width, int height) {
        this(new ArrayList<VisionProcessor>(), name, width, height);
    }
    
    /**
     * Sets up the thread with its interface to the robot
     * <p> remember to call startAutomaticCapture before running this
     * 
     * @param processor The interfacing VisionProcessor
     * @param name The name of the output video
     * @param width The width of the output video
     * @param height The height of the output video
     */
    public VisionThread(VisionProcessor processor, String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        
        processors = new ArrayList<>();
        processors.add(processor);
    }
    
    /**
     * Appends a processor to the pipeline
     * 
     * @param p The VisionProcessor
     */
    public void addProcessor(VisionProcessor p) {
        processors.add(p);
    }
    
    @Override
    public void run() {
        // Setup OpenCV stuff
        CvSink sink = CameraServer.getInstance().getVideo();
        CvSource out = CameraServer.getInstance().putVideo(name, width, height);
        
        // Source and destination matricies
        Mat source = new Mat(),
            dest = new Mat();
        
        // Run the processing
        while(!Thread.interrupted()) {
            // Don't run stuff until we have a frame
            if(sink.grabFrame(source) == 0) continue;
            
            // Run the VisionProcessor, giving the output of the last as the input of the next
            for(int i = 0; i < processors.size(); i++) {
                if(i != 0) dest.copyTo(source);
                
                processors.get(i).process(source, dest, width, height);
            }
            
            // Output the frame
            out.putFrame(dest);
        }
    }

}




