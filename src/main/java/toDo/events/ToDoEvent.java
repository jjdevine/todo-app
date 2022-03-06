package toDo.events;

import toDo.data.ToDoItem;

public class ToDoEvent {

    public enum Type {CONFIGURE_AUTO_ESCALATION, CANCEL_AUTO_ESCALATION}

    private Object eventInfo;
    private Type type;
    private ToDoItem toDoItem;

    public ToDoEvent(Type type, Object eventInfo, ToDoItem toDoItem) {
        this.type = type;
        this.eventInfo = eventInfo;
        this.toDoItem = toDoItem;
    }

    public Type getType() {
        return type;
    }

    public Object getEventInfo() {
        return eventInfo;
    }

    public ToDoItem getToDoItem() {
        return toDoItem;
    }

    @Override
    public String toString() {
        return "ToDoEvent{" +
                "eventInfo=" + eventInfo +
                ", type=" + type +
                ", toDoItem=" + toDoItem +
                '}';
    }
}
