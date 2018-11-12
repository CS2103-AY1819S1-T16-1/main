package seedu.address.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.WishBookChangedEvent;
import seedu.address.commons.events.ui.WishDataUpdatedEvent;
import seedu.address.commons.events.ui.WishPanelSelectionChangedEvent;
import seedu.address.model.WishTransaction;
import seedu.address.model.wish.Wish;

/**
 * Panel containing the detail of wish.
 */
public class WishDetailSavingHistory extends UiPart<Region> {
    private static final String FXML = "WishDetailSavingHistory.fxml";
    private final Logger logger = LogsCenter.getLogger(getClass());

    private WishTransaction wishTransaction;
    private Wish currentWish;

    private ArrayList savingHistoryList = new ArrayList();
    private String id;

    @FXML
    private ListView<String> savingHistoryListView;

    public WishDetailSavingHistory(WishTransaction wishTransaction) {
        super(FXML);

        this.wishTransaction = wishTransaction;

        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<String> savingHistoryList) {
        savingHistoryListView.setItems(savingHistoryList);
        savingHistoryListView.setCellFactory(listView -> new WishDetailSavingHistory.SavingHistoryListViewCell());
    }

    /**
     * Load the page that shows the detail of wish.
     */
    private void loadWishDetails(Wish wish) {
        this.savingHistoryList.clear();
        this.id = wish.getId().toString();

        ListIterator<Wish> entry = wishTransaction.getWishMap().get(wish.getId()).listIterator(1);

        while (entry.hasNext()) {
            Wish prevWish = wishTransaction.getWishMap().get(wish.getId()).get(entry.previousIndex());
            double prevAmount = prevWish.getSavedAmount().value;
            double nextAmount = entry.next().getSavedAmount().value;
            double diff = nextAmount - prevAmount;

            if (diff > 0) {
                savingHistoryList.add("Saved $" + String.format("%.2f", diff));
            } else if (diff < 0) {
                savingHistoryList.add("Deducted $" + String.format("%.2f", Math.abs(diff)));
            }
        }

        Collections.reverse(savingHistoryList);

        ObservableList<String> oSavingHistoryList = FXCollections.observableArrayList(savingHistoryList);
        setConnections(oSavingHistoryList);
    }

    @Subscribe
    private void handleWishPanelSelectionChangedEvent(WishPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.currentWish = event.getNewSelection();
        loadWishDetails(event.getNewSelection());
    }

    @Subscribe
    private void handleWishDataUpdatedEvent(WishDataUpdatedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event,
                "wish data updated by " + WishDetailSavingHistory.class.getSimpleName()));
        if (this.id.equals(event.getNewData().getId().toString())) {
            this.currentWish = event.getNewData();
            loadWishDetails(event.getNewData());
        }
    }

    @Subscribe
    private void handleWishBookChangedEvent(WishBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event,
                "handled by " + WishDetailSavingHistory.class.getSimpleName()));
        this.wishTransaction = event.wishTransaction;
        loadWishDetails(this.currentWish);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code String} using a {@code WishSavingHistoryCell}.
     */
    class SavingHistoryListViewCell extends ListCell<String> {
        @Override
        protected void updateItem(String savingHistory, boolean empty) {
            super.updateItem(savingHistory, empty);

            if (empty || savingHistory == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new SavingHistoryCell(savingHistory).getRoot());
            }
        }
    }
}
