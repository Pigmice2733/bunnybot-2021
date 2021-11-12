package com.pigmice.frc.robot.commands.subroutines;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

import com.pigmice.frc.robot.subsystems.impl.Drivetrain;

public class TurnToAngle extends PIDCommand {
    private final double maxError = 0.02;
    private final double maxTurnSpeed = 0.25 * Math.PI;

    public TurnToAngle(double targetHeading, boolean absolute, Drivetrain drivetrain) {
        super(
            new PIDController(0.0, 0.0, 0.0),
            drivetrain::getHeading,
            absolute ? targetHeading : targetHeading + drivetrain.getHeading(),
            output -> drivetrain.arcadeDrive(0, output),
            drivetrain
        );

        getController().enableContinuousInput(-180, 180);

        getController().setTolerance(maxError, maxTurnSpeed);
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }
}