import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("Json diffing tests")
class JsonDiffingTests {
  @Test
  @DisplayName("Primitive test")
  void primitiveTest() {
    JsonPrimitive<String> a = new JsonPrimitive<>("aStr");
    JsonPrimitive<Boolean> b = new JsonPrimitive<>(true);
    JsonPrimitive<Integer> c = new JsonPrimitive<>(52);
    JsonDiff diff = a.createDiff(b);
    assertEquals(a.applyDiff(diff), b);
    diff = b.createDiff(a);
    assertEquals(b.applyDiff(diff), a);
    diff = a.createDiff(c);
    assertEquals(a.applyDiff(diff), c);
  }

  @Test
  @DisplayName("Array test")
  void arrayTest() {
    ArrayList<JsonNode> aList = new ArrayList<>();
    aList.add(new JsonPrimitive<>("aStr"));
    aList.add(new JsonPrimitive<>(false));
    aList.add(new JsonPrimitive<>(55));
    ArrayList<JsonNode> bList = new ArrayList<>(aList);
    bList.remove(0);
    bList.add(new JsonPrimitive<>(250));
    JsonArray a = new JsonArray(aList);
    JsonArray b = new JsonArray(bList);
    JsonDiff diff = a.createDiff(b);
    assertNotEquals(a.applyDiff(diff), a);
    assertEquals(a.applyDiff(diff), b);
  }

  @Test
  @DisplayName("Object test")
  void objectTest() {
    JsonObject a = new JsonObject();
    a.add("b", new JsonPrimitive<>(false));
    a.add("c", new JsonPrimitive<>(55));
    JsonObject b = new JsonObject();
    b.add("b", new JsonPrimitive<>(false));
    b.add("c", new JsonPrimitive<>(17));
    JsonDiff diff = a.createDiff(b);
    assertEquals(a.applyDiff(diff), b);
  }

  @Test
  @DisplayName("Nested test")
  void complicatedTest() {
    JsonObject root = new JsonObject();
    JsonArray arr = new JsonArray();
    JsonObject obj = new JsonObject();
    obj.add("bool", new JsonPrimitive<>(false));
    obj.add("str", new JsonPrimitive<>("str"));
    obj.add("str2", new JsonPrimitive<>("str2"));
    arr.add(obj);
    root.add("arr", arr);
    root.add("int", new JsonPrimitive<>(50));
    root.add("obj", obj.deepCopy());
    JsonObject rootCopy = root.deepCopy();
    obj.remove("str");
    root.get("obj").getAsJsonObject().remove("str");
    JsonDiff diff = root.createDiff(rootCopy);
    assertEquals(root.applyDiff(diff), rootCopy);
  }
}
