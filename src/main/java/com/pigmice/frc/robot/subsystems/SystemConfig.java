package com.pigmice.frc.robot.subsystems;

import edu.wpi.first.wpilibj.SPI;

public class SystemConfig {
    public static class DrivetrainConfiguration {
        public static final int frontRightMotorPort = 1;
        public static final int backRightMotorPort = 2;
        public static final int frontLeftMotorPort = 4;
        public static final int backLeftMotorPort = 3;

        public static final SPI.Port navxPort = SPI.Port.kMXP;

        public static final double rotationToDistanceConversion = 16.13;
        public static final double wheelBase = 0.603;

        // 0 = navx is mounted facing robot front, 90 degrees = robot right, etc.
        public static final int navXRotationalOffsetDegrees = 0;

    }

    public static class ColorSorterConfiguration {
        public static final int colorSensorMotorPort = 6;
        public static final double colorSensorMotorSpeed = 0.2;
    }

    public static class IntakeConfiguration {
        public static final int bottomMotorPort = 7;
        public static final int bandMotorPort = 8;
        public static final int topMotorPort = 5;
    }

    public static class DispenserConfiguration {
        public static final int[] DispenserSolenoidPorts = new int[] { 0, 1 }; // SUBJECT TO CHANGE
    }
}
