package com.pigmice.frc.robot;

import com.pigmice.frc.robot.subsystems.impl.ColorSorter;
import com.pigmice.frc.robot.subsystems.impl.Drivetrain;
import com.pigmice.frc.robot.subsystems.impl.Intake;
import com.pigmice.frc.robot.subsystems.impl.Dispenser;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Controls {
    private XboxController driver;
    private XboxController operator;

    public Controls(XboxController driver, XboxController operator) {
        this.driver = driver;
        this.operator = operator;

        this.bindDriverControls();
        this.bindOperatorControls();
    }

    // private interface OperatorProfile {
    // boolean intake();
    // }

    public void bindDriverControls() {
    }

    public void bindOperatorControls() {
        final XboxController controller = new XboxController(1);
        JoystickButton XButton = new JoystickButton(controller, Button.kX.value);
        final ColorSorter colorSorterSubsystem = ColorSorter.getInstance();
        final Intake intakeSubsystem = Intake.getInstance();
        XButton.toggleWhenPressed(new InstantCommand(() -> {
            intakeSubsystem.toggle();
            colorSorterSubsystem.toggle();
        }));
        final JoystickButton AButton = new JoystickButton(controller, Button.kA.value);
        final JoystickButton RBumper = new JoystickButton(controller, Button.kBumperRight.value);
        final JoystickButton BButton = new JoystickButton(controller, Button.kB.value);
        final Dispenser dispenserSubsystem = Dispenser.getInstance();
        AButton.and(RBumper).whenActive(new InstantCommand(dispenserSubsystem::open));
        BButton.whenPressed(new InstantCommand(dispenserSubsystem::close));
    }

    public double turnSpeed() {
        double epsilon = 0.1;

        double left = driver.getX(Hand.kLeft);
        left = Math.abs(left) > epsilon ? left : 0;

        double right = driver.getX(Hand.kRight);
        right = Math.abs(right) > epsilon ? right : 0;

        return (right != 0 ? right / 4 : left) / 4;
    }

    public double driveSpeed() {
        double value = driver.getTriggerAxis(Hand.kRight) - driver.getTriggerAxis(Hand.kLeft);
        Drivetrain drivetrain = Drivetrain.getInstance();
        return value * (drivetrain.isBoosting() ? 1 : drivetrain.isSlow() ? 0.375 : 0.25);
    }

    /*
     * public boolean intake() {
     * return operator.intake();
     * }
     */
}