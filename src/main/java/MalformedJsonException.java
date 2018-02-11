/**
 * Exception while parsing the json (either from string or reader).
 */
public class MalformedJsonException extends Exception {
  /**
   * Instantiates a new Malformed json exception.
   *
   * @param message the message
   */
  MalformedJsonException(String message) {
    super(message);
  }

  /**
   * Instantiates a new Malformed json exception.
   *
   * @param jsonString the json string
   * @param pos        the position where the parsing error occurred
   */
  MalformedJsonException(String jsonString, int pos) {
    super("Unexpected character in: " + jsonString + " at pos: " + pos);
  }
}
