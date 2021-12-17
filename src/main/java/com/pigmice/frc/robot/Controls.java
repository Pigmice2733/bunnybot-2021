package com.pigmice.frc.robot;

import com.pigmice.frc.lib.inputs.Debouncer;
import com.pigmice.frc.lib.inputs.Toggle;
import com.pigmice.frc.robot.subsystems.impl.Intake;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController;
import com.pigmice.frc.robot.commands.subroutines.TurnToAngle;
import com.pigmice.frc.robot.subsystems.impl.Drivetrain;

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
        XboxController controller = new XboxController(1);
        JoystickButton XButton = new JoystickButton(controller, Button.kX.value);
        final Intake intakeSubsystem = Intake.getInstance();
        XButton.toggleWhenPressed((Command) new InstantCommand(intakeSubsystem::toggle));
    }

    public double turnSpeed() {
        double epsilon = 0.1;
        double left = driver.getX(Hand.kLeft);
        left = Math.abs(left) > epsilon ? left : 0d;
        double right = driver.getX(Hand.kRight);
        right = Math.abs(right) > epsilon ? right : 0d;
        return (left != 0 ? left / 4 : right) / 4;
    }

    public double driveSpeed() {
        double value = driver.getTriggerAxis(Hand.kRight) - driver.getTriggerAxis(Hand.kLeft);
        boolean lowSpeed = driver.getAButton();
        value *= lowSpeed ? 0.375 : 1;
        return value / (Drivetrain.getInstance().isBoosting() ? 1 : 4);
    }

    /*
     * public boolean intake() {
     * return operator.intake();
     * }
     */
}
