package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_URL;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_WISHES;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;

import seedu.address.model.Model;
import seedu.address.model.tag.Tag;
import seedu.address.model.wish.Email;
import seedu.address.model.wish.Name;
import seedu.address.model.wish.Price;
import seedu.address.model.wish.Remark;
import seedu.address.model.wish.SavedAmount;
import seedu.address.model.wish.Url;
import seedu.address.model.wish.Wish;

/**
 * Edits the details of an existing wish in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String COMMAND_ALIAS = "e";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the wish identified "
            + "by the index number used in the displayed wish list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PRICE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_URL + "URL] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PRICE + "60.60 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_WISH_SUCCESS = "Edited Wish: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_WISH = "This wish already exists in the wish book.";

    private final Index index;
    private final EditWishDescriptor editWishDescriptor;

    /**
     * @param index of the wish in the filtered wish list to edit
     * @param editWishDescriptor details to edit the wish with
     */
    public EditCommand(Index index, EditWishDescriptor editWishDescriptor) {
        requireNonNull(index);
        requireNonNull(editWishDescriptor);

        this.index = index;
        this.editWishDescriptor = new EditWishDescriptor(editWishDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Wish> lastShownList = model.getFilteredWishList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_WISH_DISPLAYED_INDEX);
        }

        Wish wishToEdit = lastShownList.get(index.getZeroBased());
        Wish editedWish = createEditedWish(wishToEdit, editWishDescriptor);

        if (!wishToEdit.isSameWish(editedWish) && model.hasWish(editedWish)) {
            throw new CommandException(MESSAGE_DUPLICATE_WISH);
        }

        model.updateWish(wishToEdit, editedWish);
        model.updateFilteredWishList(PREDICATE_SHOW_ALL_WISHES);
        model.commitWishBook();
        return new CommandResult(String.format(MESSAGE_EDIT_WISH_SUCCESS, editedWish));
    }

    /**
     * Creates and returns a {@code Wish} with the details of {@code wishToEdit}
     * edited with {@code editWishDescriptor}.
     */
    private static Wish createEditedWish(Wish wishToEdit, EditWishDescriptor editWishDescriptor) {
        assert wishToEdit != null;

        Name updatedName = editWishDescriptor.getName().orElse(wishToEdit.getName());
        Price updatedPrice = editWishDescriptor.getPrice().orElse(wishToEdit.getPrice());
        Email updatedEmail = editWishDescriptor.getEmail().orElse(wishToEdit.getEmail());
        Url updatedUrl = editWishDescriptor.getUrl().orElse(wishToEdit.getUrl());
        SavedAmount savedAmount = wishToEdit.getSavedAmount(); // edit command does not allow editing remarks
        Remark remark = wishToEdit.getRemark(); // cannot modify remark with edit command
        Set<Tag> updatedTags = editWishDescriptor.getTags().orElse(wishToEdit.getTags());

        return new Wish(updatedName, updatedPrice, updatedEmail, updatedUrl, savedAmount, remark, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editWishDescriptor.equals(e.editWishDescriptor);
    }

    /**
     * Stores the details to edit the wish with. Each non-empty field value will replace the
     * corresponding field value of the wish.
     */
    public static class EditWishDescriptor {
        private Name name;
        private Price price;
        private Email email;
        private Url url;
        private Set<Tag> tags;

        public EditWishDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditWishDescriptor(EditWishDescriptor toCopy) {
            setName(toCopy.name);
            setPrice(toCopy.price);
            setEmail(toCopy.email);
            setUrl(toCopy.url);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, price, email, url, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        public Optional<Price> getPrice() {
            return Optional.ofNullable(price);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setUrl(Url url) {
            this.url = url;
        }

        public Optional<Url> getUrl() {
            return Optional.ofNullable(url);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditWishDescriptor)) {
                return false;
            }

            // state check
            EditWishDescriptor e = (EditWishDescriptor) other;

            return getName().equals(e.getName())
                    && getPrice().equals(e.getPrice())
                    && getEmail().equals(e.getEmail())
                    && getUrl().equals(e.getUrl())
                    && getTags().equals(e.getTags());
        }
    }
}
