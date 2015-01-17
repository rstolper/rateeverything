package rateeverything.item;

/**
 * Created by roman on 1/3/15.
 */
public class ItemId {
    private int id;

    public ItemId(int id) {
        this.id = id;
    }

    public ItemId() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override public String toString() {
        return Integer.toString(id);
    }
}
