# Cellular Automata Visualization

This project implements a cellular automata simulator.


### Attributions

* Oracle JavaFX Documentation -- https://docs.oracle.com/javase/8/javafx/api/toc.htm

* OpenAI ChatGPT -- https://chat.openai.com

* GeeksForGeeks JavaFX Tutorial -- https://www.geeksforgeeks.org/javafx-tutorial/


### Running the Program

 * Main class: `Main.java`

 * Data files needed: `data/[variations_folders]`

 * Interesting data files: 
   * `Percolation2.xml` will show the vertical edge policy, where the percolated cells
   will not percolate pass the vertical split in the middle of the grid, effectively creating a new
   edge.
   * `GameOfLifeBlinker.xml` will show the blinker pattern in Conway's Game of Life.
   * `GameOfLifeGlider.xml` will show the glider pattern in Conway's Game of Life.
   * `GameOfLifeToad.xml` will show the Toad pattern in Conway's Game of Life.
 * Key/Mouse inputs: 
   * Press `n` to open up another simulation.
   * Press `next` on the screen to view the next generation of cellular automata.
   * Press `back` on the screen to view the previous generation of cellular automata.
   * Press `play` on the screen to indefinitely run the simulation.
   * Press `pause` on the screen to stop the simulation.
   * Press `toggle graph` on the screen to view the graph of the simulation showing data on the 
     simulation.
   * Press `toggle outline` on the screen to toggle the outline of the cells in the simulation.
   * Press `cell shape` on the screen to toggle the cell shape of the cells in the simulation.
   * Press `save` to save the current state of the cellular automata.
   * Drag the `simulation speed` slider to increase or decrease the ticks of the simulation.


### Notes/Assumptions

 * Assumptions or Simplifications:
   * For `Foraging Ants`, ants cells and other states may not show until a threshold is reached.
   For instance, ants may be in different cells within the simulation, but will only display when 
   there are at least 6 ants in that cell. Displaying every ant would take away from the view of 
   pheromones, food, and other cells. The program runs under the assumption that the behavior of 
   ant cells as a whole is more important that viewing every single ant that is in the simulation.
   * For `WaTor`, the simulation does not run simualtaneously compared to other simulations. The 
   reason for this is to allow the sharks to eat the fish in the same generation. If the fish and
   shark moves simultaneously, a situation may arise where a shark will never be able to reach a
   fish as they may move away at the same speed and direction.
   * For the `xml` files, instead of having the initial grid and its states be inside the file,
   the program simplifies the process by having a field that points to a text file that the program
   loads the grid from.
   * Changing the states dynamically by clicking on a cell within the grid will automatically set it
   as "empty", instead of allowing users to customize which state to change the cell to.

 * Known Bugs:
   * N/A 

 * Features implemented:
   * Conway's Game of Life
   * Spreading of Fire
   * WaTor World
   * Schelling's Model of Segregation
   * Water Percolation 
   * XML-Based Simulation Configuration
   * Display Simulation Information
   * Load New Configuration File
   * Start/Pause Simulation
   * Simulation Speed Adjustment
   * Save Simulation State as XML
   * Edit Simulation Save
   * [CELL-25]: Reset and Clear Grid Functionality
   * Falling Sand
   * Foraging Ants
   * Exception Handling
   * Dynamic updates
   * Graphing of cellular statistics during game state
   * XML Error handling
  

 * Features unimplemented:
   * Although the changing of cell shapes is implemented, the logic of the interactions between the 
   different neighbors created by the changed cell shape is not. For instance, although triggering
   the cell shapes to be `triangle`, water may not flow through the gaps between two triangles in 
   `percolation`, even though there is empty space between it.
   * XML Configuration Initialization, in `change` is not implemented.
   * Extended Moore Neighbors are not implemented.
   
 * Noteworthy Features:
   * A `next` and a `back` button is implemented to view the simulation per time step, as well as 
   its history. This allows users to not only view the logic behind the simulation more clearly, but
   also allow them to watch how the simulation changes over time at their own pace.
   * Moving the cursor on the cells will create a `hover` effect, displaying a opaque color to show 
   users the location of their cursor.

    


