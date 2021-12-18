package com.pigmice.frc.robot.subsystems.impl;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.pigmice.frc.robot.subsystems.SystemConfig.DispenserConfiguration;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Dispenser extends SubsystemBase {

    private final DoubleSolenoid solenoid = new DoubleSolenoid(
            DispenserConfiguration.DispenserSolenoidPorts[0],
            DispenserConfiguration.DispenserSolenoidPorts[1]); // creates the object
                                                               // initialize values
    private Value targetPistonState = Value.kReverse;
    private Value previousPistonState = Value.kOff;

    private static Dispenser instance = null;

    public static Dispenser getInstance() {
        if (instance == null) {
            instance = new Dispenser();
        }
        return instance;
    }

    public void open() { // opens the door by extending the piston
        targetPistonState = Value.kReverse;
    }

    public void close() { // closes the door by retracting the piston
        targetPistonState = Value.kForward;
    }

    public void toggle() {
        targetPistonState = targetPistonState == Value.kForward ? Value.kReverse : Value.kForward;
    }

    public void periodic() { // applies the target state once every 20 ms
        if (targetPistonState != previousPistonState) { // If they disagree,...
            solenoid.set(targetPistonState); // ... put the piston where it is supposed to go,...
            previousPistonState = targetPistonState; // ... then replace the "previous state."
        }
    }
}