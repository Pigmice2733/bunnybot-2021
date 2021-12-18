package com.pigmice.frc.robot.subsystems.impl;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.subsystems.SystemConfig.IntakeConfiguration;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private TalonSRX bottomMotor, bandMotor, topMotor;
    private boolean enabled = false;
    private final double speed = 0.25;
    private static Intake instance = null;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }

        return instance;
    }

    public Intake() {
        bottomMotor = new TalonSRX(IntakeConfiguration.bottomMotorPort);
        bottomMotor.setInverted(true);
        bandMotor = new TalonSRX(IntakeConfiguration.bandMotorPort);
        bandMotor.setInverted(true);
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
            bottomMotor.set(TalonSRXControlMode.PercentOutput, speed*2);
            bandMotor.set(TalonSRXControlMode.PercentOutput, speed*3);
            topMotor.set(TalonSRXControlMode.PercentOutput, speed);
        } else {
            bottomMotor.set(TalonSRXControlMode.PercentOutput, 0.0);
            bandMotor.set(TalonSRXControlMode.PercentOutput, 0.0);
            topMotor.set(TalonSRXControlMode.PercentOutput, 0.0);
        }
    }
}