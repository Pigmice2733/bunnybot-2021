package com.pigmice.frc.robot.motorconfig;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;

public class CTRE {
    public static void configCurrentLimit(IMotorControllerEnhanced motor) {
        motor.configContinuousCurrentLimit(10, 10);
        motor.configPeakCurrentLimit(20, 10);
        motor.configPeakCurrentDuration(500, 10);
    }

    public static void configureDriveMotor(IMotorControllerEnhanced motor) {
        configureVoltageComp(motor);
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        motor.setSelectedSensorPosition(0, 0, 10);
    }

    public static void configureFollowerMotor(IMotorController follower, IMotorController leader) {
        configureVoltageComp(follower);
        follower.follow(leader);
        follower.setInverted(leader.getInverted());
    }

    public static void configureVoltageComp(IMotorController motor) {
        motor.configVoltageCompSaturation(11.0, 10);
        motor.enableVoltageCompensation(true);
        motor.configVoltagemeasurementFilter(32, 10);
    }
}