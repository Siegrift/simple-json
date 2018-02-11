/**
 * The type Json diff applier.
 */
class JsonDiffApplier {
  /**
   * Applies diff on json node. The diff is applied immutably (source is
   * not modified). The function returns the node with applied diff.
   *
   * @param source the json node on which to apply the diff
   * @param jsonDiff the json diff to apply
   * @return the json node got by applying the diff on the source
   */
  public JsonNode applyDiff(JsonNode source, JsonDiff jsonDiff) {
    JsonNode removed = recursiveRemove(source, jsonDiff.getRemoved());
    return recursiveAdd(removed, jsonDiff.getAdded());
  }

  /**
   * Recursively traverse the source and return new node with applied additions.
   * @param source the object to recurse on
   * @param add the additions
   * @return json object with applied additions
   */
  private static JsonNode recursiveAdd(JsonNode source, JsonNode add) {
    if (!(add instanceof JsonObject)) return add;
    JsonObject addJson = (JsonObject) add;
    JsonObject ret = (JsonObject) source;
    for (String key : addJson.getKeys()) {
      if (!ret.has(key)) ret.add(key, addJson.get(key));
      else ret.add(key, recursiveAdd(ret.get(key), addJson.get(key)));
    }
    return ret;
  }

  /**
   * Recursively traverse the source and return new node with applied removals.
   * @param source the object to recurse on
   * @param remove the removals
   * @return json object with applied removals
   */
  private static JsonNode recursiveRemove(JsonNode source, JsonNode remove) {
    if (remove == null) return source;
    if (remove.isJsonPrimitive()) return new JsonObject();
    JsonObject ret = (JsonObject) source;
    JsonObject jsonRemove = (JsonObject) remove;
    for (String key : jsonRemove.getKeys()) {
      ret.add(key, recursiveRemove(ret.get(key), jsonRemove.get(key)));
      if (ret.get(key).getAsJsonObject().getChildren().size() == 0) ret.remove(key);
    }
    return ret;
  }
}
