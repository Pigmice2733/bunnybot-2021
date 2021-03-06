// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;

import com.pigmice.frc.robot.subsystems.impl.ColorSorter;
import com.pigmice.frc.robot.subsystems.impl.Drivetrain;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command autonomousCommand;

    private RobotContainer robotContainer;

    private Drivetrain drivetrain;

    @Override
    public void robotInit() {
        displayDeployTimestamp();
        // Instantiate our RobotContainer. This will perform all our button bindings,
        // and put our
        // autonomous chooser on the dashboard.
        robotContainer = new RobotContainer();

        this.drivetrain = Drivetrain.getInstance();

        // m_colorSensor.configureColorSensor(ColorSensorResolution.kColorSensorRes13bit,
        // ColorSensorMeasurementRate.kColorRate50ms, GainFactor.kGain1x);
    }

    @Override
    public void autonomousInit() {
        drivetrain.setCoastMode(false);
        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.initialize());

        autonomousCommand = robotContainer.getAutonomousCommand();

        if (autonomousCommand != null)
            autonomousCommand.schedule();
    }

    @Override
    public void autonomousPeriodic() {
        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.updateInputs());

        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.updateOutputs());
        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.updateDashboard());
    }

    @Override
    public void teleopInit() {
        drivetrain.setCoastMode(false);
        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.initialize());

        if (autonomousCommand != null)
            autonomousCommand.cancel();
    }

    @Override
    public void teleopPeriodic() {
        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.updateOutputs());
        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.updateDashboard());
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.test(Timer.getFPGATimestamp() - testStartTime));
    }

    @Override
    public void disabledInit() {
        drivetrain.setCoastMode(true);
    }

    @Override
    public void disabledPeriodic() {
        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.updateInputs());
        // robotContainer.subsystems.forEach((RobotSubsystem subsystem) ->
        // subsystem.updateDashboard());
    }

    @Override
    public void robotPeriodic() {

        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled
        // commands, running already-scheduled commands, removing finished or
        // interrupted commands,
        // and running subsystem periodic() methods. This must be called from the
        // robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
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

    enum Alliance {
        RED, BLUE;
    }
}
