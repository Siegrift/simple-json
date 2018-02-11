import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Json object.
 */
public class JsonObject extends JsonNode {
  private Map<String, JsonNode> children;

  /**
   * Instantiates a new Json object.
   */
  public JsonObject() {
    children = new HashMap<>();
  }

  /**
   * Instantiates a new Json object from from the map.
   *
   * @param values the values
   */
  public JsonObject(Map<String, JsonNode> values) {
    this.children = values;
  }

  /**
   * Instantiates a new Json object by parsing the argument.
   *
   * @param jsonObjectString the json object string
   * @throws MalformedJsonException the malformed json exception if the
   * string in not a valid JsonObject instance.
   */
  public JsonObject(String jsonObjectString) throws MalformedJsonException {
    JsonNode node = JsonParser.parse(jsonObjectString);
    if (!(node instanceof JsonObject)) throw new MalformedJsonException(jsonObjectString + " is not of type JsonObject");
    children = ((JsonObject) node).children;
  }

  /**
   * Add value to json object at specified key.
   *
   * @param key   the key
   * @param value the value
   */
  public void add(String key, JsonNode value) {
    children.put(key, value);
  }

  /**
   * Remove value from json object.
   *
   * @param key the key
   * @return the removed json node
   */
  public JsonNode remove(String key) {
    return children.remove(key);
  }

  @Override
  public boolean isJsonObject() {
    return true;
  }

  @Override
  public JsonObject getAsJsonObject() {
    return this;
  }

  @Override
  public JsonDiff createDiff(JsonNode target) {
    JsonDiff diff = new JsonDiff();
    if (!(target instanceof JsonObject)) {
      diff.addToRemoved(new JsonPrimitive<>(true));
      diff.addToAdded(target);
    } else {
      JsonObject add = new JsonObject(), rem = new JsonObject();
      JsonObject targetJsonObject = (JsonObject) target;
      // remove keys
      for (String key : getKeys()) {
        if (!targetJsonObject.has(key)) rem.add(key, new JsonPrimitive<>(true));
      }
      // add keys
      for (String key : targetJsonObject.getKeys()) {
        if (!has(key)) {
          add.add(key, targetJsonObject.get(key));
        } else if (!targetJsonObject.get(key).toString().equals(this.get(key).toString())) {
          JsonDiff childDiff = this.get(key).createDiff(targetJsonObject.get(key));
          if (childDiff.isSomethingAdded()) add.add(key, childDiff.getAdded());
          if (childDiff.isSomethingRemoved()) rem.add(key, childDiff.getRemoved());
        }
      }
      if (!add.getChildren().isEmpty()) diff.addToAdded(add);
      if (!rem.getChildren().isEmpty()) diff.addToRemoved(rem);
    }
    return diff;
  }

  /**
   * Get value from json object.
   *
   * @param key the key
   * @return the json node
   */
  public JsonNode get(String key) {
    return children.get(key);
  }

  /**
   * Get keys from json object.
   *
   * @return the keys of the json object
   */
  public Iterable<String> getKeys() {
    return children.keySet();
  }

  /**
   * Checks whether the key is in the json object or not.
   *
   * @param key the key
   * @return true if the key is located in the object, false otherwise
   */
  public boolean has(String key) {
    return children.containsKey(key);
  }

  @Override
  public void accept(JsonNodeVisitor visitor) {
    visitor.visit(this);
  }

  /**
   * Gets the read-only copy of values in the json object.
   *
   * @return the read-only copy of values
   */
  public Map<String, JsonNode> getChildren() {
    return Collections.unmodifiableMap(children);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof JsonObject && ((this == obj) || children.equals(((JsonObject) obj).children));
  }

  @Override
  public JsonObject deepCopy() {
    JsonObject copy = new JsonObject();
    for (String key : getKeys()) {
      copy.add(key, get(key).deepCopy());
    }
    return copy;
  }


}
