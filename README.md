# Cell Society - Team 3
## Philip Lee (kl445), Saad Hakim (sh604), Abhishek Chataut (ac802)

This project implements a cellular automata simulator.

### Timeline

 * Start Date: January 29th, 2024

 * Finish Date: February 12, 2024

 * Hours Spent: 80 hours



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

 * Known Bugs:
   * N/A 

 * Features implemented:
   * [CELL-01]: Conway's Game of Life
   * [CELL-02]: Spreading of Fire
   * [CELL-03]: WaTor World
   * [CELL-04]: Schelling's Model of Segregation
   * [CELL-05]: Water Percolation 
   * [CELL-06]: 3 simulation configurations showing patterns for Conway's Game of Life
   * [CELL-16]: XML-Based Simulation Configuration
   * [CELL-17]: Cell Grid View
   * [CELL-18]: Display Simulation Information
   * [CELL-19]: Load New Configuration File
   * [CELL-20]: Start Simulation
   * [CELL-21]: Pause Simulation
   * [CELL-22]: Simulation Speed Adjustment
   * [CELL-23]: Save Simulation State as XML
   * [CELL-24]: Edit Simulation Save
   * [CELL-25]: Reset and Clear Grid Functionality
   * [CELL-26A]: Falling Sand
   * [CELL-26B]: Foraging Ants
   * [CELL-27]: Input Missing Parameters
   * [CELL-28]: Invalid Value Check
   * [CELL-29]: Invalid Cell State Check
   * [CELL-30]: Grid Bounds Check
   * [CELL-31]: File Format Validation
   * [CELL-33X]: Grid Edge Type - Custom (Vertical Split)
   * [CELL-35B]: Cell Shape - Triangle
   * [CELL-36]: Multiple Simultaneous Simulations
   * [CELL-37]: Simulation Language Customization
   * [CELL-38]: Cell State Colors Customization
   * [CELL-40B]: Graph of Cell Population Changes
   * [CELL-41A]: Dynamic Updates: Simulation Parameters
   * [CELL-41B] Dynamic Updates: Grid States
   * [CELL-41C]: Dynamic Updates: Grid Outline
   * [CELL-41E]: Dynamic Updates: Cell Shape
   

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


### Assignment Impressions
   * Working on Cell Society was both challenging and incredibly interesting. The complexity of the 
   cellular automaton required us to carefully design and implement the rules governing cell 
   behavior, ensuring that they accurately represented the real-world phenomena we wanted to 
   simulate. We felt that Cell Society was difficult, but working together step by step made the 
   challenge less daunting. It was also a great exercise in practicing communication with our
   groups, setting deadlines, meetings, and targets, etc.
   * Collaborating with our team and utilizing version control with Git added a rewarding 
    dimension to the project. The ability to work together, track changes, and merge 
    contributions allowed us to effectively manage the development process. Connecting all the 
    simulations and integrating everyone's work into a cohesive whole was also really cool, as
    it showed us that we had overcome a lot of challenges, such as the occasional merge conflicts.
    


