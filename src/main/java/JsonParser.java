import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The type Json parser.
 */
public class JsonParser {
  private static final char DECIMAL_POINT = '.';
  private static final String TRUE_STR = "true";
  private static final String FALSE_STR = "false";

  private enum INTEGRAL_TYPES {
    /**
     * Byte integral types.
     */
    BYTE, /**
     * Short integral types.
     */
    SHORT, /**
     * Int integral types.
     */
    INT, /**
     * Long integral types.
     */
    LONG, /**
     * Big int integral types.
     */
    BIG_INT
  }

  /**
   * Parse json node from the string.
   *
   * @param jsonString the json string to parse from
   * @return the parsed json node
   * @throws MalformedJsonException the malformed json exception it the string could not be parsed
   */
  public static JsonNode parse(String jsonString) throws MalformedJsonException {
    jsonString = jsonString.replaceAll("\\s+", "");
    Pair<JsonNode, Integer> res = internalParse(jsonString, 0);
    if (res.second != jsonString.length()) throw new MalformedJsonException(jsonString, res.second);
    // must not be primitive
    if (res.first instanceof JsonPrimitive) throw new MalformedJsonException(jsonString, 0);
    return res.first;
  }

  private static Pair<JsonNode, Integer> internalParse(String jsonString, int start) throws MalformedJsonException {
    char startChar = charAt(jsonString, start);
    int pos = start + 1;
    switch (startChar) {
      case '{':
        JsonObject jsonObject = new JsonObject();
        while (charAt(jsonString, pos) != '}') {
          Pair<String, Integer> parsedKey = parseString(jsonString, pos);
          if (charAt(jsonString, parsedKey.second) != ':') {
            throw new MalformedJsonException(jsonString, parsedKey.second);
          }
          Pair<JsonNode, Integer> parsedValue = internalParse(jsonString, parsedKey.second + 1);
          jsonObject.add(parsedKey.first, parsedValue.first);
          pos = parsedValue.second;
          if (charAt(jsonString, pos) != ',') {
            if (charAt(jsonString, pos) == '}') break;
            else throw new MalformedJsonException(jsonString, pos);
          }
          pos++;
        }
        return new Pair<>(jsonObject, pos + 1);
      case '[':
        JsonArray jsonArray = new JsonArray();
        while (charAt(jsonString, pos) != ']') {
          Pair<JsonNode, Integer> parsedValue = internalParse(jsonString, pos);
          jsonArray.add(parsedValue.first);
          pos = parsedValue.second;
          if (charAt(jsonString, pos) != ',') {
            if (charAt(jsonString, pos) == ']') break;
            else throw new MalformedJsonException(jsonString, pos);
          }
          pos++;
        }
        return new Pair<>(jsonArray, pos + 1);
      default:
        if (charAt(jsonString, start) == '\"') {
          Pair<String, Integer> parsedString = parseString(jsonString, start);
          return new Pair<>(new JsonPrimitive<>(parsedString.first), parsedString.second);
        } else if (Character.isDigit(charAt(jsonString, start)) || charAt(jsonString, start) == DECIMAL_POINT) {
          Pair<Number, Integer> parsedNumber = parseNumber(jsonString, start);
          return new Pair<>(new JsonPrimitive<>(parsedNumber.first), parsedNumber.second);
        } else if (substring(jsonString, start, start + TRUE_STR.length()).equals(TRUE_STR)) {
          return new Pair<>(new JsonPrimitive<>(true), start + TRUE_STR.length());
        } else if (substring(jsonString, start, start + FALSE_STR.length()).equals(FALSE_STR)) {
          return new Pair<>(new JsonPrimitive<>(false), start + FALSE_STR.length());
        } else {
          throw new MalformedJsonException(jsonString, start);
        }
    }
  }

  private static String substring(String jsonString, int start, int end) throws MalformedJsonException {
    try {
      return jsonString.substring(start, end);
    } catch (Exception e) {
      throw new MalformedJsonException(jsonString, start);
    }
  }

  private static char charAt(String jsonString, int pos) throws MalformedJsonException {
    if (pos < jsonString.length()) return jsonString.charAt(pos);
    throw new MalformedJsonException(jsonString, pos);
  }

  private static Pair<Number, Integer> parseNumber(String jsonString, int pos) throws MalformedJsonException {
    int endPos = pos;
    boolean decimalPointFound = false;
    while (endPos < jsonString.length() && (Character.isDigit(charAt(jsonString, endPos))) || charAt(jsonString, endPos) == DECIMAL_POINT) {
      if (charAt(jsonString, endPos) == DECIMAL_POINT) {
        if (decimalPointFound) throw new MalformedJsonException(jsonString, endPos);
        decimalPointFound = true;
      }
      endPos++;
    }
    if (endPos - pos <= 1) throw new MalformedJsonException(jsonString, pos);
    if (decimalPointFound) {
      BigDecimal decimal = new BigDecimal(jsonString.substring(pos, endPos));
      // NOTE: we convert to double (don't care about lost precision)
      return new Pair<>(decimal.doubleValue(), endPos);
    } else {
      BigInteger integer = new BigInteger(jsonString.substring(pos, endPos));
      return new Pair<>(getCorrectNumberType(integer), endPos);
    }
  }

  private static Number getCorrectNumberType(BigInteger integer) {
    Number number = integer;
    for (INTEGRAL_TYPES type : INTEGRAL_TYPES.values()) {
      try {
        switch (type) {
          case BYTE:
            number = integer.byteValueExact();
          case INT:
            number = integer.intValueExact();
          case LONG:
            number = integer.longValueExact();
          case SHORT:
            number = integer.shortValueExact();
          case BIG_INT:
            number = integer;
        }
      } catch (Exception ignored) {
      }
    }
    return number;
  }

  private static Pair<String, Integer> parseString(String jsonString, int pos) throws MalformedJsonException {
    StringBuilder keyBuilder = new StringBuilder();
    if (charAt(jsonString, pos++) != '"') throw new MalformedJsonException(jsonString, pos);
    while (pos < jsonString.length() && charAt(jsonString, pos) != '"') {
      if (charAt(jsonString, pos) == '\\') pos++;
      if (pos == jsonString.length()) throw new MalformedJsonException(jsonString, pos);
      keyBuilder.append(charAt(jsonString, pos++));
    }
    if (charAt(jsonString, pos++) != '"') throw new MalformedJsonException(jsonString, pos);
    return new Pair<>(keyBuilder.toString(), pos);
  }

  /**
   * Reads all the input from reader and then uses string parsing to parse the json.
   *
   * @param reader the reader from which to read
   * @return the parsed json node
   * @throws MalformedJsonException the malformed json exception it the string could not be parsed
   * @see JsonParser#parse(String)
   */
  public static JsonNode parse(BufferedReader reader) throws MalformedJsonException {
    StringBuilder builder = new StringBuilder();
    reader.lines().forEach(builder::append);
    return parse(builder.toString());
  }
}
