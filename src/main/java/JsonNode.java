/**
 * The base class for any json object.
 */
public abstract class JsonNode {
  /**
   * @return true if the instance is JsonObject, false otherwise
   * @see JsonObject
   */
  public boolean isJsonObject() {
    return false;
  }

  /**
   * @return true if the instance is JsonArray, false otherwise
   * @see JsonArray
   */
  public boolean isJsonArray() {
    return false;
  }

  /**
   * @return true if the instance is JsonPrimitive, false otherwise
   * @see JsonPrimitive
   */
  public boolean isJsonPrimitive() {
    return false;
  }

  /**
   * @return the instance as json object
   */
  public JsonObject getAsJsonObject() {
    return null;
  }

  /**
   * @return the instance as json array
   */
  public JsonArray getAsJsonArray() {
    return null;
  }

  /**
   * @return the instance as json primitive
   */
  public JsonPrimitive getAsJsonPrimitive() {
    return null;
  }

  /**
   * Create diff while comparing current instance against the target.
   *
   * @param target the target to compare
   * @return the json diff
   */
  protected abstract JsonDiff createDiff(JsonNode target);

  /**
   * Apply diff to current instance and return it. This operation is done
   * <b>immutably<b/> (current instance is not changed).
   *
   * @param diff the diff to apply
   * @return the json node with applied diff
   * @see JsonDiffApplier
   */
  public JsonNode applyDiff(JsonDiff diff) {
    JsonDiffApplier applier = new JsonDiffApplier();
    return applier.applyDiff(this, diff);
  }

  /**
   * @param visitor the visitor to accept.
   */
  public abstract void accept(JsonNodeVisitor visitor);

  /**
   * Performs a deep copy of current node.
   *
   * @return the deep copy.
   */
  protected abstract JsonNode deepCopy();

  /**
   * Get the current instance as string formatted with zero whitespace
   *
   * @return the string representation of current instance
   */
  @Override
  public String toString() {
    JsonFormatter formatter = new JsonFormatter();
    this.accept(formatter);
    return formatter.getFormattedJson();
  }

  /**
   * Get the current instance as string formatted using given number of spaces
   *
   * @param indent the number of spaces to indent
   * @return the string representation of current instance
   */
  public String toString(int indent) {
    JsonFormatter formatter = new JsonFormatter(indent);
    this.accept(formatter);
    return formatter.getFormattedJson();
  }
}
