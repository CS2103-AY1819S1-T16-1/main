package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.WishPanelSelectionChangedEvent;
import seedu.address.model.wish.Wish;

/**
 * Panel containing the list of wishes.
 */
public class WishListPanel extends UiPart<Region> {
    private static final String FXML = "WishListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(WishListPanel.class);

    @FXML
    private ListView<Wish> wishListView;

    public WishListPanel(ObservableList<Wish> wishList) {
        super(FXML);
        setConnections(wishList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Wish> wishList) {
        wishListView.setItems(wishList);
        wishListView.setCellFactory(listView -> new WishListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        wishListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in wish list panel changed to : '" + newValue + "'");
                        raise(new WishPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code WishCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            wishListView.scrollTo(index);
            wishListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Wish} using a {@code WishCard}.
     */
    class WishListViewCell extends ListCell<Wish> {
        @Override
        protected void updateItem(Wish wish, boolean empty) {
            super.updateItem(wish, empty);

            if (empty || wish == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new WishCard(wish, getIndex() + 1).getRoot());
            }
        }
    }

}
