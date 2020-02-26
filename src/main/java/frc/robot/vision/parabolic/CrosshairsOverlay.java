package frc.robot.vision.parabolic;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import frc.robot.vision.VisionProcessor;

/**
 * Calculates and draws the crosshairs for the shooter
 * 
 * @author Alex Pickering
 */
public class CrosshairsOverlay implements VisionProcessor {
    
    private double cameraX,         // X position of the camera where the shooter is x=0
                   cameraY,         // Y position of the camera where the ground is y=0
                   cameraAngle,     // Angle of the camera in radians
                   cameraFOV,       // Vertical field of view of the camera in radians
                   imageHeight,     // Height of the image in pixels
                   shooterY,        // Y position of the shooter where the ground is y=0
                   targetY,         // Y position of the target where the ground is y=0
                   gravity,         // Gravitational constant g in whatever units the rest use
                   shooterVelocity, // Velocity of the ball on launch in units/sec^2
                   shooterAngle,    // Angle of the shooter in radians
                   wheelDiameter;   // Diameter of the shooter wheels in meters
    
    private boolean hasLines = true;    // If the lines exist
    
    private Scalar colorA,  // Color of line A
                   colorB,  // Color of line B
                   colorC;  // Color of the center line
    
    private int lineAY, // Store the values so we don't recalculate it until we need to
                lineBY;
    
    private double quadA,   // Store the parabola values to give them out later
                   quadB,
                   quadC,
                   angleA,  // These too
                   angleB;
    
    /**
     * Creates the overlay, calculating line positions automatically
     * <p> Distance units don't matter as long as they're consistent
     * 
     * @param cameraX X position of the camera where the shooter is x=0
     * @param cameraY Y position of the camera where the ground is y=0
     * @param cameraAngle Angle of the camera in radians
     * @param cameraFOV Vertical field of view of the camera in radians
     * @param imageHeight Height of the image in pixels
     * @param shooterY Y position of the shooter where the ground is y=0
     * @param wheelDiameter Diameter of the shooter wheels in meters
     * @param targetY Y position of the target where the ground is y=0
     * @param gravity Acceleration due to gravity (g)
     * @param shooterVelocity The velocity of the ball as it exists the shooter
     * @param shooterAngle The angle of the shooter in radians
     * @param colorA The color of line A
     * @param colorB The color of line B
     * @param colorC The color of the center line
     */
    public CrosshairsOverlay(double cameraX, double cameraY, double cameraAngle, double cameraFOV, double imageHeight, double shooterY, double wheelDiameter, double targetY, double gravity, double shooterVelocity, double shooterAngle, Scalar colorA, Scalar colorB, Scalar colorC) {
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.cameraAngle = cameraAngle;
        this.cameraFOV = cameraFOV;
        this.imageHeight = imageHeight;
        this.shooterY = shooterY;
        this.wheelDiameter = wheelDiameter;
        this.targetY = targetY;
        this.gravity = gravity;
        this.shooterVelocity = shooterVelocity;
        this.shooterAngle = shooterAngle;
        this.colorA = colorA;
        this.colorB = colorB;
        this.colorC = colorC;
        
        // This will init the other stuff
        calculateLinePositions();
    }
    
    /**
     * Creates the overlay with only the 'constants', doesn't calculate the line positions initially
     * <p> Distance units don't matter as long as they're consistent
     * 
     * @param cameraX X position of the camera where the shooter is x=0
     * @param cameraY Y position of the camera where the ground is y=0
     * @param cameraAngle Angle of the camera in radians
     * @param cameraFOV Vertical field of view of the camera in radians
     * @param imageHeight Height of the image in pixels
     * @param shooterY Y position of the shooter where the ground is y=0
     * @param wheelDiameter Diameter of the shooter wheels in meters
     * @param targetY Y position of the target where the ground is y=0
     * @param gravity Acceleration due to gravity (g)
     * @param colorA
     * @param colorB
     * @param colorC
     */
    public CrosshairsOverlay(double cameraX, double cameraY, double cameraAngle, double cameraFOV, double imageHeight, double shooterY, double wheelDiameter, double targetY, double gravity, Scalar colorA, Scalar colorB, Scalar colorC) {
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.cameraAngle = cameraAngle;
        this.cameraFOV = cameraFOV;
        this.imageHeight = imageHeight;
        this.shooterY = shooterY;
        this.wheelDiameter = wheelDiameter;
        this.targetY = targetY;
        this.gravity = gravity;
        this.colorA = colorA;
        this.colorB = colorB;
        this.colorC = colorC;
        
        shooterVelocity = 0;
        shooterAngle = 0;
        lineAY = -1;
        lineBY = -1;
        quadA = 0;
        quadB = 0;
        quadC = 0;
        angleA = 0;
        angleB = 0;
    }
    
    /*
     * Getty bois
     */
    public double[] getParabola() { return new double[] {quadA, quadB, quadC}; }
    public double getQuadraticA() { return quadA; }
    public double getQuadraticB() { return quadB; }
    public double getQuadraticC() { return quadC; }
    public double getAngleA() { return angleA; }
    public double getAngleB() { return angleB; }
    public Scalar getColorA() { return colorA; }
    public Scalar getColorB() { return colorB; }
    
    
    /*
     * Setty bois
     */
    public void setVelocity(double v) { shooterVelocity = v; }
    public void setAngle(double a) { shooterAngle = a; }
    
    
    /*
     * Calculation setters/getters
     */
    
    /**
     * Sets the velocity based on rpm
     * 
     * @param rpm
     */
    public void setVelocityRPM(double rpm) {
        shooterVelocity = (wheelDiameter * Math.PI * rpm) / 60;
        shooterVelocity *= 0.95; // adjust it a bit
        //System.out.println("v: " + shooterVelocity);
    }
    
    /**
     * Sets the velocity based on distance to the target
     * 
     * @param dist
     */
    public void setVelocityDistance(double dist) {
        // v = (sqrt(gravity) * x * sqrt((tan(angle)^2) + 1)) / sqrt((2 * x * tan(angle)) - (2 * y))
        double tanTheta = Math.tan(shooterAngle),
               y = targetY - shooterY;
        
        shooterVelocity = (Math.sqrt(gravity) * dist * Math.sqrt((tanTheta * tanTheta) + 1)) / Math.sqrt((2 * dist * tanTheta) - (2 * y));
    }
    
    /**
     * Gets the RPM for the set velocity
     * 
     * @return Shooter RPM
     */
    public double getRPM() {
        return shooterVelocity / (wheelDiameter * Math.PI * 60);
    }
    
    
    /**
     * Calculates and sets the positions of the lines
     */
    public void calculateLinePositions() {
        // Start by calculating the quadratic trajectory
        double secTheta = 1.0d / Math.cos(shooterAngle);
        
        quadA = (gravity * secTheta * secTheta) / (2 * shooterVelocity * shooterVelocity);
        quadB = Math.tan(shooterAngle);
        quadC = shooterY - targetY;
        
        // Get roots
        double[] roots = getRoots(quadA, quadB, quadC);
        
        // Make sure it has roots
        if(roots[0] == -1) { // no roots
            hasLines = false;
            return;
        } else {
            hasLines = true;
        }
        
        // Move roots to be relative to the camera
        roots[0] -= cameraX;
        roots[1] -= cameraX;
        
        //System.out.println(String.format("%s, %s", roots[0], targetY - cameraY));
        
        // Find the angles via trig
        angleA = Math.atan2(targetY - cameraY, roots[0]);
        angleB = Math.atan2(targetY - cameraY, roots[1]);
        
        // Convert to y coordinate
        double anglePerPixel = cameraFOV / imageHeight,     // Angle per pixel of the image
               angleToTop = cameraAngle + (cameraFOV / 2),  // Angle from the x axis to the top of the image
               fromTopA = angleToTop - angleA,              // Angle from the top to the crosshair, different variable to preserve debug
               fromTopB = angleToTop - angleB;
        
        lineAY = (int) (fromTopA / anglePerPixel);    // Finally convert to y coord
        lineBY = (int) (fromTopB / anglePerPixel);
        
        //System.out.println(String.format("A:%s B:%s", lineAY, lineBY));
    }
    
    /**
     * Calculates the roots of a quadratic assuming they exist
     * 
     * @param a
     * @param b
     * @param c
     * @return The roots of the quadratic
     */
    private double[] getRoots(double a, double b, double c) {
        double disciminant = (b * b) - (4 * a * c);
        
        // No real roots
        if(disciminant < 0) {
            return new double[] {-1, -1};
        }
        
        // Has roots
        return new double[] {
                (-b + Math.sqrt(disciminant)) / (2 * a),
                (-b - Math.sqrt(disciminant)) / (2 * a)
        };
    }

    @Override
    public void process(Mat source, Mat dest, int width, int height) {
        // Speed too low
        if(!hasLines) {
            Imgproc.putText(source, "Velocity too low", new Point(10, 25), Core.FONT_HERSHEY_PLAIN, 1, new Scalar(0, 255, 0));
        } else {
            // Draw lines based off of lineAY and lineBY
            Imgproc.line(source, new Point(0, lineAY), new Point(width, lineAY), colorA);
            Imgproc.line(source, new Point(0, lineBY), new Point(width, lineBY), colorB);
            
            // Draw vertical crosshair
            Imgproc.line(source, new Point(width / 2, 0), new Point(width / 2, height), colorC);
        }
        
        source.copyTo(dest);
    }
}
