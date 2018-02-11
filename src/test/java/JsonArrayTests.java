import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Json array tests")
class JsonArrayTests {
  private JsonArray arr;
  private JsonObject nested;
  private List<JsonNode> arrayList;

  @BeforeEach
  @Test
  @DisplayName("Array creation")
  void initialize() {
    arrayList = new ArrayList<>();
    arrayList.add(new JsonPrimitive<>(true));
    arrayList.add(new JsonPrimitive<>("str"));
    arrayList.add(new JsonPrimitive<>(55));
    nested = new JsonObject();
    nested.add("a1", new JsonPrimitive<>("a1"));
    nested.add("a2", new JsonPrimitive<>("a2"));
    arrayList.add(nested);
    arr = new JsonArray(arrayList);
    JsonArray b = new JsonArray();
    JsonArray c = new JsonArray(arrayList);

    assertEquals(new JsonArray(), b);
    assertNotEquals(new JsonArray(), c);
    assertEquals(c.getChildren(), arrayList);
    try {
      JsonArray d = new JsonArray(c.toString(2));
      JsonArray e = new JsonArray(c.toString());
      assertEquals(d, e);
    } catch (MalformedJsonException e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("Array deep copy")
  void arrayDeepCopy() {
    JsonArray b = arr.deepCopy();
    assertEquals(arr, b);
    arr.remove(0);
    assertNotEquals(arr, b);
    b = arr.deepCopy();
    nested.remove("a2");
    assertNotEquals(arr, b);
  }

  @Test
  @DisplayName("Array modification")
  void arrayModification(){
    JsonNode first = arr.get(0);
    assertEquals(first, arr.remove(0));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> arr.set(5, new JsonPrimitive<>(true)));
    JsonArray arrCopy = arr.deepCopy();
    arrCopy.set(0, new JsonPrimitive<>("string"));
    assertEquals(arrCopy.get(0), new JsonPrimitive<>("string"));
    assertNotEquals(arr, arrCopy);
  }

  @Test
  @DisplayName("Array properties")
  void arrayProperties() {
    JsonNode node = arr;
    assertEquals(true, node.isJsonArray());
    assertEquals(arrayList, node.getAsJsonArray().getChildren());
    assertEquals(node, node.getAsJsonArray());
  }
}
