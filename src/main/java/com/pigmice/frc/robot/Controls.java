package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import com.pigmice.frc.robot.commands.subroutines.TurnToAngle;
import com.pigmice.frc.robot.subsystems.impl.Drivetrain;

import edu.wpi.first.wpilibj.XboxController;

public class Controls {
    private XboxController driver;
    private XboxController operator;

    public Controls(XboxController driver, XboxController operator) {
        this.driver = driver;
        this.operator = operator;

        this.bindDriverControls();
        this.bindOperatorControls();
    }

    public void bindDriverControls() {

    }

    public void bindOperatorControls() {

    }

    public double turnSpeed() {
        double left = driver.getX(Hand.kLeft);
        double right = driver.getX(Hand.kRight);
        return (Math.abs(right) > 0.1 ? right / 4 : left) / 4;
    }

    public double driveSpeed() {
        double value = driver.getTriggerAxis(Hand.kRight) - driver.getTriggerAxis(Hand.kLeft);
        return value / (Drivetrain.getInstance().isBoosting() ? 1 : 4);
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
