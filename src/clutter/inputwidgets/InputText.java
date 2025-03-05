package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension; // Update import statement
import clutter.core.KeyEventController;
import clutter.decoratedwidgets.Text;
import clutter.widgetinterfaces.Interactable;

public class InputText extends StatefulWidget implements Interactable, KeyEventHandler {
    String text;
    boolean blinker = false;
    boolean editable = false;
    Timer timer = new java.util.Timer();

    public InputText(Context context, String defaultText) {
        super(context);
        text = defaultText;
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
    public Widget build(Context context) {
        return new Text(text + (blinker ? "|" : " ")).setColor(Color.white);
    }

    @Override
    public void onClick() {
        if (editable)
            return;
        context.getProvider(KeyEventController.class).setKeyHandler(this);
        editable = true;
        blink();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
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
                        context.getProvider(KeyEventController.class).removeKeyHandler(this);
                    });
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
