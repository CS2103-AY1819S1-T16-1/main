package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_WISH;
import static seedu.address.testutil.TypicalWishes.getTypicalWishes;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysWish;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import guitests.guihandles.WishCardHandle;
import guitests.guihandles.WishListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.wish.Wish;
import seedu.address.storage.XmlSerializableWishBook;

public class WishListPanelTest extends GuiUnitTest {
    private static final ObservableList<Wish> TYPICAL_WISHES =
            FXCollections.observableList(getTypicalWishes());

    private static final JumpToListRequestEvent JUMP_TO_SECOND_EVENT = new JumpToListRequestEvent(INDEX_SECOND_WISH);

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "sandbox");

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private WishListPanelHandle wishListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_WISHES);

        for (int i = 0; i < TYPICAL_WISHES.size(); i++) {
            wishListPanelHandle.navigateToCard(TYPICAL_WISHES.get(i));
            Wish expectedWish = TYPICAL_WISHES.get(i);
            WishCardHandle actualCard = wishListPanelHandle.getWishCardHandle(i);

            assertCardDisplaysWish(expectedWish, actualCard);
            //assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        initUi(TYPICAL_WISHES);
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        WishCardHandle expectedWish = wishListPanelHandle.getWishCardHandle(INDEX_SECOND_WISH.getZeroBased());
        WishCardHandle selectedWish = wishListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedWish, selectedWish);
    }

    /**
     * Verifies that creating and deleting large number of wishes in {@code WishListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() throws Exception {
        ObservableList<Wish> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of wish cards exceeded time limit");
    }

    /**
     * Returns a list of wishes containing {@code wishCount} wishes that is used to populate the
     * {@code WishListPanel}.
     */
    private ObservableList<Wish> createBackingList(int wishCount) throws Exception {
        Path xmlFile = createXmlFileWithWishes(wishCount);
        XmlSerializableWishBook xmlWishBook = XmlUtil.getDataFromFile(xmlFile, XmlSerializableWishBook.class);
        return FXCollections.observableArrayList(xmlWishBook.toModelType().getWishList());
    }

    /**
     * Returns a .xml file containing {@code wishCount} wishes. This file will be deleted when the JVM terminates.
     */
    private Path createXmlFileWithWishes(int wishCount) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        builder.append("<wishbook>\n");
        for (int i = 0; i < wishCount; i++) {
            builder.append("<wishes>\n");
            builder.append("<name>").append(i).append("a</name>\n");
            builder.append("<price>00.00</price>\n");
            builder.append("<savedAmount>00.00</savedAmount>\n");
            builder.append("<email>a@aa</email>\n");
            builder.append("<url>a</url>\n");
            builder.append("<remark>aa</remark>\n");
            builder.append("</wishes>\n");
        }
        builder.append("</wishbook>\n");

        Path manyWishesFile = Paths.get(TEST_DATA_FOLDER + "manyWishes.xml");
        FileUtil.createFile(manyWishesFile);
        FileUtil.writeToFile(manyWishesFile, builder.toString());
        manyWishesFile.toFile().deleteOnExit();
        return manyWishesFile;
    }

    /**
     * Initializes {@code wishListPanelHandle} with a {@code WishListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code WishListPanel}.
     */
    private void initUi(ObservableList<Wish> backingList) {
        WishListPanel wishListPanel = new WishListPanel(backingList);
        uiPartRule.setUiPart(wishListPanel);

        wishListPanelHandle = new WishListPanelHandle(getChildNode(wishListPanel.getRoot(),
                WishListPanelHandle.WISH_LIST_VIEW_ID));
    }
}
