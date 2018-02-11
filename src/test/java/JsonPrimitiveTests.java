import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Json primitive tests")
class JsonPrimitiveTests {

  @Test
  @DisplayName("String primitive")
  void stringPrimitive() {
    JsonPrimitive<String> a = new JsonPrimitive<>("aStr");
    JsonPrimitive<String> b = new JsonPrimitive<>("bStr");
    JsonPrimitive aCopy = (JsonPrimitive) a.deepCopy();
    JsonPrimitive aCopyCopy = (JsonPrimitive) aCopy.deepCopy();

    assertEquals(true, a.isString());
    assertEquals(false, a.isBoolean());
    assertEquals(false, a.isNumber());
    assertEquals(true, a.isJsonPrimitive());

    assertNotEquals(a, b);
    assertEquals(a, aCopy);
    assertEquals(a, aCopyCopy);
    assertEquals("aStr", a.getValue());
    assertEquals("bStr", b.getValue());
  }

  @Test
  @DisplayName("Number primitive")
  void numberPrimitive() {
    JsonPrimitive<Number> intNum = new JsonPrimitive<>(123);
    JsonPrimitive<Long> longNum = new JsonPrimitive<>(123L);
    JsonPrimitive<BigInteger> bigIntNum = new JsonPrimitive<>(new BigInteger("254"));
    JsonPrimitive<Double> doubleNum = new JsonPrimitive<>(254.51);
    JsonPrimitive<Float> floatNum = new JsonPrimitive<>((float) 254.51);
    JsonPrimitive<Double> doubleNumIntegral = new JsonPrimitive<>((double) 123);
    JsonPrimitive<BigDecimal> bigDecNum = new JsonPrimitive<>(new BigDecimal("254.51"));

    assertEquals(true, intNum.isNumber());
    assertEquals(true, longNum.isNumber());
    assertEquals(true, bigIntNum.isNumber());
    assertEquals(true, doubleNum.isNumber());
    assertEquals(true, floatNum.isNumber());
    assertEquals(true, bigDecNum.isNumber());

    assertEquals(intNum, longNum);
    assertEquals(floatNum.getValue(), doubleNum.getValue(), 0.0001);
    assertEquals(bigDecNum, bigDecNum);
    assertNotEquals(doubleNumIntegral, doubleNum);
    assertNotEquals(intNum, bigDecNum);
    assertEquals(doubleNumIntegral, intNum);
  }

  @Test
  @DisplayName("Boolean primitive")
  void booleanPrimitive() {
    JsonPrimitive<Boolean> a = new JsonPrimitive<>(true);
    JsonPrimitive<Boolean> b = new JsonPrimitive<>(true);
    JsonPrimitive<Boolean> c = new JsonPrimitive<>(false);

    assertEquals(a, b);
    assertNotEquals(c, b);
  }

  @Test
  @DisplayName("Different type")
  void differentType() {
    JsonPrimitive<String> a = new JsonPrimitive<>("5");
    JsonPrimitive<Integer> b = new JsonPrimitive<>(5);
    JsonPrimitive<Boolean> c = new JsonPrimitive<>(true);
    JsonPrimitive<Integer> d = new JsonPrimitive<>(1);

    assertNotEquals(a, b);
    assertNotEquals(c, d);
    assertNotEquals(b, d);
  }

  @Test
  @DisplayName("Primitive properties")
  void primitiveProperties() {
    JsonNode node = new JsonPrimitive<>("str");
    assertEquals(true, node.isJsonPrimitive());
    assertEquals("str", node.getAsJsonPrimitive().getValue());
    assertNull(node.getAsJsonArray());
  }

}
