# Refactoring Discussion

#### Philip Lee, Abhishek Chataut, Saad Hakim

## Review Design Principles

### Summarize the Open/Closed Principle
The Open/Closed Principle deals with the program being extendable
but not modifiable. We should not be able to modify the code, disrupting
its consistency and reliability. However, it should be extendable, so we
can add to the functionality of the code.

### Summarize the Liskov Substituion Principle

"LSP states that in an object-oriented program, if we substitute
a superclass object reference with an object of any of its subclasses,
the program should not break." In other words, if we substitute an object
of the superclass with that of one its subclasses, the code should not 
demonstrate unexpected behavior. 

Some of the ways that LSP is violated are:
* Returning an object that’s incompatible with the object returned by the superclass method.
* Throwing a new exception that’s not thrown by the superclass method.
* Changing the semantics or introducing side effects that are not part of the superclass’s contract.


### Describe How 3 of Your Project's Abstractions Do or Do Not Obey Open/Closed and Liskov Substitution

#### Existing Abstraction 1:
Simulation Interface


#### Existing Abstraction 2:
CellStates Enum
Stores and abstracts the different states of a cell and cell types 

#### Existing Abstraction 3:
Abstract Class Cell
It follows Liskov Substitution Principle because it is a superclass and its subclasses 
can be substituted for it without breaking the code.


### Describe 3 New Abstractions Based on the Change Requirements

#### New Abstraction 1:


#### New Abstraction 2:


#### New Abstraction 3:


## Refactor from Design Principles

### Encapsulation Violations

eg. `Class.method` - public instance variable `foo`.


### Abstraction Violations


### MVC Violations


### Planned Refactorings from Design Principles
Make more variables static and fix pipelines issues.

