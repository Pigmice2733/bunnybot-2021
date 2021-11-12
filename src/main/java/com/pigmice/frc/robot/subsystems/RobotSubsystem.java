package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class RobotSubsystem extends SubsystemBase {
    /**
     * Initialize the subsystem
     */
    public abstract void initialize();

    /**
     * Updates variables with sensor values
     */
    public abstract void updateInputs();
    
    /**
     * Updates the physical state of the subsystem
     */
    public abstract void updateOutputs();

    /**
     * Updates values on SmartDashboard
     */
    public abstract void updateDashboard();

    /**
     * Starts robot test
     */
    public abstract void test(double currentTestTime);
}