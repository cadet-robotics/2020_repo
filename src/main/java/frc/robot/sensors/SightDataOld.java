package frc.robot.sensors;

import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.UpdateLineManager;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Contains data from the vision systems
 * Javadoc comments lovingly provided by Alex Pickering 
 * 
 * @author Owen Avery
 */
public class SightDataOld {
    private double lastUpdate = Double.MIN_VALUE;
    private static final double timeout = 500;

    private double[] data = new double[4];
    private double r;
    private double xOff;
    private double yOff;

    private static final double X_WIDTH = 320;
    private static final double Y_WIDTH = 240;

    private boolean tieToFirst = true;

    private ReadWriteLock editLock = new ReentrantReadWriteLock();

    private NetworkTableInstance nt;
    private int lineListener;

    public SightDataOld(NetworkTableInstance ntIn) {
        nt = ntIn;
        //lineListener = UpdateLineManager.startListener(ntIn, this);
    }

    @Override
    protected void finalize() {
        nt.removeEntryListener(lineListener);
    }

    /**
     * Determines whether or not the read thing has timed out
     * 
     * @return Whether or not there's been a timeout
     */
    public boolean isTimeout() {
        editLock.readLock().lock();
        boolean v = (lastUpdate + timeout) < System.currentTimeMillis();
        editLock.readLock().unlock();
        return v;
    }
    
    /**
     * Sets vision target position for PID loops
     * dxxzz
     * @param p1x First point X
     * @param p1y First point Y
     * @param p2x Second point X
     * @param p2y Second point Y
     */
    public void setPoints(double p1x, double p1y, double p2x, double p2y) {
        editLock.writeLock().lock();
        lastUpdate = System.currentTimeMillis();
        int t1 = tieToFirst ? 0 : 2;
        int t2 = t1 + 1;
        
        tieToFirst ^= distSq(tieToFirst ? p1x : p2x, tieToFirst ? p1y : p2y, data[t1], data[t2]) > distSq(tieToFirst ? p2x : p1x, tieToFirst ? p2y : p1y, data[t1], data[t2]);
        
        data[0] = p1x;
        data[1] = p1y;
        data[2] = p2x;
        data[3] = p2y;
        
        double cx = (data[2] - data[0]) / 2;
        double cy = (data[3] - data[1]) / 2;
        double px = data[t1];
        double py = data[t2];
        
        r = Math.toDegrees(Math.atan2(py - cy, px - cx));
        xOff = (p2x - p1x - X_WIDTH) / 2;
        yOff = (p2y - p1y - Y_WIDTH) / 2;
        editLock.writeLock().unlock();
    }
    
    /**
     * Complements 'tieToFirst' variable
     */
    public void flip() {
        editLock.writeLock().lock();
        tieToFirst = !tieToFirst;
        editLock.writeLock().unlock();
    }
    
    /**
     * Gets the rotation offset
     * 
     * @return The rotation offset
     */
    public double getRotOffset() {
        editLock.readLock().lock();
        double v;
        
        if (isTimeout()) v = 0;
        else v = r;
        
        editLock.readLock().unlock();
        return v;
    }
    
    /**
     * Gets the X offset
     * 
     * @return The X offset
     */
    public double getXOffset() {
        editLock.readLock().lock();
        double v;
        
        if (isTimeout()) v = 0;
        else v = xOff;
        
        editLock.readLock().unlock();
        return v;
    }
    
    /**
     * Gets the Y offset
     * 
     * @return The Y offset
     */
    public double getYOffset() {
        editLock.readLock().lock();
        double v;
        
        if (isTimeout()) v = 0;
        else v = yOff;
        
        editLock.readLock().unlock();
        return v;
    }
    
    /**
     * Gets the length of the provided line squared
     * 
     * @param p1x The start point X
     * @param p1y The start point Y
     * @param p2x The end point X
     * @param p2y The end point Y
     * @return The length of the line squared
     */
    public static double distSq(double p1x, double p1y, double p2x, double p2y) {
        double x, y;
        
        x = p2x - p1x;
        y = p2y - p1y;
        
        return x * x + y * y;
    }

    /**
     * Calculates the error of a pair of lines
     *
     * @param l1 The first line
     * @param l2 The second line
     * @return The error between the two lines
     */
    public static double errorCalc(double[] l1, double[] l2) {
        double c1x = (l1[0] + l1[2]) / 2;
        double c1y = (l1[1] + l1[3]) / 2;
        double c2x = (l2[0] + l2[2]) / 2;
        double c2y = (l2[1] + l2[3]) / 2;
        
        double xd = c2x - c1x;
        double yd = c2y - c1y;
        
        double cerr = Math.sqrt(xd * xd / yd * yd);
        double serr = Math.atan2(l1[3] - l1[1], l1[2] - l1[0]) - Math.atan2(l2[3] - l2[1], l2[2] - l2[0]);
        
        return cerr + serr;
    }
}