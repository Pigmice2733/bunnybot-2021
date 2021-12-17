// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot.subsystems.impl;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.pigmice.frc.robot.subsystems.SystemConfig;
import com.pigmice.frc.robot.subsystems.SystemConfig.ColorSorterConfiguration;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3.RawColor;

public class ColorSorter extends SubsystemBase {
  private boolean enabled = false;
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  final TalonSRX colorSensorMotor = new TalonSRX(SystemConfig.ColorSorterConfiguration.colorSensorMotorPort);

  // Scales needed to trigger motor
  private static double generalSensitivity = 1;

  // all of these should be greater than 1
  private static double redScaleToBlue = 1.5;
  private static double redScaleToGreen = 1.0;

  private static double blueScaleToRed = 1.5;
  private static double blueScaleToGreen = 1.0;

  RawColor detectedColor;

  private static ColorSorter instance = null;

  public static ColorSorter getInstance() {
    if (instance == null) {
      instance = new ColorSorter();
    }

    return instance;
  }

  /** Creates a new ColorSorter. */
  public ColorSorter() {
    colorSensorMotor.setInverted(SystemConfig.ColorSorterConfiguration.redBallsWanted);
  }

  @Override
  public void periodic() {

  }

  public void colorSort() {

    detectedColor = colorSensor.getRawColor();

    // colorSensorMotor.set(ControlMode.PercentOutput, 0.5);

    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);

    // red detection loop
    if (detectedColor.red / detectedColor.blue > redScaleToBlue
        && detectedColor.red / detectedColor.green > redScaleToGreen) {
      colorSensorMotor.set(ControlMode.PercentOutput, SystemConfig.ColorSorterConfiguration.colorSensorMotorSpeed);
      SmartDashboard.putBoolean("Red Detected", true);
      SmartDashboard.putBoolean("Blue Detected", false);
    } else if (detectedColor.blue / detectedColor.red > blueScaleToRed
        && detectedColor.blue / detectedColor.green > blueScaleToGreen) {
      colorSensorMotor.set(ControlMode.PercentOutput, -SystemConfig.ColorSorterConfiguration.colorSensorMotorSpeed);
      SmartDashboard.putBoolean("Blue Detected", true);
      SmartDashboard.putBoolean("Red Detected", false);

    } else {
      colorSensorMotor.set(ControlMode.PercentOutput, 0);
      SmartDashboard.putBoolean("Red Detected", false);
      SmartDashboard.putBoolean("Blue Detected", false);
    }
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void toggle() {
    this.setEnabled(!this.enabled);
  }
}
