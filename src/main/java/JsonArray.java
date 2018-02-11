import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Json array.
 */
public class JsonArray extends JsonNode {
  private List<JsonNode> children;

  /**
   * Instantiates a new Json array.
   */
  public JsonArray() {
    children = new ArrayList<>();
  }

  /**
   * Instantiates a new Json array with values from the list.
   *
   * @param children the values
   */
  public JsonArray(List<JsonNode> children) {
    this.children = children;
  }

  /**
   * Instantiates a new Json array by parsing the argument.
   *
   * @param jsonArrayString the json array string
   * @throws MalformedJsonException the malformed json exception if the
   * string in not a valid JsonArray instance.
   */
  public JsonArray(String jsonArrayString) throws MalformedJsonException {
    JsonNode node = JsonParser.parse(jsonArrayString);
    if (!(node instanceof JsonArray)) throw new MalformedJsonException(jsonArrayString + " is not of type JsonArray");
    children = ((JsonArray) node).children;
  }

  /**
   * Add value to json array
   *
   * @param value the value to add
   */
  public void add(JsonNode value) {
    children.add(value);
  }

  /**
   * Set value to json array
   *
   * @param index the index, where to set the value
   * @param value the value
   */
  public void set(int index, JsonNode value) {
    children.set(index, value);
  }

  /**
   * Remove value from json array
   *
   * @param index the index of the node to remove
   * @return the removed value
   */
  public JsonNode remove(int index) {
    return children.remove(index);
  }

  /**
   * Get json value from json array
   *
   * @param index the index
   * @return the json value
   */
  public JsonNode get(int index) {
    return children.get(index);
  }

  @Override
  public boolean isJsonArray() {
    return true;
  }

  @Override
  public JsonArray getAsJsonArray() {
    return this;
  }

  @Override
  public JsonDiff createDiff(JsonNode target) {
    JsonDiff diff = new JsonDiff();
    if (!this.equals(target)) {
      diff.addToRemoved(new JsonPrimitive<>(true));
      diff.addToAdded(target);
    }
    return diff;
  }

  @Override
  public void accept(JsonNodeVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public JsonArray deepCopy() {
    JsonArray copy = new JsonArray();
    for (JsonNode value : children) {
      copy.add(value.deepCopy());
    }
    return copy;
  }

  /**
   * Gets the read-only copy of values in the json array.
   *
   * @return the read-only copy of values
   */
  public List<JsonNode> getChildren() {
    return Collections.unmodifiableList(children);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof JsonArray && ((this == obj) || children.equals(((JsonArray) obj).children));
  }
}