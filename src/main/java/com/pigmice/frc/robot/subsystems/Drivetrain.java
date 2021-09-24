package com.pigmice.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
// import com.pigmice.frc.lib.pidf.Gains;
// import com.pigmice.frc.lib.pidf.PIDF;
// import com.pigmice.frc.lib.utils.Odometry;
// import com.pigmice.frc.lib.utils.Range;

public class Drivetrain {
    private TalonSRX leftDrive, rightDrive;
    private AHRS navx;
    private double trackWidth;

    private static final double ticksPerFoot = 4096 / (Math.PI * 0.5);

    public Drivetrain(TalonSRX leftDrive, TalonSRX rightDrive, AHRS navx, double trackWidth) {
        this.leftDrive = leftDrive;
        this.rightDrive = rightDrive;
        this.navx = navx;
        this.trackWidth = trackWidth;

        // Gains alignmentGains = new Gains(-0.015, 0.0, 0.0);
    }

    public double getTrackWidth() {
        return trackWidth;
    }

    public void initializePID() {
        leftDrive.setSelectedSensorPosition(0, 0, 100);
        rightDrive.setSelectedSensorPosition(0, 0, 100);
    }

    public void stop() {
        tankDrive(0.0, 0.0);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        leftDrive.set(ControlMode.PercentOutput, leftSpeed);
        leftDrive.set(ControlMode.PercentOutput, rightSpeed);
    }

    public void arcadeDrive(double forwardSpeed, double turnSpeed) {
        tankDrive(forwardSpeed + turnSpeed, forwardSpeed - turnSpeed);
    }

    public double getSensorPosition() {
        double avg = 0.5 * (leftDrive.getSelectedSensorPosition(0) + rightDrive.getSelectedSensorPosition(0));
        return avg / ticksPerFoot;
    }

    public double getLeftSensorPosition() {
        return leftDrive.getSelectedSensorPosition(0) / ticksPerFoot;
    }

    public double getRightSensorPosition() {
        return rightDrive.getSelectedSensorPosition(0) / ticksPerFoot;
    }

    public double getSensorVelocity() {
        double avg = 0.5 * (leftDrive.getSelectedSensorVelocity(0) + rightDrive.getSelectedSensorVelocity(0));
        return avg / ticksPerFoot;
    }
}