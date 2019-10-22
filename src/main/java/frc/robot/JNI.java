package frc.robot;

import edu.wpi.first.wpilibj.Filesystem;

public class JNI {
    static {
        System.loadLibrary(Filesystem.getDeployDirectory().toString() + "/rustpart.so");
    }

    public static native int test(int i);
}