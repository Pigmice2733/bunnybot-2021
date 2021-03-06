package com.pigmice.frc.robot.commands.subroutines;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

import com.pigmice.frc.robot.subsystems.impl.Drivetrain;

public class TurnToAngle extends PIDCommand {
    private final double maxError = 0.02;
    private final double maxTurnSpeed = 0.25 * Math.PI;

    private final static double turnP = 1.0D;
    private final static double turnI = 0.0D;
    private final static double turnD = 0.0D;

    public TurnToAngle(double targetHeading, boolean absolute, Drivetrain drivetrain) {
        super(
                new PIDController(turnP, turnI, turnD),
                drivetrain::getHeading,
                absolute ? targetHeading : targetHeading + drivetrain.getHeading(),
                output -> drivetrain.arcadeDrive(0, output),
                drivetrain);

        getController().enableContinuousInput(-Math.PI, Math.PI);

        getController().setTolerance(maxError, maxTurnSpeed);
    }

    @Override
    public void execute() {
        // System.out.println("TURNING TO ANGLE");
        try {
            super.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isFinished() {
        return getController().atSetpoint();
    }
}