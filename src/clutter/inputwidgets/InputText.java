package clutter.inputwidgets;

import static clutter.core.Dimension.contains;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Function;

import clutter.abstractwidgets.StatefulWidget;
import clutter.abstractwidgets.Widget;
import clutter.core.Context;
import clutter.core.Debug;
import clutter.core.Dimension; // Update import statement
import clutter.decoratedwidgets.Clip;
import clutter.decoratedwidgets.DecoratedBox;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.SizedBox;
import clutter.layoutwidgets.enums.Alignment;
import clutter.widgetinterfaces.Interactable;
import clutter.widgetinterfaces.KeyEventHandler;

/**
 * An input text widget.
 */
public class InputText extends StatefulWidget<Context> implements Interactable, KeyEventHandler {
    String text;
    String originalText;
    boolean blinker = false;
    boolean editable = false;
    Timer timer = new java.util.Timer();
    Consumer<String> onTextChange;
    Color color;
    Function<String, Boolean> validationFunction;
    int minWidth = 0;

    /**
     * @param context the context
     * @param defaultText the default text
     * @param onTextChange the on text change action
     */
    public InputText(Context context, String defaultText, Consumer<String> onTextChange) {
        super(context);
        text = defaultText;
        originalText = defaultText;
        this.onTextChange = onTextChange;
    }

    /**
     * @param callback function to validate the text
     * @return the input text widget
     */
    public InputText setValidationFunction(Function<String, Boolean> f){
        this.validationFunction = f;
        return this;
    }

    /**
     * @return whether the text is valid
     */
    public boolean isValid(){
        return validationFunction == null || validationFunction.apply(text) || text == originalText;
    }

    /**
     * blink the cursor
     */
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

    /**
     * @return the input text widget
     */
    @Override
    public Widget build() {
        if (editable) {
            Color borderColor = isValid() ? null : Color.red;
            Debug.log(this, isValid());

            return new DecoratedBox(new Text(text + (blinker ? "|" : " ")).setColor(color)).setBorderColor(borderColor)
                    .setHorizontalAlignment(Alignment.STRETCH);
        } else {
            Debug.log(this, "Building clipped text");
            return new Clip(new Text(text).setColor(color)).setHorizontalAlignment(Alignment.STRETCH);
        }
    }

    /**
     * click event
     */
    @Override
    public void onClick() {
        System.out.println("editing text");
        setEditable(true, false);
    }

    /**
     * @color the color
     * @return the input text widget
     */
    public InputText setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * @param editable whether the text is editable
     * @param save whether to save the text
     */
    private void setEditable(boolean editable, boolean save) {
        if (this.editable == editable)
            return;
        this.editable = editable;
        if (editable) {
            context.getClickEventController().setClickHandler(this);
            context.getKeyEventController().setKeyHandler(this);
            blink();
        } else {
            blinker = false;
            context.getKeyEventController().removeKeyHandler(this);
            context.getClickEventController().removeClickHandler(this);
            if (save && isValid()) {
                onTextChange.accept(text);
                originalText = text;
            } else {
                text = originalText;
            }
        }
    }

    /**
     * @param id the id
     * @param hitPos the hit position
     * @param clickCount the click count
     * @return the interactable
     */
    @Override
    public Interactable hitTest(int id, Dimension hitPos, int clickCount) {
        if (id == MouseEvent.MOUSE_CLICKED)
            Debug.log(this, "Hit test: ", position, size, hitPos);
        if (!contains(position, size, hitPos)) {
            if (editable) {
                setState(() -> {
                    setEditable(false, true);
                });
            }
            return null;
        }
        if (id == MouseEvent.MOUSE_RELEASED && clickCount == 1 && editable == false)
            return this;
        if (clickCount > 1)
            setEditable(false, false);
        return null;
    }

    /**
     * @param id the id
     * @param keyCode the key code
     * @param keyChar the key character
     */
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
                        setEditable(false, false);
                    });
                    return;
                case KeyEvent.VK_ENTER:
                    setState(() -> {
                        setEditable(false, true);
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
