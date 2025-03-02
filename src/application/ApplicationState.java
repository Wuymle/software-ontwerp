package application;

public class ApplicationState {
    private Runnable repaint;

    public ApplicationState(Runnable repaint) {
        this.repaint = repaint;
    }

    public void requestRepaint() {
        repaint.run();
    }
}
