package com.pigmice.frc.robot.autonomous.tasks;

import com.pigmice.frc.lib.controllers.PID;
import com.pigmice.frc.lib.controllers.PIDGains;
import com.pigmice.frc.lib.motion.execution.ProfileExecutor;
import com.pigmice.frc.lib.motion.profile.StaticProfile;
import com.pigmice.frc.lib.utils.Point;
import com.pigmice.frc.lib.utils.Range;
import com.pigmice.frc.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive implements ITask {
    private ProfileExecutor executor;
    private PID drivingPID;

    private final Drivetrain drivetrain;

    private final double targetDistance;
    private Point initialPosition = Point.origin();
    private final boolean reverse;

    public Drive(Drivetrain drivetrain, double meters) {
        this.drivetrain = drivetrain;
        this.targetDistance = Math.abs(meters);

        reverse = meters < 0.0;

        SmartDashboard.putNumber("Initial Distance", meters);
        SmartDashboard.putBoolean("Reverse", reverse);
        SmartDashboard.putNumber("Current Distance", targetDistance);

        PIDGains gains = new PIDGains(2.0, 0.0, 0.0, 0.0, 0.03, 0.005);
        Range outputBounds = new Range(-0.5, 0.5);
        drivingPID = new PID(gains, outputBounds, 0.02);
    }

    public void initialize() {
        // drivetrain.resetPose();
        initialPosition = new Point(drivetrain.getPose());

        StaticProfile profile = new StaticProfile(0.0, 0.0, targetDistance, 1, 2, 1.35);
        executor = new ProfileExecutor(profile, drivingPID, this::output, this::getDistance, 0.05, 0.1,
                Timer::getFPGATimestamp);

        drivingPID.initialize(0.0, 0.0);
        executor.initialize();
    }

    public boolean update() {
        SmartDashboard.putNumber("Current Distance", targetDistance);
        return executor.update();
    }

    private double getDistance() {
        Point currentPosition = new Point(drivetrain.getPose());
        return currentPosition.subtract(initialPosition).magnitude();
    }

    private void output(double output) {
        output = reverse ? -output : output;
        drivetrain.tankDrive(output, output);
    }
}