package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.WishBookChangedEvent;
import seedu.address.commons.events.ui.WishDataUpdatedEvent;
import seedu.address.commons.events.ui.WishPanelSelectionChangedEvent;
import seedu.address.model.wish.Wish;

/**
 * Panel containing the detail of wish.
 */
public class WishDetailSavingAmount extends UiPart<Region> {

    private static final String FXML = "WishDetailSavingAmount.fxml";

    private final Logger logger = LogsCenter.getLogger(WishDetailSavingAmount.class);

    @FXML
    private Label price;

    @FXML
    private Label savedAmount;

    @FXML
    private Label progress;

    private String id;
    private Wish currentWish;

    public WishDetailSavingAmount() {
        super(FXML);

        loadDefaultDetails();
        registerAsAnEventHandler(this);
    }

    /**
     * Load the default page.
     */
    public void loadDefaultDetails() {
        price.setText("");
        savedAmount.setText("");
    }

    /**
     * Load the page that shows the detail of wish.
     */
    private void loadWishDetails(Wish wish) {
        savedAmount.setText("$" + wish.getSavedAmount().toString());
        price.setText("/ $" + wish.getPrice().toString());
        progress.setText(getProgressInString(wish));
        this.id = wish.getId().toString();
        this.currentWish = wish;
    }

    /**
     * Returns the progress String (in percentage) for {@code wish}.
     */
    private String getProgressInString(Wish wish) {
        Double progress = wish.getProgress() * 100;
        return String.format("%d", progress.intValue()) + "%";
    }

    @Subscribe
    private void handleWishBookChangedEvent(WishBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event,
                "handled by " + WishDetailSavingHistory.class.getSimpleName()));

        for (Wish wish: event.data.getWishList()) {
            if (this.id.equals(wish.getId().toString())) {
                loadWishDetails(wish);
            }
        }
    }

    @Subscribe
    private void handleWishPanelSelectionChangedEvent(WishPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadWishDetails(event.getNewSelection());
    }

    @Subscribe
    private void handleWishDataUpdatedEvent(WishDataUpdatedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (this.id.equals(event.getNewData().getId().toString())) {
            loadWishDetails(event.getNewData());
        }
    }
}
