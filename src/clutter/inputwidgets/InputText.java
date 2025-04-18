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
import clutter.core.Decoration;
import clutter.core.Dimension;
import clutter.core.KeyEventController.KeyEventHandler;
import clutter.decoratedwidgets.Text;
import clutter.layoutwidgets.Box;
import clutter.layoutwidgets.enums.Alignment;

/**
 * An input text widget.
 */
public class InputText extends StatefulWidget<Context> implements KeyEventHandler {
    String text;
    String originalText;
    boolean blinker = false;
    boolean editable = false;
    Timer timer = new java.util.Timer();
    Consumer<String> onTextChange;
    Color fontColor;
    Color borderColor;
    Function<String, Boolean> validationFunction;
    int minWidth = 0;

    /**
     * constructor for the input text widget
     * 
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
     * set the validation function
     * 
     * @param callback function to validate the text
     * @return the input text widget
     */
    public InputText setValidationFunction(Function<String, Boolean> f) {
        this.validationFunction = f;
        return this;
    }

    /**
     * check whether the text is valid
     * 
     * @return whether the text is valid
     */
    private boolean isValid() {
        return validationFunction == null || validationFunction.apply(text) || text == originalText;
    }

    /**
     * blink the cursor
     */
    protected void blink() {
        if (!editable)
            return;
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

    /**
     * build the input text widget
     * 
     * @return the input text widget
     */
    @Override
    public Widget build() {
        return new Box(editable ? new Text(text + (blinker ? "|" : " ")).setFontColor(fontColor)
                : new Text((text != "") ? text : "    ").setFontColor(fontColor))
                        .setHorizontalAlignment(Alignment.STRETCH)
                        .setDecoration(new Decoration().setBorderColor(
                                (!editable || isValid()) ? borderColor : Color.red));
    }

    /**
     * set the color of the text
     * 
     * @color the color
     * @return the input text widget
     */
    public InputText setFontColor(Color color) {
        this.fontColor = color;
        return this;
    }

    public InputText setBorderColor(Color color) {
        this.borderColor = color;
        return this;
    }

    /**
     * set wheter the text is editable
     * 
     * @param editable whether the text is editable
     */
    private void setEditable(boolean editable) {
        if (this.editable == editable)
            return;
        this.editable = editable;
        if (editable) {
            context.getClickEventController().setClickHandler(this);
            context.getKeyEventController().setKeyHandler(this);
            blink();
        } else {
            context.getKeyEventController().removeKeyHandler(this);
            context.getClickEventController().removeClickHandler(this);
            setState(() -> {
                blinker = false;
            });
        }
    }

    private void save() {
        setState(() -> {
            onTextChange.accept(text);
            originalText = text;
        });
    }

    @Override
    public void setState(Runnable f) {
        super.setState(f);
        System.out.println("inputtext rebuild");
    }

    /**
     * hit test
     * 
     * @param id the id of the event
     * @param hitPos the hit position
     * @param clickCount the click count
     * @return the interactable
     */
    @Override
    public boolean hitTest(int id, Dimension hitPos, int clickCount) {
        if (!contains(position, size, hitPos)) {
            if (isValid()) {
                setEditable(false);
                save();
                return false;
            } else {
                return true;
            }
        }
        if (clickCount > 1)
            return false;
        switch (id) {
            case MouseEvent.MOUSE_CLICKED:
                setEditable(true);
                return true;
        }
        return false;
    }

    /**
     * key press event
     * 
     * @param id the id
     * @param keyCode the key code
     * @param keyChar the key character
     * @return whether the event was handled
     */
    @Override
    public boolean onKeyPress(int id, int keyCode, char keyChar) {
        if (!editable)
            throw new RuntimeException("InputText is not editable but got key");
        switch (id) {
            case KeyEvent.KEY_PRESSED:
                switch (keyCode) {
                    case KeyEvent.VK_BACK_SPACE:
                        setState(() -> {
                            if (!text.isEmpty()) {
                                text = text.substring(0, text.length() - 1);
                            }
                        });
                        return true;
                    case KeyEvent.VK_ESCAPE:
                        if (isValid())
                            setEditable(false);
                        return true;
                    case KeyEvent.VK_ENTER:
                        if (isValid()) {
                            setEditable(false);
                            save();
                        }
                        return true;
                }
                return false;
            case KeyEvent.KEY_TYPED:
                if (keyChar == KeyEvent.VK_ESCAPE || keyChar == KeyEvent.VK_BACK_SPACE
                        || keyChar == KeyEvent.VK_ENTER)
                    return !isValid();
                if (Character.isDefined(keyChar) && keyChar != KeyEvent.CHAR_UNDEFINED) {
                    setState(() -> {
                        text += keyChar;
                    });
                    return true;
                }
                return !isValid();
        }
        return !isValid();
    }

    @Override
    public void onKeyHandlerRemoved() {
        if (!editable)
            return;
        editable = false;
        context.getClickEventController().removeClickHandler(this);
        setState(() -> blinker = false);

    }

    @Override
    public void onClickHandlerRemoved() {
        if (!editable)
            return;
        editable = false;
        context.getKeyEventController().removeKeyHandler(this);
        setState(() -> blinker = false);
    }
}
