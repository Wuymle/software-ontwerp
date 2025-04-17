package clutter.core;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class AnimationController {
    private double animationProgress = 0.0;
    private Timer animationTimer = new Timer();

    public void animate(Consumer<Double> f, double beginValue, double endValue, double duration) {
        animationProgress = 0.0;
        animateProgress(f, beginValue, endValue, duration);
    }

    private void animateProgress(Consumer<Double> f, double beginValue, double endValue,
            double duration) {
        if (animationProgress > 1.0)
            return;
        int mspf = 5;
        double easeingValue = 1 - Math.pow(1 - animationProgress, 4);
        f.accept(((endValue - beginValue) * easeingValue) + beginValue);
        animationProgress += mspf / (1000 * duration);
        animationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                animateProgress(f, beginValue, endValue, duration);
            }
        }, mspf);
    }
}
