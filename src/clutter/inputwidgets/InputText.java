package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension; // Update import statement
import clutter.decoratedwidgets.Clip;
import clutter.decoratedwidgets.Text;
import clutter.widgetinterfaces.Interactable;
import clutter.widgetinterfaces.KeyEventHandler;

public class InputText extends StatefulWidget implements Interactable, KeyEventHandler {
    String text;
    String originalText;
    boolean blinker = false;
    boolean editable = false;
    Timer timer = new java.util.Timer();
    Consumer<String> onTextChange;
    Color color;

    public InputText(Context context, String defaultText, Consumer<String> onTextChange) {
        super(context);
        text = defaultText;
        originalText = defaultText;
        this.onTextChange = onTextChange;
    }

    protected void blink() {
        if (editable) {
            setState(() -> {
                blinker = !blinker;
            });
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    blink();
                }
            }, 500);
        }
    }

    @Override
    public Widget build() {
        if (editable) {
            return new Text(text + (blinker ? "|" : " ")).setColor(color);
        } else {
            return new Clip(new Text(text).setColor(color));
        }
    }

    @Override
    public void onClick() {
        if (editable)
            return;
        context.getKeyEventController().setKeyHandler(this);
        editable = true;
        blink();
    }

    public InputText setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        if (!contains(position, size, hitPos))
            return null;
        if (clickCount == 2 && editable == false) {
            return this;
        }
        return null;
    }

    @Override
    public void onKeyPress(int id, int keyCode, char keyChar) {
        if (id == KeyEvent.KEY_PRESSED) {
            switch (keyCode) {
                case KeyEvent.VK_BACK_SPACE:
                    setState(() -> {
                        if (!text.isEmpty()) {
                            text = text.substring(0, text.length() - 1);
                        }
                    });
                    return;
                case KeyEvent.VK_ESCAPE:
                    setState(() -> {
                        editable = false;
                        blinker = false;
                        context.getKeyEventController().removeKeyHandler(this);
                        text = originalText;
                    });
                    return;
                case KeyEvent.VK_ENTER:
                    setState(() -> {
                        editable = false;
                        blinker = false;
                        context.getKeyEventController().removeKeyHandler(this);
                        onTextChange.accept(text);
                        originalText = text;
                    });
                    return;
            }
        }
        if (id == KeyEvent.KEY_TYPED) {
            if (keyCode == KeyEvent.VK_UNDEFINED && Character.isDefined(keyChar)
                    && keyChar != KeyEvent.CHAR_UNDEFINED) {
                if (keyChar != KeyEvent.VK_ESCAPE && keyChar != KeyEvent.VK_BACK_SPACE) {
                    setState(() -> {
                        System.out.println("Changing text: " + keyChar);
                        text += keyChar;
                        System.out.println("New text: " + text);
                    });
                }
            } else {
                System.out.println("Unhandled key event");
            }
        }
    }

}
