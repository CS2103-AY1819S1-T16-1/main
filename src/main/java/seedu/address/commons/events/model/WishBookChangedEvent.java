package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyWishBook;

/** Indicates the WishBook in the model has changed*/
public class WishBookChangedEvent extends BaseEvent {

    public final ReadOnlyWishBook data;

    public WishBookChangedEvent(ReadOnlyWishBook data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of wishes " + data.getWishList().size();
    }
}
