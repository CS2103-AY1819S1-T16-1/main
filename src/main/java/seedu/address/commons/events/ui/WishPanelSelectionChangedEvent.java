package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.wish.Wish;

/**
 * Represents a selection change in the Wish List Panel
 */
public class WishPanelSelectionChangedEvent extends BaseEvent {


    private final Wish newSelection;

    public WishPanelSelectionChangedEvent(Wish newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public Wish getNewSelection() {
        return newSelection;
    }
}
