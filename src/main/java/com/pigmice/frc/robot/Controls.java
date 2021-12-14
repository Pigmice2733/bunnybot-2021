package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController;

public class Controls {
    private XboxController driver;
    private XboxController operator;

    public Controls(XboxController driver, XboxController operator) {
        this.driver = driver;
        this.operator = operator;
    }

    public double turnSpeed() {
        double left = driver.getX(Hand.kLeft);
        double right = driver.getX(Hand.kRight);
        return (right > 0 ? right / 3 : left) / 3;
    }

    public double driveSpeed() {
        double value = driver.getTriggerAxis(Hand.kRight) - driver.getTriggerAxis(Hand.kLeft);
        return value / 3;
    }

    public boolean getYbutton() {
        return driver.getYButton();
    }

    /*
     * public boolean intake() {
     * return operator.intake();
     * }
     */
}
