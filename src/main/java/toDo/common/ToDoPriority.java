package toDo.common;

public enum ToDoPriority {

    URGENT(0),
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    private int index;

    private ToDoPriority(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
