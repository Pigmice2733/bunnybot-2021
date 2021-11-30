package com.pigmice.frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
// ^ I don't fully understand any of these but they need to be here

public class Dispenser extends SubsystemBase {
    private final DoubleSolenoid solenoid; // creates the variable
    // initialize values
    private Value targetPistonState = Value.kReverse;
    private Value previousPistonState = Value.kOff;

    public void open() { // opens the door by extending the piston
        targetPistonState = Value.kForward;
    }

    public void close() { // closes the door by retracting the piston
        targetPistonState = Value.kReverse;
    }

    public void periodic() { // applies the target state once every 20 ms
        if (targetPistonState != previousPistonState) { // If they disagree,...
            solenoid.set(targetPistonState); // ... put the piston where it is supposed to go,...
            previousPistonState = targetPistonState; // ... then replace the "previous state."
        }
    }
}


/* There are no commands for this subsystem, as it would not be practical to make them. Instead, the
following "instant commands" should be run whenever the dispenser needs to open/close:
new InstantCommand(Dispenser::open, Dispenser)
new InstantCommand(Dispenser::close, Dispenser)
I'm not entirely sure how these work, I believe they are run when the button is pressed to open/close the
dispenser.
*/


// THE BELOW CODE SHOULD NOT BE HERE
// It needs to be transferred to something that is run periodicaly, most likely in Robot.java.

/* Define triggerOpenDisp as the trigger/button that is activated to open the dispenser, and triggerCloseDisp
the one to close it. Without the XboxController code I'm not sure how to do this, and it doesn't need to be
here anyway.

{
    triggerOpenDisp.whenActive(new InstantCommand(Dispenser::open, Dispenser));
    triggerCloseDisp.whenActive(new InstantCommand(Dispenser::close, Dispenser));
} */