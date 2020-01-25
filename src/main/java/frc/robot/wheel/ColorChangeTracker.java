package frc.robot.wheel;

import java.util.function.Consumer;

/**
 * Keeps track of the number of color changes we've seen
 * Created by, and stores, an instance of ColorWheel
 */
public class ColorChangeTracker extends Thread {
    private ColorWheel wheel;

    public ColorChangeTracker(ColorWheel wheelIn) {
        wheel = wheelIn;
    }

    private final Object lock = new Object();

    // The last color we saw
    private ColorEnum old = null;

    // The number of color changes we've detected
    private int cnt = 0;

    // The expected time between sensor measurements (nanoseconds)
    private static final long DELAY = 25;

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                runForTime(() -> {
                    ColorEnum new_c = wheel.getColor();
                    if (new_c != null) {
                        System.out.println(new_c.name());
                        synchronized (lock) {
                            if (new_c != old) {
                                if (old != null) {
                                    cnt += 1;
                                }
                                old = new_c;
                            }
                        }
                    }
                }, (time) -> {}/*System.out.println("COLOR COUNTER OVERTIME: " + time)*/, DELAY);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Calls a method, attempting to take as close to a given time as possible
     *
     * If the method takes less time than expected, waits
     *
     * If the method takes longer than expected, calls overflowCallback and returns
     *
     * @param r The method to call
     * @param overflowCallback A callback that accepts the time overflowed by
     * @param delay The time in milliseconds that this method call should take
     * @throws InterruptedException
     */
    public void runForTime(Runnable r, Consumer<Long> overflowCallback, long delay) throws InterruptedException {
        long now = System.currentTimeMillis();
        r.run();
        // Waits so that this method takes ~delay time to execute
        long now2 = System.currentTimeMillis();
        long time = delay - (now2 - now);
        if (time <= 0) {
            overflowCallback.accept(-time);
        } else {
            Thread.sleep(time);
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