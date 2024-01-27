# Cell Society Design Lab Discussion
#### Philip Lee (kl445), Saad Hakim (sh604), Abhishek Chataut (ac802)


### Simulations

* Commonalities
  * Each cell is affected by its neighbors' states
  * Grid of cells
  * Has rules defining how cells change states

* Variations
  * In Schelling's model, the contents of the cells (color) change their position depending on
  the state of their neighbors, rather than their state
  * Conway's Game of Life only has two states while the others have more. 
  * Each cell is also affected by the number of neighbors with a certain state
  * Fire and Schelling are probabilistic, but Game of Life is deterministic


### Discussion Questions

* How does a Cell know what rules to apply for its simulation?
  * By checking its neighbors

* How does a Cell know about its neighbors?
  * A hashmap that hashes the cell (i, j) with a list of its neighbors [(x0, y0), (x1, y1) ... (xn, yn)]

* How can a Cell update itself without affecting its neighbors update?
  * It simultaneously applies the set of rules to all the cells at once, so if a cell changes that will not immediately have an affect

* What behaviors does the Grid itself have?
  * The grid would initialize with an initial state of the alive/dead cells
  * The grid would have two dimensions

* How can a Grid update all the Cells it contains?
  * Make a copy of the grid and loop through the original grid and add to the copy matrix one by one

* What information about a simulation needs to be in the configuration file?
  * simulation type (e.g., Game of Life, Fire)
  * title
  * authors
  * description
  * grid dimensions
  * initial cell states
  * specific simulation parameters.

* How is configuration information used to set up a simulation?
  * It is read from an XML file

* How is the graphical view of the simulation updated after all the cells have been updated?
  * It renders the new grid of updated cells on the next tick of the game


### Alternate Designs

#### Design Idea #1

* Data Structure #1 and File Format #1

##### Package Display_and_Contol
  * Display
  - Manages the scene creation and maintenance
  - Shows the grid and its color changes

  * Control
  - Implements Start, Pause, Speed, Save, Edit, and Reset functionality


##### Package Model
  * Grid
  - Implements the grid(s)
  - Handles behavior of what how it affects neighbors

  * Cell
  - Keep track of the state of the cell and its neighbors
  - Updates the state of the cell


##### Package Config
  * Config (Abstract/Interface class)
  - Manages the reading of the files
  - Each subclass be a type of simulation
  - Ensures all specifications in XML is implemented




### High Level Design Goals

Separate classes into three packages: Display_and_Control, Model, and Config
Model package has no information about JavaFX and is only concerned with the logic of the simulation. 
Display will be concerned with the graphical representation of the simulation from model.
Config will be concerned with reading the XML file and creating the simulation model from the information in the file.


### CRC Card Classes

This class's purpose or value is to represent a customer's order:

| Display                            |         |
|------------------------------------|---------|
| void renderGrid(Cell[][] nextGrid) | Cell    |
| void renderControlOverlay()        | Control |
| void updateControlOverlay()        |         |
|                                    |         |


| Control                                                                                   |         |
|-------------------------------------------------------------------------------------------|---------|
| void start()                                                                              | Cell    |
| void pause()                                                                              | Display |
| void reset ()                                                                             |         |
| void save ()                                                                              |         |
| void edit (String fileName, String fileLocation, String fileAuthor, String fileDescription |         |

| Grid                                 |      |
|--------------------------------------|------|
| Cell[][] updateGrid()                | Cell |
|                |      |

| Config                        |          |
|-------------------------------|----------|
| void loadXMLFile(String path) |          |
| void saveXMLFile(String path) |          |




This class's purpose or value is to represent a customer's order:
```java
public class Display {
     // render grid of colors based on simulation grid 
     public void renderGrid(Cell[][] nextGrid)
     // render buttons like save, start, pause, etc.
     public void renderControlOverlay()
    // handle button logic
     public void updateControlOverlay()
 }
 ```

```java
public class control {
    // start the game
    public void start()
    // pause the game
    public void pause()
    // reset the state of the grid
    public void reset()
    // save the grid state in an XML file
    public void save()
    // edit the information of the saved file
    public void edit()
 }
 ```


This class's purpose or value is to manage something:
```java
public class Config {
     // load simulation state from xml file
     public void loadXMLFile(String path)
	 // save xml simulation state to xml file
     public void saveXMLFile(String path)
     // change simulation state
      public void useSimulationState()
 }
```

```java
public class Cell {
     // update cell state based on rules
     public void updateState()
     // get cell's neighbors
     public Cell[] getNeighbors(Cell[][] grid)
 }
```


### Use Cases

* Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)
```java
  cell.updateState();
```

* Apply the rules to an edge cell: set the next state of a cell to live by counting its number of neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors missing)
```java
    cell.updateState();  // Edge cases handled in implementation
        
```

* Move to the next generation: update all cells in a simulation from their current state to their next state and display the result graphically
```java
    grid.updateGrid();
    display.renderGrid(grid);
```

* Set a simulation parameter: set the value of a parameter, probCatch, for a simulation, Fire, based on the value given in a data file
```java
    config.loadXMLFile("data/fire.xml");
```

* Switch simulations: load a new simulation from a data file, replacing the current running simulation with the newly loaded one
```java
    config.loadXMLFile("data/fire.xml");
    config.useSimulationState();
    
```