/**
 * The interface Json node visitor.
 */
interface JsonNodeVisitor {
  /**
   * @param jsonArray the json array to visit
   */
  void visit(JsonArray jsonArray);

  /**
   * Visit.
   *
   * @param jsonObject the json object to visit
   */
  void visit(JsonObject jsonObject);

  /**
   * Visit.
   *
   * @param jsonPrimitive the json primitive to visit
   */
  void visit(JsonPrimitive jsonPrimitive);
}
