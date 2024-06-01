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

// Iterate over elements
val region = catalog["lazio"]
region elements {
    println("Element: ${it.name}")
}

### Visitor Creation
class CustomVisitor : XMLEntityVisitor {
    override fun visit(entity: XMLEntity): Boolean {
        println("Visiting: ${entity.name}")
        return true
    }

    override fun endVisit(entity: XMLEntity) {
        println("Ending visit: ${entity.name}")
    }
}

val customVisitor = CustomVisitor()
catalog.accept(customVisitor)


### Visitor Usage
val printer = PrettyPrintVisitor()
catalog.accept(printer)
println(printer.getPrettyPrint())

It is also included a section of Testing


### docs/api.md

```markdown
# API Documentation for Project Tallini Dervishi

## Overview
This document provides a detailed API reference for the Project Tallini Dervishi library.

### XMLEntity
Represents an XML Entity (tag) with nested entities, attributes, and text.

#### Properties
- `name: String`
- `textContent: String?`
- `parent: XMLEntity?`
- `depth: Int`

#### Methods
- `setAttribute(name: String, value: String)`
- `getAttributes(): MutableMap<String, String>`
- `removeAttribute(name: String)`
- `addChild(child: XMLEntity)`
- `removeChild(child: XMLEntity)`
- `gtChildren(): MutableList<XMLEntity>`
- `gtParent(): XMLEntity?`
- `accept(visitor: XMLEntityVisitor)`
- `query(path: String): List<XMLEntity>`

### Visitors
Implement the `XMLEntityVisitor` interface to create custom visitors for processing XML entities.

#### XMLEntityVisitor
Defines the visitor interface.

- `visit(entity: XMLEntity): Boolean`
- `endVisit(entity: XMLEntity)`

### Built-in Visitors
- `PrettyPrintVisitor`
- `RenameEntityVisitor`
- `RemoveEntityVisitor`
- `RenameAttributeVisitor`
- `RemoveAttributeVisitor`
- `GlobalAttributeAdder`

## Example Usage
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
}


