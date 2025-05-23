package database;

import java.util.List;

public class ActionList extends Action {

    public ActionList(List<Action> actionList) {
        super(() -> {
            for (int i = actionList.size() - 1; i >= 0; i--) {
                Action action = actionList.get(i);
                action.undo();
            }
        }, () -> {
            for (int i = 0; i < actionList.size(); i++) {
                Action action = actionList.get(i);
                action.redo();
            }
        });
    }

}
