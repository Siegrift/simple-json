import java.math.BigInteger;

/**
 * The type Json primitive.
 *
 * @param <T> the type parameter
 */
public class JsonPrimitive<T> extends JsonNode {
  private final T value;

  /**
   * Instantiates a new Json primitive with the value.
   *
   * @param value the value
   */
  public JsonPrimitive(T value) {
    this.value = value;
  }

  /**
   * @return true if the primitive is String
   */
  public boolean isString() {
    return value instanceof String;
  }

  /**
   * @return true if the primitive is Number
   */
  public boolean isNumber() {
    return value instanceof Number;
  }

  /**
   * @return true if the primitive is Boolean
   */
  public boolean isBoolean() {
    return value instanceof Boolean;
  }

  @Override
  public boolean isJsonPrimitive() {
    return true;
  }

  @Override
  public JsonPrimitive getAsJsonPrimitive() {
    return this;
  }

  @Override
  public JsonDiff createDiff(JsonNode target) {
    JsonDiff diff = new JsonDiff();
    // NOTE: we don't have to set primitives to removed
    if (!this.equals(target)) {
      diff.addToAdded(target);
    }
    return diff;
  }

  @Override
  public void accept(JsonNodeVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public JsonNode deepCopy() {
    return this;
  }

  /**
   * Gets the raw value of this primitive.
   *
   * @return the value
   */
  public T getValue() {
    return value;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof JsonPrimitive)) return false;
    JsonPrimitive objPrimitive = (JsonPrimitive) obj;
    if (isIntegral(this) && isIntegral(objPrimitive)) {
      return getValueAsNumber().longValue() == objPrimitive.getValueAsNumber().longValue();
    }
    if (value instanceof Number && objPrimitive.value instanceof Number) {
      double a = getValueAsNumber().doubleValue();
      // Java standard types objPrimitive than double return true for two NaN. So, need
      // special handling for double.
      double b = objPrimitive.getValueAsNumber().doubleValue();
      return a == b || (Double.isNaN(a) && Double.isNaN(b));
    }
    return value.equals(objPrimitive.getValue());
  }

  private boolean isIntegral(JsonPrimitive<T> primitive) {
    if (primitive.value instanceof Number) {
      Number number = (Number) primitive.value;
      return number instanceof BigInteger || number instanceof Long || number instanceof Integer
              || number instanceof Short || number instanceof Byte;
    }
    return false;
  }

  private Number getValueAsNumber() {
    return (Number) value;
  }
}
