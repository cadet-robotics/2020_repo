package frc.robot.wheel;

public class ColorChangeTracker extends Thread {
    private ColorWheel wheel;

    ColorChangeTracker(ColorWheel wheelIn) {
        wheel = wheelIn;
    }

    private final Object lock = new Object();

    // The last color we saw
    private ColorEnum old = null;

    // The number of color changes we've detected
    private int cnt = 0;

    // The expected time between sensor measurements (nanoseconds)
    private static final long DELAY = 15;

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long now = System.currentTimeMillis();
            ColorEnum new_c = wheel.getColor();
            //System.out.println(ColorWheelUtil.getIdName(new_id));
            double diff;
            // Detects any change
            synchronized (lock) {
                if (new_c != null) {
                    if (new_c != old) {
                        if (old != null) {
                            cnt += 1;
                        }
                        old = new_c;
                    }
                }
            }
            // Waits so that each loop takes ~DELAY amount of nanoseconds to execute
            long now2 = System.currentTimeMillis();
            long time = DELAY - (now2 - now);
            if (time <= 0) {
                System.out.println("COLOR COUNTER OVERTIME: " + time);
            } else {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public void reset() {
        synchronized (lock) {
            cnt = 0;
            old = null;
        }
    }

    public boolean hasMeasure() {
        synchronized (lock) {
            return old != null;
        }
    }

    public int getCount() {
        synchronized (lock) {
            return cnt;
        }
    }
}