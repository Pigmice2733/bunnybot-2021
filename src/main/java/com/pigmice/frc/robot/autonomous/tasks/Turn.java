package com.pigmice.frc.robot.autonomous.tasks;

import com.pigmice.frc.lib.controllers.PID;
import com.pigmice.frc.lib.controllers.PIDGains;
import com.pigmice.frc.lib.motion.execution.ProfileExecutor;
import com.pigmice.frc.lib.motion.profile.StaticProfile;
import com.pigmice.frc.lib.utils.Range;
import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turn implements ITask {
    private ProfileExecutor executor;
    private PID turningPID;

    private final Drivetrain drivetrain;

    private final double targetRotation;
    private double targetHeading = 0.0;
    private double initialHeading = 0.0;
    private final boolean absolute;

    public Turn(Drivetrain drivetrain, double radians) {
        this(drivetrain, radians, false);
    }

    public Turn(Drivetrain drivetrain, double radians, boolean absolute) {
        this.drivetrain = drivetrain;
        this.targetRotation = radians;
        this.absolute = absolute;

        SmartDashboard.putNumber("Target Rotation", this.targetRotation);
        SmartDashboard.putBoolean("Absolute", absolute);

        PIDGains gains = new PIDGains(0.0, 0.0, 0.0, 0.0, 0.6 / (2 * Math.PI), 0.00135);
        Range outputBounds = new Range(-0.8, 0.8);
        turningPID = new PID(gains, outputBounds, 0.02);
    }

    @Override
    public void initialize() {
        // drivetrain.zeroHeading();
        SmartDashboard.putBoolean("NavX Calibrating", true);
        while (drivetrain.isCalibrating()) {}
        SmartDashboard.putBoolean("NavX Calibrating", false);
        initialHeading = drivetrain.getHeading();
        targetHeading = absolute ? targetRotation : initialHeading + targetRotation;

        SmartDashboard.putNumber("Initial Heading", initialHeading);
        SmartDashboard.putNumber("Target Heading", targetHeading);

        StaticProfile profile = new StaticProfile(0.0, initialHeading, targetHeading,
                0.25 * Math.PI, 1.5 * Math.PI, 1.25 * Math.PI);
        executor = new ProfileExecutor(profile, turningPID, this::driveOutput, this::getHeading,
                0.02, 0.05, Timer::getFPGATimestamp);

        turningPID.initialize(0.0, 0.0);
        executor.initialize();
        
    }

    @Override
    public boolean update() {
        return executor.update();
    }
    
    private double getHeading() {
        double heading = drivetrain.getHeading();
        SmartDashboard.putNumber("Current Heading", heading);
        return heading;
    }

    private void driveOutput(double output) {
        SmartDashboard.putNumber("Power Output", output);
        if (Math.signum(targetHeading) == Math.signum(output))
            output = -output;
        drivetrain.tankDrive(-output, output);
    }

}
