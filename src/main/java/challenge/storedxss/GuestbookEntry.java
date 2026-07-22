package challenge.storedxss;

/**
 * Represents a single guestbook entry in the Stored XSS challenge.
 * <p>
 * Each entry consists of the author's name and the submitted message
 * retrieved from the {@code storedxss_guestbook} database table.
 * Instances of this record are used to transfer guestbook data between
 * the service layer and the HTTP handler.
 * </p>
 *
 * @param author  the name of the user who submitted the message
 * @param message the guestbook message submitted by the author
 */
public record GuestbookEntry(String author, String message) {
}
