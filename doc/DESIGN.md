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
* The Display class handles the rendering of the grid and the control overlay.
* It takes in as a constructor parameter the grid and config instances for the specific simulation,
this enables the Display to communicate with the grid and config classes to get and send the necessary data
* The rendering is done by partitioning the scene into first two parts, the grid on the left and the UI on the right.
* The grid is rendered by using the Grid class's iterator and drawing them appropriately based on what the cell shape is
as well as the state of the cell. The color is derived from what Config passed to the Display class as the default or XML configured color.
* The UI is a VBox which the top half is the simulation info and the bottom half is the control overlay with buttons and sliders. 
Both of those are also VBox.
* The creating of these VBox and everything is within their respective methods like a createGrid or a createControl.
* Create control makes multiple buttons and toggles and so on that are all tied to event handlers.
* There is a Grapher class that makes a line graph and stores the data of the graph in a list of data points and has
methods to show or add data points to the graph.
* To update the grid and run the simulation, all you need to do is tell the Grid instance to computeNextGeneration and then
call the createGrid method again to update the visuals. This is encapsulated in a method called nextTick. It also adds the 
new tick's data to the graph.
* By seperating the parts of the display into their own methods, it makes it easier to add new features where you want them
and also make it a bit more efficient because the UI only needs to be rendered once and is only created once at the start.


#### Config High Level Design
* Class Config loads an xml file and reads each node into its appropriate
instance variable, which it can give to other classes using the getter
methods.
  * The grid to be implemented is saved in a txt file, whose name is given
  in the xml file. So, class Config follows the path of the txt file and 
  reads the grid, storing it in a queue, which pops off the values of the cells
  in the grid upon request using a getter method.
* Class Control handles setting up the simulation by working as a liaison between
different classes in config, view, and model. 
  * It calls the objects of the relevant classes necessary for
  setting the simulation and passes the information between
  them.
  * It also checks the type of simulation that it should call
  from model.
* Class Saving handles saving the state of the simulation into an
xml file and the grid state into a txt file with its path given
in the xml file.
* Class ConfigurationException handles the exceptions caused by
bad xml, data, or txt files. It calls the Display static method 
showMessage to display the message thrown by the exception and additionally
can output the call stack leading to the exception depending upon the way
ConfigurationException is thrown.

## Assumptions that Affect the Design

#### Features Affected by Assumptions
* Given that it is assumed in Config that the grid state is stored into
a txt file with the path given in the relevant node of the loaded
xml file, when creating a simulation test file, one has to create
both an xml file and its pair txt file.
* Given the implementation of saveXmlFile method in Saving class,
all saved files are stored in 'SavedFile' directory under 'data' directory.
* Given the implementation of config, whenever one wants to add a new kind of
simulation, they must add the new simulation to Control class's returnSimulation
method as one of the cases.
* TODO

## Significant differences from Original Plan
* In the original plan, the cell states were to be stored in the
loaded xml file. However, now the cell states are stored in a txt
file with its path given in the loaded xml file.
* In the original plan, Config is an abstract class having subclasses
for each type of the simulations. However, in the implementation of the project,
the implementation of different simulations are handled in Model, and the
Control class switches between different simulations based on the data loaded
by loadXmlFile method of Config class.
* In the original plan, Control is part of the View package. However,
in the implementation, Control is in config package.
* In the original plan, saveXmlFile and loadXmlFile are both methods
in the config class. However, in the implementation, saveXmlFile is moved
to Saving class.
* editParameter() class noted in the original plan is not implemented.
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

