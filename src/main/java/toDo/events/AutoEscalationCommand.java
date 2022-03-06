package toDo.events;

import toDo.data.ToDoSchedule;

public class AutoEscalationCommand {

    private int dayFrequency;
    private ToDoSchedule.TargetPriority targetPriority;
    private boolean notify;

    public AutoEscalationCommand(ToDoSchedule.TargetPriority targetPriority, int dayFrequency, boolean notify) {
        this.dayFrequency = dayFrequency;
        this.targetPriority = targetPriority;
        this.notify = notify;
    }

    public int getDayFrequency() {
        return dayFrequency;
    }

    public void setDayFrequency(int dayFrequency) {
        this.dayFrequency = dayFrequency;
    }

    public ToDoSchedule.TargetPriority getTargetPriority() {
        return targetPriority;
    }

    public void setTargetPriority(ToDoSchedule.TargetPriority targetPriority) {
        this.targetPriority = targetPriority;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    @Override
    public String toString() {
        return "AutoEscalationCommand{" +
                "dayFrequency=" + dayFrequency +
                ", targetPriority=" + targetPriority +
                ", notify=" + notify +
                '}';
    }
}
