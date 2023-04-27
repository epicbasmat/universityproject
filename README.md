# Cellular Ecosystem - A final year university project
Cellular Ecosystem is a final year university project developed in Java 19 with the Swing Framework from Java.
This application focuses on simulating a cellular ecosystem where cells can live and die by applying a series of rules that determine how the simulation affects cells.

## The System and Simulation
### Initial start-up phase
The start-up phase shows the system variables that the user can change, within reasonable limits.  
The user can then select ``Generate new simulation`` with the specified parameters, or click ``Alternatively, load from a .dat file`` to load a save from a file.
will then start the simulation

![image](https://user-images.githubusercontent.com/43749261/233792521-3df82adc-e325-4688-8ee7-7abadc2a04c0.png)



### The Simulation

The user will then get directed to the main simulation interface. This interface contains the rendered simulation, and buttons that the user can click to change the state of the system.  
The button panel on the right hand side contains the ability to pause/play the simulation, saving the simulation
as either a PNG or as a .dat file, or enable Editing mode, which when clicked the user can then change the simulation by clicking on it to set the position to a certain cell, designated by the dropdown menu.  
To the right of the button menu, there is an interface for showing the system variables in place and how many Life Cells / Society Cells there are. The bottom text panel is used for showing events that have occurred, and any clicks on the simulation that show data.

![image](https://user-images.githubusercontent.com/43749261/233792536-febf617e-9358-4ce8-852d-c93427d5ef1e.png)

#### The Simulation - World Cells
World cells act as the backing for the system, and affect how the cells can interact with the world. Certain World Cells cannot be traversed by Life Cells, or not inhabited.
The World is randomly generated through Perlin Noise generation, by mapping the Perlin Noise hash to the (x, y) of the simulation and applying Cubic Spline Interpolation to the coordinate.
The result is a terrain that looks semi-realistic. Terrain ranges from deep water ![deep_water](https://user-images.githubusercontent.com/43749261/230655988-4382b880-30dd-458c-9c4d-5e8215afa714.png)
to grass ![grass](https://user-images.githubusercontent.com/43749261/230656101-5b3c06e3-3b5c-410f-a227-75eb8d473e71.png) to mountain peak ![mountain_peak](https://user-images.githubusercontent.com/43749261/230656124-e20fe1f7-2c5d-48d7-9f8c-0905e802abdf.png)

#### The Simulation - Life Cells
Life Cells ![life](https://user-images.githubusercontent.com/43749261/230656873-57174461-ec83-494f-be14-cfa285d7d22b.png)
are the primary entity within the system that interacts with the environment. Life cells will recreate by seeking another Life Cell and coming into direct contact with it, 
providing both Life Cells are not on a reproductive cool down. Life Cells will be under an umbrella that is referred to as a Society Cell, or a civilization. 
Life Cells can seek, live and procreate by being near other life cells. The rule set that dictates this behaviour is known as the Gardener.
Life Cells may die through being abandoned and alone after a series of steps, if the food within the civilization cannot be stretched enough for everyone within the civilization, if there
are too many Life Cells within an area, or if a civilization collapses - which kills all Life Cells within a civilization. This rule set for death is known as the Winnower.

#### The Simulation - Nutrient Cells
Nutrient Cells ![nutrient](https://user-images.githubusercontent.com/43749261/230657031-0747cf37-bd03-4397-aaee-f9093de1ca73.png) provide food for a civilization. If a nutrient cell is within 
an area of influence over a civilization then the food from the Nutrient Cells get distributed to each Life Cell within a civilization. A Nutrient Cell can support several Life Cells, which is determined by an internal range.

#### The Simulation - Civilization Cells
Society or Civilization Cells ![society](https://user-images.githubusercontent.com/43749261/230657455-a51a68b4-3071-49ea-85e4-73139806c22c.png) are the primary structure of how Life Cells operate and Nutrient Cells get distributed.
Civilization Cells may fall if the ratio of people to land area exceeds a certain threshold. 

## Prerequisites
To run this program, the latest version of Java is recommended which you can download from OpenJDK or Oracle.

To build this program, JDK 19 is recommended which you can also download from OpenJDK or Oracle.

## How to use this repository
1) Fork the repository
2) Clone the repository by clicking Clone or cloning it from the command line
`git clone https://github.com/epicbasmat/universityproject.git`
3) Open in IntelliJ or Eclipse and run Main.java 
4) Alternatively, direct the CLI into the `Main.java` system and execute `javac Main.java`
5) The program will now start.
