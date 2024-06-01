# Project Tallini Dervishi

## Introduction
Project Tallini Dervishi is a library designed for creating and manipulating XML structures in Kotlin. It provides a set of tools to build, modify, and traverse XML documents using a fluent and intuitive API.

## Features
- Create XML documents with nested elements.
- Add, remove, and modify attributes of XML elements.
- Traverse and query XML structures.
- Use visitors to apply custom transformations and processing.

## Usage
### Creating XML Documents
```kotlin
val catalog = xml("catalog") {
    element("region") {
        attr("name", "lazio")
        element("vineyard") {
            attr("name", "frascati")
            element("wine") {
                attr("name", "gotto d'oro")
            }
        }
    }
    element("region") {
        attr("name", "veneto")
        element("vineyard") {
            attr("name", "valpolicella")
            element("wine") {
                attr("name", "ripasso")
            }
            element("wine") {
                attr("name", "amarone")
            }
        }
    }
}

### Modifying Entities
// Adding an attribute
catalog["lazio"]?.setAttribute("climate", "warm")

// Removing an element
val region = catalog["lazio"]
region?.removeChild(region["frascati"])

### Visitor Usage
val printer = PrettyPrintVisitor()
catalog.accept(printer)
println(printer.getPrettyPrint())

It is also included a section of Testing

