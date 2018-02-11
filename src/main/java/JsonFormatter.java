import java.util.Collections;

/**
 * The type Json formatter.
 */
public class JsonFormatter implements JsonNodeVisitor {
  private final StringBuilder builder;
  private final int indent;
  private int offset = 0;

  /**
   * Instantiates a new Json formatter with 0 indentation.
   */
  public JsonFormatter() {
    this(0);
  }

  /**
   * Instantiates a new Json formatter.
   *
   * @param indent the number of spaces used when formatting the json
   */
  public JsonFormatter(int indent) {
    this(indent, new StringBuilder());
  }

  private JsonFormatter(int indent, StringBuilder builder) {
    this.builder = builder;
    this.indent = indent;
    this.offset = 0;
  }

  private String createSpace(int count) { return String.join("", Collections.nCopies(count, " "));
  }

  private boolean shouldIndent() {
    return indent != 0;
  }

  @Override
  public void visit(JsonArray jsonArray) {
    builder.append("[");
    if (shouldIndent()) builder.append('\n');
    offset += indent;
    int nextIndex = 1;
    for (JsonNode node : jsonArray.getChildren()) {
      builder.append(createSpace(offset));
      node.accept(this);
      if (nextIndex != jsonArray.getChildren().size()) builder.append(',');
      nextIndex++;
      if (shouldIndent()) builder.append('\n');
    }
    offset -= indent;
    builder.append(createSpace(offset)).append("]");
  }

  @Override
  public void visit(JsonObject jsonObject) {
    builder.append('{');
    if (shouldIndent()) builder.append('\n');
    offset += indent;
    int nextIndex = 1;
    for (String key : jsonObject.getKeys()) {
      builder.append(createSpace(offset))
              .append('"')
              .append(quote(key))
              .append('"')
              .append(shouldIndent() ? ": " : ":");
      jsonObject.get(key).accept(this);
      if (nextIndex != jsonObject.getChildren().size()) builder.append(',');
      nextIndex++;
      if (shouldIndent()) builder.append('\n');
    }
    offset -= indent;
    builder.append(createSpace(offset)).append('}');
  }

  @Override
  public void visit(JsonPrimitive jsonPrimitive) {
    if (jsonPrimitive.isString()) {
      builder.append('"').append(quote((String) jsonPrimitive.getValue())).append('"');
    } else {
      builder.append(jsonPrimitive.getValue());
    }
  }

  private String quote(String value) {
    return value.replace("\"", "\\\"");
  }

  /**
   * Gets formatted json.
   *
   * @return the formatted json
   */
  public String getFormattedJson() {
    return builder.toString();
  }
}
