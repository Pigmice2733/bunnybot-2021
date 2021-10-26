// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.ISubsystem;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Drivetrain drivetrain;

    private final List<ISubsystem> subsystems = new ArrayList<>();

    private final Controls controls = new Controls();

    private double testStartTime;

    // Color Senosr
    private final I2C.Port i2cPort = I2C.Port.kOnboard;

    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

    // Pneumatics
    boolean pneumaticsEnabled = false;

    Compressor c = new Compressor(0);


    @Override
    public void robotInit() {
        displayDeployTimestamp();

        drivetrain = Drivetrain.getInstance();

        subsystems.add(drivetrain);

        subsystems.forEach((ISubsystem subsystem) -> subsystem.initialize());

        // Pneumatics
        
    }

    @Override
    public void autonomousInit() {
        drivetrain.setCoastMode(false);
        subsystems.forEach((ISubsystem subsystem) -> subsystem.initialize());
    }

    @Override
    public void autonomousPeriodic() {
        subsystems.forEach((ISubsystem subsystem) -> subsystem.updateInputs());

        subsystems.forEach((ISubsystem subsystem) -> subsystem.updateOutputs());
        subsystems.forEach((ISubsystem subsystem) -> subsystem.updateDashboard());
    }

    @Override
    public void teleopInit() {
        drivetrain.setCoastMode(false);
        subsystems.forEach((ISubsystem subsystem) -> subsystem.initialize());
        SmartDashboard.putBoolean("button", false);
    }

    @Override
    public void teleopPeriodic() {
        controls.update();

        drivetrain.arcadeDrive(controls.driveSpeed(), controls.turnSpeed());
        boolean aButton = controls.getAButton();
        if (aButton) {
            pneumaticsEnabled = !pneumaticsEnabled;
        }
        SmartDashboard.putBoolean("button", pneumaticsEnabled);

        subsystems.forEach((ISubsystem subsystem) -> subsystem.updateOutputs());
        subsystems.forEach((ISubsystem subsystem) -> subsystem.updateDashboard());
    }

    @Override
    public void testInit() {
        testStartTime = Timer.getFPGATimestamp();
    }

    @Override
    public void testPeriodic() {
        subsystems.forEach((ISubsystem subsystem) -> subsystem.test(Timer.getFPGATimestamp() - testStartTime));
    }

    @Override
    public void disabledInit() {
        drivetrain.setCoastMode(true);
    }

    @Override
    public void disabledPeriodic() {
        subsystems.forEach((ISubsystem subsystem) -> subsystem.updateInputs());
        subsystems.forEach((ISubsystem subsystem) -> subsystem.updateDashboard());
    }

    @Override
    public void robotPeriodic() {
        /**
         * The method GetColor() returns a normalized color value from the sensor and can be
         * useful if outputting the color to an RGB LED or similar. To
         * read the raw color, use GetRawColor().
         * 
         * The color sensor works best when within a few inches from an object in
         * well lit conditions (the built in LED is a big help here!). The farther
         * an object is the more light from the surroundings will bleed into the 
         * measurements and make it difficult to accurately determine its color.
         */
        Color detectedColor = m_colorSensor.getColor();

        /**
         * The sensor returns a raw IR value of the infrared light detected.
         */
        double IR = m_colorSensor.getIR();

        /**
         * Open Smart Dashboard or Shuffleboard to see the color detected by the 
         * sensor.
         */
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("IR", IR);

        /**
         * In addition to RGB IR values, the color sensor can also return an 
         * infrared proximity value. The chip contains an IR led which will emit
         * IR pulses and measure the intensity of the return. When an object is 
         * close the value of the proximity will be large (max 2047 with default
         * settings) and will approach zero when the object is far away.
         * 
         * Proximity can be used to roughly approximate the distance of an object
         * or provide a threshold for when an object is close enough to provide
         * accurate color values.
         */
        int proximity = m_colorSensor.getProximity();

        SmartDashboard.putNumber("Proximity", proximity);
    }

    private void displayDeployTimestamp() {
        FileInputStream file;
        Properties properties = new Properties();

        NetworkTableEntry timestampDisplay = Shuffleboard.getTab(Dashboard.developmentTabName)
                .add("Deploy Timestamp", "none").withSize(2, 1).withPosition(Dashboard.deployTimestampPosition, 0)
                .getEntry();

        try {
            Path filePath = Filesystem.getDeployDirectory().toPath().resolve("deployTimestamp.properties");
            file = new FileInputStream(filePath.toFile());
            properties.load(file);
        } catch (Exception e) {
            return;
        }

        timestampDisplay.forceSetString(properties.getProperty("DEPLOY_TIMESTAMP"));
    }
}