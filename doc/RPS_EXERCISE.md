# Rock Paper Scissors Lab Discussion

#### Abhishek Chataut (ac802), Philip Lee (kl445), Saad Hakim (sh604)

### High Level Design Goals

Game class (stores players, weapons)
Weapons class (stores weapon name, what it beats and loses too)
Player class (stores score, name, weapon choice)

### CRC Card Classes

This class's purpose or value is to represent a customer's order:

| Player                                   |         |
|------------------------------------------|---------|
| Weapons promptWeapon(Weapons[])          | Weapons |
| void updateScore  ()                     | Game    |
| int returnScore ()                       | Score   |
| boolean doesWeaponExist (String weapon ) |         |

| Game                          |        |
|-------------------------------|--------|
| Player calculateWin()         | Player |
| void addWeapon(Weapos weapon) |        |
| Weapons[] getWeapons()        |        |
| Weapons chooseRandomWeapon()  |        |
| void nextTurn                 |        |

| Weapons                             | |
|-------------------------------------|-|
| boolean canBeat(Weapons weapon)     | |
| void updateBeatList(Weapons weapon) | |
|                                     | |
|                                     | |

This class's purpose or value is to represent a customer's order:

```java
public class Player {
    // prompt the list of weapons for the player to choose
    public Weapons promptWeapon(Weapons[]) {}
    
    // update score to the game
    public void updateScore() {}
    
    // return currentScore
    public int returnScore() {}
    
    // return if a weapon exists
    public boolean doesWeaponExist() {}
 }
 ```

This class's purpose or value is to manage something:

```java
public class Weapons {
     // if the weapon can beat the given weapon
    public boolean canBeat(Weapons weapon){}
	 // change the list of weapons that the weapon can beat
     void updateBeatList(Weapons weapon){}
 }
```

This class's purpose or value is to manage something:

```java
public class Game {
     // return the player that wins the game
    public Player calculateWin (){}
    // adds the weapon to the list of available weapon
    public void addWeapon (Weapon weapon){}
    // get current weapons list
   public Weapons[] getWeapons (){}
    // choose a weapon for computer to play
    public Weapons chooseRandomWeapon (){}
    // go to next turn, print necessary stuff
    public void nextTurn (){}
 }
```

### Use Cases

* A new game is started with five players, their scores are reset to 0.

 ```java
Player player1 = new Player();
Player player2 = new Player();
Player player3 = new Player();
Player player4 = new Player();
Player player5 = new Player();
 ```

* A player chooses his RPS "weapon" with which he wants to play for this round.

 ```java
player1.promptWeapon();
 ```

* Given three players' choices, one player wins the round, and their scores are updated.

 ```java
player1.promptWeapon();
player2.promptWeapon();
player3.promptWeapon();
winner = calculateWin();
players.updateScore();
 ```

* A new choice is added to an existing game and its relationship to all the other choices is
  updated.

 ```java
currentWeapons = getWeapons();
addWeapon(newWeapon);
 ```

* A new game is added to the system, with its own relationships for its all its "weapons".

 ```java
Game game = new Game();
game.addWeapon(newWeapon);
 ```
