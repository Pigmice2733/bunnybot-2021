package com.pigmice.frc.robot.subsystems.impl;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.pigmice.frc.robot.subsystems.SystemConfig.IntakeConfiguration;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final TalonSRX leftMotor,rightMotor;
    private boolean enabled = false;
    private final double speed = 0.5;
    public Intake() {
        leftMotor = new TalonSRX(IntakeConfiguration.leftMotorPort);
        rightMotor = new TalonSRX(IntakeConfiguration.rightMotorPort);
        rightMotor.setInverted (true);
        rightMotor.set(TalonSRXControlMode.Follower, IntakeConfiguration.leftMotorPort);
        
    }
    public void setEnabled (boolean enabled) {
        this.enabled = enabled;
    }
    public void enable () {
        setEnabled(true);
    }
    public void disable () {
        setEnabled(false);
    }
    @Override
    public void periodic() {
        if(this.enabled) {
            leftMotor.set (TalonSRXControlMode.PercentOutput, speed);
        }
        else {
            leftMotor.set (TalonSRXControlMode.PercentOutput, 0.0);
        }
    }
}
