package seedu.address.storage;

import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * This Xml adapted class acts as a wrapper for a collection of {@code XmlAdaptedWish}es.
 */
public class XmlAdaptedWishWrapper {
    @XmlElementWrapper
    private LinkedList<XmlAdaptedWish> xmlAdaptedWishes;

    public XmlAdaptedWishWrapper() {
        this.xmlAdaptedWishes = new LinkedList<>();
    }

    public XmlAdaptedWishWrapper(LinkedList<XmlAdaptedWish> xmlAdaptedWishes) {
        this.xmlAdaptedWishes = xmlAdaptedWishes;
    }

    public LinkedList<XmlAdaptedWish> getXmlAdaptedWishes() {
        return xmlAdaptedWishes;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XmlAdaptedWishWrapper) || hasSameNumberOfWishes((XmlAdaptedWishWrapper) obj)) {
            return false;
        }
        Iterator<XmlAdaptedWish> otherIterator = ((XmlAdaptedWishWrapper) obj).getXmlAdaptedWishes().listIterator();
        Iterator<XmlAdaptedWish> thisIterator = xmlAdaptedWishes.listIterator();
        while (otherIterator.hasNext()) {
            if (!thisIterator.next().equals(otherIterator.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if this has the same number of {@code XmlAdaptedWish} objects as the other {@code XmlAdaptedWishWrapper}.
     */
    private boolean hasSameNumberOfWishes(XmlAdaptedWishWrapper other) {
        return xmlAdaptedWishes.size() == other.getXmlAdaptedWishes().size();
    }
}
