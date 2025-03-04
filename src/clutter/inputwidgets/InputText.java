package clutter.inputwidgets;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.TimerTask;

import application.ApplicationState;
import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Dimension; // Update import statement
import clutter.core.KeyEventController;
import clutter.decoratedwidgets.Text;
import clutter.widgetinterfaces.Interactable;

public class InputText extends StatefulWidget implements Interactable, KeyEventHandler {
    String text = "";
    boolean blinker = true;
    boolean editable = false;
    java.util.Timer timer = new java.util.Timer();

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
    protected void init() {
        text = "defaulttext";
    }

    @Override
    public Widget build(Context context) {
        return new Text(text + (blinker ? "|" : " ")).setColor(Color.white);
    }

    @Override
    public void onClick() {
        context.getProvider(KeyEventController.class).setKeyHandler(this);
    }

    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        if (clickCount == 2) {
            editable = true;
            blinker = false;
            blink();
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
            }
        }
        if (id == KeyEvent.KEY_TYPED) {
            if (keyCode == KeyEvent.VK_UNDEFINED && Character.isDefined(keyChar)
                    && keyChar != KeyEvent.CHAR_UNDEFINED) {
                if (keyChar != KeyEvent.VK_ESCAPE && keyChar != KeyEvent.VK_BACK_SPACE) {
                    setState(() -> {
                        text += keyChar;
                    });
                }
            } else {
                System.out.println("Unhandled key event");
            }
        }
    }

}
