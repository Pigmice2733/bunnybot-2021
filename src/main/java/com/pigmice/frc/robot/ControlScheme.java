package com.pigmice.frc.robot;

import com.pigmice.frc.lib.controls.ButtonDebouncer;
import com.pigmice.frc.lib.controls.SubstateToggle;
import com.pigmice.frc.lib.controls.Toggle;
import com.pigmice.frc.robot.superstructure.Pose;
import com.pigmice.frc.robot.superstructure.SuperStructure;

import edu.wpi.first.wpilibj.Joystick;

public class ControlScheme {
    Joystick driver = new Joystick(0);

    public void initialize() {

    }

    public void update() {

    }

    public double steer() {
        double steering = driver.getX();
        return Math.pow(steering, 2) * Math.signum(steering);
    }

    public double drive() {
        return -driver.getY();
    }

    public boolean getA() {
        return driver.getRawButton(1);
    }

    public boolean getB() {
        return driver.getRawButton(2);
    }

    public boolean X() {
        return driver.getRawButton(3);
    }

    public boolean Y() {
        return driver.getRawButton(4);
    }

    public boolean leftBumper() {
        return driver.getRawButton(5);
    }

    public boolean rightBumper() {
        return driver.getRawbutton(6);
    }
}