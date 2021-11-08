// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

//Autonomous imports
import com.pigmice.frc.robot.autonomous.Autonomous;
import com.pigmice.frc.robot.autonomous.ForwardAndTurnAround;
import com.pigmice.frc.robot.autonomous.LeaveLine;

//Subsystem imports
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.subsystems.ISubsystem;

import static edu.wpi.first.wpilibj.XboxController.Button;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final Drivetrain drivetrain = Drivetrain.getInstance();
  
  final List<ISubsystem> subsystems = new ArrayList<>();
  
  subsystems.add(drivetrain);

  subsystems.forEach((ISubsystem subsystem) -> subsystem.initialize());

  // Initializes autoRoutines for use in {@link Autonomous}
  private List<Autonomous> autoRoutines = new ArrayList<>();
  Autonomous autonomous;
  
  // The autonomous routines
  autoRoutines.addAll(Arrays.asList(
    new LeaveLine(drivetrain), 
    new ForwardAndTurnAround(drivetrain)));

  // Initializing both controllers
  private final Controls controls = new Controls();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    // Set the default drive command to split-stick arcade drive
    drivetrain.setDefaultCommand(
      // A split-stick arcade command, with forward/backward controlled by the left
      // hand, and turning controlled by the right.
        new RunCommand(
            () ->
              drivetrain.arcadeDrive(
                  controls.driveSpeed(), controls.turnSpeed()),
            drivetrain;

      // Add commands to the autonomous command chooser
      Autonomous.setOptions(autoRoutines);

      // Gets current selected command on SmartDashboard
      autonomous = Autonomous.getSelected();

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
}
