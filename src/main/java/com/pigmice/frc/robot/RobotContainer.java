// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Autonomous imports
import com.pigmice.frc.robot.commands.routines.ForwardAndTurnAround;
import com.pigmice.frc.robot.commands.routines.LeaveLine;
import com.pigmice.frc.robot.commands.subroutines.ArcadeDrive;
import com.pigmice.frc.robot.commands.subroutines.TurnToAngle;
//Subsystem imports
import com.pigmice.frc.robot.subsystems.impl.ColorSorter;
import com.pigmice.frc.robot.subsystems.impl.Dispenser;
import com.pigmice.frc.robot.subsystems.impl.Drivetrain;
import com.pigmice.frc.robot.subsystems.impl.Intake;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private Drivetrain drivetrain;
  private Intake intake;
  private ColorSorter colorSorter;
  private Dispenser dispenser;

  final List<SubsystemBase> subsystems = new ArrayList<>();

  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  // Initializing both controllers
  final Controls controls;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    XboxController driver = new XboxController(0);
    XboxController operator = new XboxController(1);

    controls = new Controls(driver, operator);

    this.drivetrain = Drivetrain.getInstance();
    this.intake = Intake.getInstance();
    this.colorSorter = ColorSorter.getInstance();
    subsystems.addAll(Arrays.asList(drivetrain, intake));

    this.dispenser = Dispenser.getInstance();
    subsystems.add(dispenser);

    Command leaveline = new LeaveLine(drivetrain);
    Command fowardAndTurnAround = new ForwardAndTurnAround(drivetrain);

    // subsystems.forEach((RobotSubsystem subsystem) -> subsystem.initialize());

    // Configure default commands
    // Set the default drive command to split-stick arcade drive
    drivetrain.setDefaultCommand(new ArcadeDrive(drivetrain, controls::driveSpeed, controls::turnSpeed));

    try {
      configureButtonBindings(driver, operator);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Add commands to the autonomous command chooser
    m_chooser.setDefaultOption("Leave Line", leaveline);
    m_chooser.addOption("Foward And Turn Around", fowardAndTurnAround);

    // Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);
  }

  private void configureButtonBindings(XboxController driver, XboxController operator) {
    // OPERATOR
    // toggle intake and color sorter [X Button]
    new JoystickButton(operator, Button.kX.value)
      .whenPressed(new InstantCommand(() -> {
        this.intake.toggle();
        this.colorSorter.toggle();
    }));
    // open dispenser [A Button and Right Bumper]
    new JoystickButton(operator, Button.kA.value)
      .and(new JoystickButton(operator, Button.kBumperRight.value))
      .whenActive(new InstantCommand(dispenser::open));
    // close dispenser [B Button]
    new JoystickButton(operator, Button.kB.value)
      .whenPressed(new InstantCommand(dispenser::close));
    
    // DRIVER
    // turbo mode [B Button]
    new JoystickButton(driver, Button.kB.value)
      .whenPressed(new InstantCommand(drivetrain::boost))
      .whenReleased(new InstantCommand(drivetrain::stopBoost));
    // slow mode [A Button]
    new JoystickButton(driver, Button.kA.value)
      .whenPressed(new InstantCommand(drivetrain::slow))
      .whenReleased(new InstantCommand(drivetrain::stopBoost));
    // turn 90º right [Right Bumper]
    new JoystickButton(driver, Button.kBumperRight.value)
      .whenPressed(new TurnToAngle(Math.PI / 2, false, this.drivetrain));
    // turn 90º left [Left Bumper]
    new JoystickButton(driver, Button.kBumperLeft.value)
      .whenPressed(new TurnToAngle(Math.PI / -2, false, this.drivetrain));
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