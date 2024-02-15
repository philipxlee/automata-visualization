# Cell Society Design Final

### Team Number: 3

### Names: Philip Lee (kl445), Saad Hakim (sh604), Abhishek Chataut (ac802)

## Team Roles and Responsibilities

* Team Member #1: Philip Lee
  * Primary: Model
  * Secondary: Config/Display

* Team Member #2: Saad Hakim
  * Primary: Config
  * Secondary: Model/Display

* Team Member #3: Abhishek Chataut
  * Primary: Display
  * Secondary: Model/Config

## Design goals

* The design of the program is to allow for easy addition of new simulations, the simulation's
corresponding cells, and edge policies. 
* The design uses an inheritance hierarchy to allow for easy addition of new simulations and cells.
* Code should be modular and easy to understand, with each class having a single responsibility.
* Each class should be heavily encapsulated, with minimal dependencies on other classes.
* Data structures are hidden from other classes, and only the necessary methods are exposed, with 
iterators for data structures written to be used by other classes.
* Repeated code is minimized, with public methods well documented and private methods and variables 
well named.

## High-level Design

#### Core Classes
* Display: Handles the display of the simulation
* Grid: Handles the grid and its behavior
* Config: Handles the configuration of the simulation

#### Model High Level Design
* The model is made up of a grid, which handles the initialization and updating of the grid.
* Each cell in the grid is made up of a specific type of cell, which is a subclass of the abstract 
class Cell. Each variation of cellular automata implements a simulation interface.
* The grid is encapsulated into a class instead of a data structure, with its public methods being 
able to be used by View to render the grid and subsequent generations of cellular automata.
* The different cells used by different variations have specific needs but some commonalities.
As such, an abstract class Cell defines basic features of what a common cell has, and additional 
features is extended by subclasses specific to a cell type, allowing ease of adding new cell types 
for new simulations.
* An interface is used for simulation variations, allowing for easy addition of new simulations.
Additional simulations will override the interface by adding specific features to that simulation.
* Another interface is used for the EdgePolicy, allowing for more edge policies to be added easily.
An abstract class for edge policy also holds basic functionality of different edge policies, 
so further edge policies can be added and customized via subclasses. Although the interface and 
abstract class is short, the design allows for future additions of edge policies.
* Generics is also employed so that the grid and simulations can be used for any type of cell. 
Instead of a grid for each individual cell type or simulation, generics allow the program to simply
pass in a type of cell and have the software run smoothly. The usage of generics from the Model is
extended and employed in other parts of the program outside Model.

#### Display High Level Design
* TODO

#### Config High Level Design
* TODO

## Assumptions that Affect the Design

#### Features Affected by Assumptions
* TODO

## Significant differences from Original Plan
* TODO

## New Features HowTo

#### Easy to Add Features
* Adding new simulations and cells unique to a simulation:
  * Create a new class that implements the Simulation interface in the variation directory
  * Create a new cell that implements the Cell abstract class in the cell directory, unique to this
  new simulation. If the cell shares basic features with other cells, the BasicCell class can be 
  used instead.
* Adding new edge policies:
  * Create a new class that implements the EdgePolicy interface in the edgepolicy directory
  * New edge policies can extend the abstract class AbstractEdgePolicy, which holds basic methods
  that all edge policies will have.
* Adding new representations of a state and its corresponding character used in the text file the 
XML points to:
  * Add a new state to the State enum in the grid package
  * Add a new character that matches the text file XML points to.
* Adding new XML files and configurations:
  * Simply add new XML files to the data folder with the corresponding XML formats.
  * Provide parameters and edge policies if needed. If they are not provided, errors will be thrown
  and appropriate default values will be used depending on the situation.
* Adding new features to the display (i.e. colors, tick speed, etc)
  * Simply add new features to the display class, and add new buttons to the control overlay.

#### Other Features not yet Done
* Although the changing of cell shapes is implemented, the logic of the interactions between the
  different neighbors created by the changed cell shape is not. For instance, although triggering
  the cell shapes to be `triangle`, water may not flow through the gaps between two triangles in
  `percolation`, even though there is empty space between it.
* XML Configuration Initialization, in `change` is not implemented.
* Extended Moore Neighbors are not implemented.

