package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.wpilibj.SPI;

public class SystemConfig {
    public static class DrivetrainConfiguration {
        public static final int frontRightMotorPort = 5;
        public static final int backRightMotorPort = 6;
        public static final int frontLeftMotorPort = 3;
        public static final int backLeftMotorPort = 4;

        public static final SPI.Port navxPort = SPI.Port.kMXP;

        public static final double rotationToDistanceConversion = 16.13;
        public static final double wheelBase = 0.603;
    }
}
