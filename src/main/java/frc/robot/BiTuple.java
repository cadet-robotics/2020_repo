package frc.robot;

public class BiTuple<L, R> {
    private L left;
    private R right;

    public BiTuple(L leftIn, R rightIn) {
        left = leftIn;
        right = rightIn;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}