// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

//Autonomous imports
import com.pigmice.frc.robot.commands.routines.ForwardAndTurnAround;
import com.pigmice.frc.robot.commands.routines.LeaveLine;
import com.pigmice.frc.robot.commands.subroutines.ArcadeDrive;
//Subsystem imports
import com.pigmice.frc.robot.subsystems.impl.Drivetrain;
import com.pigmice.frc.robot.subsystems.RobotSubsystem;

import static edu.wpi.first.wpilibj.XboxController.Button;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private Drivetrain drivetrain;
  
  final List<RobotSubsystem> subsystems = new ArrayList<>();

  private final Command leaveline = new LeaveLine(drivetrain);
  private final Command fowardAndTurnAround = new ForwardAndTurnAround(drivetrain);
  
  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  
  // Initializing both controllers
  final Controls controls = new Controls();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    this.drivetrain = Drivetrain.getInstance();
    subsystems.add(drivetrain);

    subsystems.forEach((RobotSubsystem subsystem) -> subsystem.initialize());

    // Configure default commands
    // Set the default drive command to split-stick arcade drive
    drivetrain.setDefaultCommand(
        new ArcadeDrive(
          drivetrain, controls::driveSpeed, controls::turnSpeed
        )
    );

    // Add commands to the autonomous command chooser
    m_chooser.setDefaultOption("Leave Line", leaveline);
    m_chooser.addOption("Foward And Turn Around", fowardAndTurnAround);

    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
}

