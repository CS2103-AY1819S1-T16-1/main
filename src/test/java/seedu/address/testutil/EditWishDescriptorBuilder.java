package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditWishDescriptor;
import seedu.address.model.tag.Tag;
import seedu.address.model.wish.Email;
import seedu.address.model.wish.Name;
import seedu.address.model.wish.Price;
import seedu.address.model.wish.Url;
import seedu.address.model.wish.Wish;

/**
 * A utility class to help with building EditWishDescriptor objects.
 */
public class EditWishDescriptorBuilder {

    private EditWishDescriptor descriptor;

    public EditWishDescriptorBuilder() {
        descriptor = new EditWishDescriptor();
    }

    public EditWishDescriptorBuilder(EditWishDescriptor descriptor) {
        this.descriptor = new EditWishDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditWishDescriptor} with fields containing {@code wish}'s details
     */
    public EditWishDescriptorBuilder(Wish wish) {
        descriptor = new EditWishDescriptor();
        descriptor.setName(wish.getName());
        descriptor.setPrice(wish.getPrice());
        descriptor.setEmail(wish.getEmail());
        descriptor.setUrl(wish.getUrl());
        descriptor.setTags(wish.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditWishDescriptor} that we are building.
     */
    public EditWishDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Price} of the {@code EditWishDescriptor} that we are building.
     */
    public EditWishDescriptorBuilder withPrice(String price) {
        descriptor.setPrice(new Price(price));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditWishDescriptor} that we are building.
     */
    public EditWishDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Url} of the {@code EditWishDescriptor} that we are building.
     */
    public EditWishDescriptorBuilder withAddress(String url) {
        descriptor.setUrl(new Url(url));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditWishDescriptor}
     * that we are building.
     */
    public EditWishDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditWishDescriptor build() {
        return descriptor;
    }
}
