# Simple json

Why another json library? There are many JSON libraries out there, but none can create json diffs
or implements visitor pattern. These are the key features of this library.

## Simple json advantages
* Heavily using java generics
* Ability to create and apply json diffs
* Visitor pattern
* Simple implementation
* Low size
* No dependencies
* Deep copyable

## Simple json disadvantages
* Simple implementation - some features you would like to have may be missing.
* Does not support custom primitive types
* Does not check for circular dependency

## How does it work

#### JsonNode

Base class for any json object is `JsonNode`. This class has 3 subclasses:
1) JsonObject
2) JsonArray
3) JsonPrimitive

Any json node can be printed out as json, create deep copy, check what subclass it is...
For full reference see `JsonObject` implementation in sources.

#### JsonPrimitive

Allowing to create `JsonPrimitive` instances of any type. JSON format only supports numbers,
strings and booleans. These are supported JsonPrimitive types... You are able to create
primitives of other types, but many things **won't** work (for example printing out).

#### JsonFormatter

Used to override toString method of JsonNode classes (using visitor pattern). This class
provides a good insight how does visitor work and how to create one...

#### JsonDiff, JsonDiffApplier

Main reason for this library was the ability to create diffs. I tried JSON patch, but I
was disappointed by the results. Every `JsonNode` subclass has method 
`createDiff(JsonNode target)`, which will create `JsonDiff` object. There is also 
`applyDiff(JsonDiff diff)`, which returns new `JsonNode` by applying the diff on the
calling instance (this instance will stay untouched).

#### JsonParse

Class used to parse json from String or Reader. Supports json with/without trailing comma.

## Examples

Basic example on how to create json
```java
JsonObject json = new JsonObject();
json.add("a", new JsonPrimitive<>(false));
json.add("b", new JsonPrimitive<>(52));
json.add("c", new JsonPrimitive<>("str2"));
System.out.println(json); // "{"a":false,"b":52,"c":"str2"}"
```

For more information have a look at tests or source files.

## Suggestions, bugfixes

If anyone (probably by accident) found some bug or has an suggestion. Feel free to create 
and issue. If I have time, I will look at it :)


