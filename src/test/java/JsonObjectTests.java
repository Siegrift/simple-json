import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Json object tests")
class JsonObjectTests {
  private JsonObject jsonObject = null;

  @BeforeEach
  void initialize() {
    jsonObject = new JsonObject();
    jsonObject.add("bool", new JsonPrimitive<>(false));
    jsonObject.add("str", new JsonPrimitive<>("str"));
    jsonObject.add("str2", new JsonPrimitive<>("str2"));
    assertEquals("{\"str\":\"str\",\"bool\":false,\"str2\":\"str2\"}", jsonObject.toString());
  }

  @Test
  @DisplayName("Object creation")
  void objectCreation() {
    Map<String, JsonNode> children = new HashMap<>();
    children.put("a", new JsonPrimitive<>("aStr"));
    children.put("b", new JsonPrimitive<>(false));
    assertEquals("{\"a\":\"aStr\",\"b\":false}", new JsonObject(children).toString());
    try {
      JsonObject a = new JsonObject(jsonObject.toString());
      assertEquals(jsonObject, a);
      assertThrows(MalformedJsonException.class, () -> new JsonObject("{invalid}"));
      assertEquals("{}", new JsonObject("{}").toString());
    } catch (MalformedJsonException e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("Object deep copy")
  void arrayDeepCopy() {
    JsonObject b = jsonObject.deepCopy();
    Assertions.assertEquals(jsonObject, b);
    b.remove("str");
    assertNotEquals(jsonObject, b);
  }

  @Test
  @DisplayName("Object modification")
  void arrayModification(){
    JsonNode first = jsonObject.get("str");
    Assertions.assertEquals(first, jsonObject.remove("str"));
    assertEquals(null, jsonObject.remove("invalidKey"));
  }

  @Test
  @DisplayName("Object properties")
  void arrayProperties() {
    JsonNode node = jsonObject;
    Assertions.assertEquals(true, node.isJsonObject());
    Assertions.assertEquals(jsonObject.getChildren(), node.getAsJsonObject().getChildren());
  }
}
