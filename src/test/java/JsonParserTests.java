import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Json parser tests")
class JsonParserTests {
  private JsonObject root;

  @BeforeEach
  void init() {
    JsonObject aJson = new JsonObject();
    aJson.add("b", new JsonPrimitive<>("bString"));
    aJson.add("c", new JsonPrimitive<>("cSt\"ring"));
    JsonArray eJson = new JsonArray();
    eJson.add(new JsonPrimitive<>(true));
    eJson.add(new JsonPrimitive<>(12));
    eJson.add(new JsonPrimitive<>(56L));
    eJson.add(new JsonPrimitive<>(new BigInteger("1651656156186186186168168")));
    root = new JsonObject();
    root.add("a", aJson);
    root.add("d", new JsonPrimitive<>(52L));
    root.add("e", eJson);
  }

  @Test
  @DisplayName("Create from reader")
  void checkParsing() {
    try {
      File file = File.createTempFile("sampleJson", "in");
      BufferedWriter read = new BufferedWriter(new FileWriter(file));
      read.write(root.toString());
      read.close();
      JsonNode node = JsonParser.parse(new BufferedReader(new FileReader(file)));
      assertEquals(root, node);
    } catch (IOException | MalformedJsonException e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("Incorrect json")
  void incorrect() {
    assertThrows(MalformedJsonException.class, () -> new JsonObject(""));
    assertThrows(MalformedJsonException.class, () -> new JsonObject("{"));
    assertThrows(MalformedJsonException.class, () -> new JsonObject("{\"asd\"::52}"));
    assertThrows(MalformedJsonException.class, () -> new JsonObject("[\"asd\":52]"));
    assertThrows(MalformedJsonException.class, () -> new JsonObject(".\"asd\"::52}"));
    assertThrows(MalformedJsonException.class, () -> new JsonObject("{\"asd\":52."));
    assertThrows(MalformedJsonException.class, () -> new JsonArray("[true,false,\"]"));
    assertThrows(MalformedJsonException.class, () -> new JsonArray("[true,false,'']"));
    assertThrows(MalformedJsonException.class, () -> new JsonArray("[true,..25]"));
    assertThrows(MalformedJsonException.class, () -> new JsonArray("[true,.25.]"));
    assertThrows(MalformedJsonException.class, () -> new JsonArray("[true,\"asds\"\"]"));
    assertThrows(MalformedJsonException.class, () -> new JsonArray("[true,falsee]"));
  }
}
