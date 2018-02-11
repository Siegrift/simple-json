import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Json formatter test")
class JsonFormatterTests {
  private JsonObject root = null;

  @BeforeEach
  void init() {
    root = new JsonObject();
    JsonArray arr = new JsonArray();
    JsonObject obj = new JsonObject();
    obj.add("bool", new JsonPrimitive<>(false));
    obj.add("str", new JsonPrimitive<>("str"));
    obj.add("str2", new JsonPrimitive<>("str2"));
    arr.add(obj);
    root.add("arr", arr);
    root.add("int", new JsonPrimitive<>(50));
    root.add("obj", obj.deepCopy());
  }

  @Test
  @DisplayName("Compact test")
  void compactTest() {
    assertEquals("X{\"arr\":[{\"str\":\"str\",\"bool\":false,\"str2\":\"str2\"}],\"obj\":{\"str\":\"str\",\"bool\":false,\"str2\":\"str2\"},\"int\":50}", root.toString());
    assertEquals(root.toString(),root.toString(4).replaceAll("\\s+", ""));
  }

  @Test
  @DisplayName("Manual diff creation")
  void manualDiffCreation() throws MalformedJsonException {
    JsonDiff diff = new JsonDiff();
    diff.addToAdded(new JsonObject("{\"a\":52}"));
    assertEquals(true, JsonDiff.isValidDiff(diff.getDiff()));
    assertEquals("{\"a\":52}", new JsonObject().applyDiff(diff).toString());
  }
}
