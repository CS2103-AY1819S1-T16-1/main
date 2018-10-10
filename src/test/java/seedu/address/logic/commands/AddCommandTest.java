package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyWishBook;
import seedu.address.model.WishBook;
import seedu.address.model.WishTransaction;
import seedu.address.model.wish.Wish;
import seedu.address.testutil.WishBuilder;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullWish_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_wishAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingWishAdded modelStub = new ModelStubAcceptingWishAdded();
        Wish validWish = new WishBuilder().build();

        CommandResult commandResult = new AddCommand(validWish).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validWish), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validWish), modelStub.wishesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateWish_throwsCommandException() throws Exception {
        Wish validWish = new WishBuilder().build();
        AddCommand addCommand = new AddCommand(validWish);
        ModelStub modelStub = new ModelStubWithWish(validWish);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_WISH);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Wish alice = new WishBuilder().withName("Alice").build();
        Wish bob = new WishBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different wish -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addWish(Wish wish) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public WishTransaction getWishTransaction() {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public void resetData(ReadOnlyWishBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyWishBook getWishBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasWish(Wish wish) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteWish(Wish target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateWish(Wish target, Wish editedWish) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Wish> getFilteredWishList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredWishList(Predicate<Wish> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoWishBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoWishBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoWishBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoWishBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitWishBook() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single wish.
     */
    private class ModelStubWithWish extends ModelStub {
        private final Wish wish;

        ModelStubWithWish(Wish wish) {
            requireNonNull(wish);
            this.wish = wish;
        }

        @Override
        public boolean hasWish(Wish wish) {
            requireNonNull(wish);
            return this.wish.isSameWish(wish);
        }
    }

    /**
     * A Model stub that always accept the wish being added.
     */
    private class ModelStubAcceptingWishAdded extends ModelStub {
        final ArrayList<Wish> wishesAdded = new ArrayList<>();

        @Override
        public boolean hasWish(Wish wish) {
            requireNonNull(wish);
            return wishesAdded.stream().anyMatch(wish::isSameWish);
        }

        @Override
        public void addWish(Wish wish) {
            requireNonNull(wish);
            wishesAdded.add(wish);
        }

        @Override
        public void commitWishBook() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyWishBook getWishBook() {
            return new WishBook();
        }
    }

}
