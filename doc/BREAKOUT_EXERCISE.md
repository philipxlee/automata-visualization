# Breakout Abstractions Lab Discussion
#### Abhishek Chataut (ac802), Philip Lee (kl445), , Saad Hakim (sh604)


### Block

This superclass's purpose as an abstraction:
```java
 public class Block {
    // what to do when the block gets hit
    public void getHit ()
    // method to change color of block depending on HP
    public void updateColor ()
    // method to move the block depend on whether the lap method is non zero
    public void moveDown ()
 }
```

#### Subclasses
Each subclass's high-level behavorial differences from the superclass

There are subclasses for the unique types of blocks that have different behaviors when hit.
```java
public class SlidingBlock extends Block {}

public class IndestructableBlock extends Block {}

public class ExplodingBlock extends Block {}

public class PowerupBlock extends Block {}

```
#### Affect on Game/Level class

Which methods are simplified by using this abstraction and why

This lets you simply call getHit and manage particular behavior of block within the subclasses. You can also 
store all the block types together and treat them the same.

### Power-up

This superclass's purpose as an abstraction:
```java
 public abstract class PowerUp {
    // move power up down
     public void fallDown ()
    // method to check whether the PowerUP is eaten by the paddle
    public void checkEat (Paddle paddle)
    // do unique thing for each power up
    public abstract void applyEffect()
 }
```

#### Subclasses

Each subclass's high-level behavorial differences from the superclass
```java
    public class LongPaddlePowerUp extends PowerUp {}

    public class StrongerBallPowerUp extends PowerUp {}

    public class ChangeBallColorPowerUp extends PowerUp {}

    public class IncreaseLivesPowerUp extends PowerUp {}
    
    public class IncreaseBallSpeedPowerUp extends PowerUp {}

```

#### Affect on Game/Level class

Which methods are simplified by using this abstraction and why

This way all the power up effects are stored in one place and modular and you can simply call applyEffect on 
the power up and not worry about anything else.



### Others?
