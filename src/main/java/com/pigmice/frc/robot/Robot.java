// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.pigmice.frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import com.pigmice.frc.robot.subsystems.Drivetrain;
import com.pigmice.frc.robot.motorconfig.CTRE;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  ControlScheme controls;
  Drivetrain drivetrain;

  ComponentLogger robotLogger = Logger.createComponent("Robot");

  @Override
  public void robotInit() {
    controls = new ControlScheme();

    AHRS navx = new AHRS(SPI.Port.kMXP);

    configureDrivetrain(3, 1, 4, 2, navx);

    URI driverStation;
    try {
      driverStation = new URI("ws://10.27.33.5:8181/log");
    } catch (URISyntaxException e) {
      throw new RuntimeException("Misformatted driver station URI");
    }

    Logger.configure(driverStation);
    Logger.start();

    robotLogger.info("Robot code started!");
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    robotLogger.info("Teleop init");

    superStructure.initialize(superStructer.getPose());
  }

  @Override
  public void teleopPeriodic() {
    gamePeriodic();
  }

  private void gamePeriodic() {
    drivetrain.arcadeDrive(controls.drive(), controls.steer());
  }

  private void configureDrivetrain(int frontLeft, int frontRight, int backLeft, int backRight, AHRS navx) {
    TalonSRX leftDrive = new TalonSRX(frontLeft), rightDrive = new TalonSRX(frontRight);
    CTRE.configureDriveMotor(leftDrive);
    CTRE.configureDriveMotor(rightDrive);
    
    rightDrive.setInverted(true);

    VictorSPX leftFollower = new VictorSPX(backLeft), rightFollower = new VictorSPX(backRight);

    CTRE.configureFollowerMotor(leftFollower, leftDrive);
    CTRE.configureFollowerMotor(rightFollower, rightDrive);

    drivetrain = new Drivetrain(leftDrive, rightDrive, navx, 2);
  }
}
