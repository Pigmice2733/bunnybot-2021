package com.pigmice.frc.robot.autonomous.tasks;

import com.pigmice.frc.lib.controllers.PID;
import com.pigmice.frc.lib.controllers.PIDGains;
import com.pigmice.frc.lib.motion.execution.ProfileExecutor;
import com.pigmice.frc.lib.motion.profile.StaticProfile;
import com.pigmice.frc.lib.utils.Range;
import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;

public class Turn implements ITask {
    private ProfileExecutor executor;
    private PID turningPID;

    private final Drivetrain drivetrain;

    private final double targetRotation;
    private double targetAngle = 0.0;
    private double initialAngle = 0.0;
    private final boolean absolute;

    public Turn(Drivetrain drivetrain, double radians) {
        this(drivetrain, radians, false);
    }

    public Turn(Drivetrain drivetrain, double radians, boolean absolute) {
        this.drivetrain = drivetrain;
        this.targetRotation = radians;
        this.absolute = absolute;

        PIDGains gains = new PIDGains(0.0, 0.0, 0.0, 0.0, 0.6 / (2 * Math.PI), 0.00135);
        Range outputBounds = new Range(-0.1, 0.1);
        turningPID = new PID(gains, outputBounds, 0.02);
    }

    @Override
    public void initialize() {
        targetAngle = absolute ? targetRotation : initialAngle + targetRotation;

        StaticProfile profile = new StaticProfile(0.0, initialAngle, targetAngle,
                2 * Math.PI, 1.5 * Math.PI, 1.25 * Math.PI);
        executor = new ProfileExecutor(profile, turningPID, this::driveOutput, this::getAngle,
                0.02, 0.05, Timer::getFPGATimestamp);

        turningPID.initialize(0.0, 0.0);
        executor.initialize();
        
    }

    @Override
    public boolean update() {
        return executor.update();
    }

    private double getAngle() {
        return drivetrain.getHeading();
    }

    private void driveOutput(double output) {
        drivetrain.tankDrive(-output, output);
    }

}
