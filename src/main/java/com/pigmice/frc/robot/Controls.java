package com.pigmice.frc.robot;

import com.pigmice.frc.robot.subsystems.impl.Drivetrain;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Controls {
    private XboxController driver;
    private XboxController operator;

    public Controls(XboxController driver, XboxController operator) {
        this.driver = driver;
        this.operator = operator;
    }

    public void bindDriverControls() {
    }

    public void bindOperatorControls() {
    }

    public double turnSpeed() {
        double epsilon = 0.2;

        double left = driver.getX(Hand.kLeft);
        left = Math.abs(left) > epsilon ? left : 0;

        double right = driver.getX(Hand.kRight);
        right = Math.abs(right) > epsilon ? right : 0;

        return (right != 0 ? right / 4 : left) / 4;
    }

    public double driveSpeed() {
        double deadzone = 0.1;
        double value = driver.getTriggerAxis(Hand.kRight) - driver.getTriggerAxis(Hand.kLeft);
        value = Math.abs(value) > deadzone ? value : 0;
        Drivetrain drivetrain = Drivetrain.getInstance();
        return value * (drivetrain.isBoosting() ? 1 : drivetrain.isSlow() ? 0.09375 : 0.25);
    }
}