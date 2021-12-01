// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.commands.subroutines;

import com.pigmice.frc.robot.subsystems.impl.ColorSorter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ColorSort extends CommandBase {
  private final ColorSorter m_colorSorter;

  /** Creates a new Sort. */
  public ColorSort(ColorSorter subsystem) {
    m_colorSorter = subsystem;
    addRequirements(m_colorSorter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_colorSorter.ColorSort();
  }
}
