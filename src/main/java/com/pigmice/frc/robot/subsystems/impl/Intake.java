package com.pigmice.frc.robot.subsystems.impl;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.subsystems.SystemConfig.IntakeConfiguration;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private TalonSRX leftMotor, rightMotor, topMotor;
    private boolean enabled = false;
    private final double speed = 0.3;
    private static Intake instance = null;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }

        return instance;
    }

    public Intake() {
        leftMotor = new TalonSRX(IntakeConfiguration.leftMotorPort);
        leftMotor.setInverted(true);
        rightMotor = new TalonSRX(IntakeConfiguration.rightMotorPort);
        // rightMotor.set(TalonSRXControlMode.Follower,
        // IntakeConfiguration.leftMotorPort);
        topMotor = new TalonSRX(IntakeConfiguration.topMotorPort);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable() {
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    public void toggle() {
        this.setEnabled(!this.enabled);
    }

    @Override
    public void periodic() {
        if (this.enabled) {
            leftMotor.set(TalonSRXControlMode.PercentOutput, speed);
            rightMotor.set(TalonSRXControlMode.PercentOutput, speed);
            topMotor.set(TalonSRXControlMode.PercentOutput, speed);
        } else {
            leftMotor.set(TalonSRXControlMode.PercentOutput, 0.0);
            rightMotor.set(TalonSRXControlMode.PercentOutput, 0.0);
            topMotor.set(TalonSRXControlMode.PercentOutput, 0.0);
        }
    }
}