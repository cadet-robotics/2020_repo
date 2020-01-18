package frc.robot.wheel;

public class ColorChangeTracker extends Thread {
    private ColorWheel wheel;

    ColorChangeTracker(ColorWheel wheelIn) {
        wheel = wheelIn;
    }

    private final Object lock = new Object();

    private double old_id = ;

    private int cnt = 0;

    private static final long DELAY = 25;

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long now = System.currentTimeMillis();
            double new_h = wheel.getHue();
            double diff;
            synchronized (lock) {
                diff = old_h - new_h;
                old_h = new_h;
                diff = Math.abs(diff);
                // False if old_h was NaN
                System.out.println("DELTA: " + diff);
                if (diff > MIN_HUE_CHANGE) {
                    cnt += 1;
                }
            }
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
            old_h = Double.NaN;
        }
    }

    public boolean hasMeasure() {
        synchronized (lock) {
            return Double.isNaN(old_h);
        }
    }

    public int getCount() {
        synchronized (lock) {
            return cnt;
        }
    }
}