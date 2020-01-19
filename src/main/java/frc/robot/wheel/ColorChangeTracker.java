package frc.robot.wheel;

public class ColorChangeTracker extends Thread {
    private ColorWheel wheel;

    ColorChangeTracker(ColorWheel wheelIn) {
        wheel = wheelIn;
    }

    private final Object lock = new Object();

    private ColorEnum old = null;

    private int cnt = 0;

    private static final long DELAY = 15;

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long now = System.currentTimeMillis();
            ColorEnum new_c = wheel.getColor();
            //System.out.println(ColorWheelUtil.getIdName(new_id));
            double diff;
            synchronized (lock) {
                if (new_c != null) {
                    if (old != null) {
                        if (new_c != old) {
                            cnt += 1;
                            old = new_c;
                        }
                    }
                    old = new_c;
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