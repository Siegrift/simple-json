/**
 * The type Json diff. Tracks what has been removed and what has been
 * added in json object. For example:<br>
 * jsonA = "{a:52,b:52}"<br>
 * jsonB = "{a:52,c:false}"<br>
 * The diff produced by comparing jsonA to jsonB would be:
 * {"add":{"c":false},"remove":{"b":true}}
 * All json primitives in remove object will be true as we care only about the keys.
 */
public class JsonDiff {
  private static final String ADD_PATH = "add";
  private static final String REMOVE_PATH = "remove";
  private final JsonObject diff;

  /**
   * Instantiates a new Json diff. You should not create the diff directly.
   * Instead get the diff by calling createDiff method on JsonNode instance.
   *
   * @see JsonNode#createDiff(JsonNode)
   */
  public JsonDiff() {
    diff = new JsonObject();
  }

  /**
   * Track that node has been added.
   *
   * @param node the node that has been added
   */
  public void addToAdded(JsonNode node) {
    diff.add(ADD_PATH, node);
  }

  /**
   * Track that node has been removed.
   *
   * @param node the node that has been removed
   */
  public void addToRemoved(JsonNode node) {
    diff.add(REMOVE_PATH, node);
  }

  /**
   * @return true if something was added, false otherwise
   */
  public boolean isSomethingAdded() {
    return diff.getChildren().containsKey(ADD_PATH);
  }

  /**
   * @return true if something was removed, false otherwise
   */
  public boolean isSomethingRemoved() {
    return diff.getChildren().containsKey(REMOVE_PATH);
  }

  /**
   * Gets json node representing additions.
   *
   * @return the additions
   */
  public JsonNode getAdded() {
    return diff.getChildren().get(ADD_PATH);
  }

  /**
   * Gets json node representing removals.
   *
   * @return the removals
   */
  public JsonNode getRemoved() {
    return diff.getChildren().get(REMOVE_PATH);
  }

  /**
   * Gets the raw diff object.
   *
   * @return the diff object
   */
  public JsonObject getDiff() {
    return diff;
  }

  @Override
  public String toString() {
    return diff.toString();
  }

  /**
   * Checks whether the jsonObject is a valid diff object (has only ADD_PATH or REMOVE_PATH).
   *
   * @param jsonObject the json object to check
   * @return true it the object is valid diff
   */
  public static boolean isValidDiff(JsonObject jsonObject) {
    for (String key : jsonObject.getKeys()) {
      if (!key.equals(ADD_PATH) && key.equals(REMOVE_PATH)) return false;
    }
    return true;
  }
}
