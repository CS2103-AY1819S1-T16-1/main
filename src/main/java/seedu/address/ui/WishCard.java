package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.wish.Wish;


/**
 * An UI component that displays information of a {@code Wish}.
 */
public class WishCard extends UiPart<Region> {

    private static final String FXML = "WishCard.fxml";
    private static final String[] TAG_COLORS = { "red", "yel", "blue", "navy", "ora", "green", "pink", "hot", "pur" };

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on WishBook level 4</a>
     */

    public final Wish wish;

    @FXML
    private HBox cardPane;

    @FXML
    private Label name;

    @FXML
    private Label progress;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private FlowPane tags;

    private String id;

    public WishCard(Wish wish, int displayedIndex) {
        super(FXML);

        this.wish = wish;
        this.id = displayedIndex + ". ";

        name.setText(wish.getName().fullName);
        progress.setText(getProgressInString(wish));
        progressBar.setProgress(wish.getProgress());

        if (wish.isFulfilled()) {
            cardPane.setOpacity(0.5);
        }
        initTags(wish);
    }

    /**
     * Returns the color style for {@code tagName}'s label.
     */
    private String getTagColorStyleFor(String tagName) {
        return TAG_COLORS[Math.abs(tagName.hashCode()) % TAG_COLORS.length];
    }

    /**
     * Returns the progress String (in percentage) for {@code wish}.
     */
    private String getProgressInString(Wish wish) {
        Double progress = wish.getProgress() * 100;
        return String.format("%d", progress.intValue()) + "%";
    }

    /**
     * Creates the tag labels for {@code wish}.
     */
    private void initTags(Wish wish) {
        wish.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorStyleFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof WishCard)) {
            return false;
        }

        // state check
        WishCard card = (WishCard) other;
        return id.equals(card.id)
                && wish.equals(card.wish);
    }
}
