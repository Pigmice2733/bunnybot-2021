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

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

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

    @Override
    public void robotInit() {
        displayDeployTimestamp();

        drivetrain = Drivetrain.getInstance();

        subsystems.add(drivetrain);

        subsystems.forEach((ISubsystem subsystem) -> subsystem.initialize());
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
    }

    @Override
    public void teleopPeriodic() {
      controls.update();

      drivetrain.arcadeDrive(controls.driveSpeed(), controls.turnSpeed());

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
