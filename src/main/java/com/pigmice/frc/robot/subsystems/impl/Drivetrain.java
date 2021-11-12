package com.pigmice.frc.robot.subsystems.impl;

import com.kauailabs.navx.frc.AHRS;
import com.pigmice.frc.lib.utils.Odometry;
import com.pigmice.frc.lib.utils.Odometry.Pose;
import com.pigmice.frc.lib.utils.Utils;
import com.pigmice.frc.lib.utils.Point;
import com.pigmice.frc.robot.Dashboard;
import com.pigmice.frc.robot.subsystems.SystemConfig;
import com.pigmice.frc.robot.subsystems.RobotSubsystem;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.pigmice.frc.robot.subsystems.SystemConfig.DrivetrainConfiguration;

public class Drivetrain extends RobotSubsystem {
    private final CANSparkMax leftDrive, rightDrive, rightFollower, leftFollower;
    private final CANEncoder leftEncoder, rightEncoder;

    private double leftDemand, rightDemand;
    private double leftPosition, rightPosition, heading;

    private Odometry odometry;

    private AHRS navx;
    private double navxTestAngle;
    private boolean navxTestPassed = false;
    private final NetworkTableEntry navxReport;

    private final NetworkTableEntry xDisplay, yDisplay, headingDisplay, leftEncoderDisplay, rightEncoderDisplay;

    private Point initialPosition = Point.origin();

    private static Drivetrain instance = null;

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }

        return instance;
    }

    private Drivetrain() {
        rightDrive = new CANSparkMax(DrivetrainConfiguration.frontRightMotorPort, MotorType.kBrushless);
        rightFollower = new CANSparkMax(DrivetrainConfiguration.backRightMotorPort, MotorType.kBrushless);
        leftDrive = new CANSparkMax(DrivetrainConfiguration.frontLeftMotorPort, MotorType.kBrushless);
        leftFollower = new CANSparkMax(DrivetrainConfiguration.backLeftMotorPort, MotorType.kBrushless);

        rightDrive.setInverted(true);
        leftFollower.follow(leftDrive);
        rightFollower.follow(rightDrive);

        navx = new AHRS(DrivetrainConfiguration.navxPort);

        ShuffleboardLayout testReportLayout = Shuffleboard.getTab(Dashboard.systemsTestTabName)
                .getLayout("Drivetrain", BuiltInLayouts.kList)
                .withSize(2, 1)
                .withPosition(Dashboard.drivetrainTestPosition, 0);

        navxReport = testReportLayout.add("NavX", false).getEntry();

        leftEncoder = leftDrive.getEncoder();
        rightEncoder = rightDrive.getEncoder();

        leftEncoder.setPositionConversionFactor(1.0 / DrivetrainConfiguration.rotationToDistanceConversion);
        rightEncoder.setPositionConversionFactor(1.0 / DrivetrainConfiguration.rotationToDistanceConversion);

        ShuffleboardLayout odometryLayout = Shuffleboard.getTab(Dashboard.developmentTabName)
                .getLayout("Odometry", BuiltInLayouts.kList).withSize(2, 5)
                .withPosition(Dashboard.drivetrainDisplayPosition, 0);

        xDisplay = odometryLayout.add("X", 0.0).getEntry();
        yDisplay = odometryLayout.add("Y", 0.0).getEntry();
        headingDisplay = odometryLayout.add("Heading", 0.0).getEntry();
        leftEncoderDisplay = odometryLayout.add("Left Encoder", 0).getEntry();
        rightEncoderDisplay = odometryLayout.add("Right Encoder", 0).getEntry();

        odometry = new Odometry(new Pose(0.0, 0.0, 0.0));
    }

    @Override
    public void initialize() {
        leftPosition = 0.0;
        rightPosition = 0.0;
        heading = 0.0;//0.5 * Math.PI;
        zeroHeading();

        leftEncoder.setPosition(0.0);
        rightEncoder.setPosition(0.0);

        odometry.set(new Pose(0.0, 0.0, heading), leftPosition, rightPosition);

        leftDemand = 0.0;
        rightDemand = 0.0;

        // navx.setAngleAdjustment(navx.getAngleAdjustment() - navx.getAngle() - 90.0);
    }

    @Override
    public void updateDashboard() {
        Pose currentPose = odometry.getPose();

        xDisplay.setNumber(currentPose.getX());
        yDisplay.setNumber(currentPose.getY());
        headingDisplay.setNumber(currentPose.getHeading());
        leftEncoderDisplay.setNumber(leftPosition);
        rightEncoderDisplay.setNumber(rightPosition);
    }

    public void updateHeading() {
        float headingDegrees = (navx.getYaw() + SystemConfig.DrivetrainConfiguration.navXRotationalOffsetDegrees) % 360;

        SmartDashboard.putNumber("Heading (Degrees)", headingDegrees);

        // calculates robot heading based on navx reading and offset
        heading = Math.toRadians(headingDegrees);
    }

    @Override
    public void updateInputs() {
        leftPosition = leftEncoder.getPosition();
        rightPosition = rightEncoder.getPosition();
        
        updateHeading();

        odometry.update(leftPosition, rightPosition, heading);
    }

    public double getHeading() {
        return heading;
    }

    public Pose getPose() {
        return odometry.getPose();
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        leftDemand = leftSpeed;
        rightDemand = rightSpeed;
    }

    public void arcadeDrive(double forwardSpeed, double turnSpeed) {
        leftDemand = forwardSpeed + turnSpeed;
        rightDemand = forwardSpeed - turnSpeed;
    }

    public void curvatureDrive(double forwardSpeed, double curvature) {
        double leftSpeed = forwardSpeed;
        double rightSpeed = forwardSpeed;

        if (!Utils.almostEquals(forwardSpeed, 0.0)) {
            leftSpeed = forwardSpeed * (1 + (curvature * 0.5 * DrivetrainConfiguration.wheelBase));
            rightSpeed = forwardSpeed * (1 - (curvature * 0.5 * DrivetrainConfiguration.wheelBase));
        }

        leftDemand = leftSpeed;
        rightDemand = rightSpeed;
    }

    public void swerveDrive(double forward, double strafe, double rotation_x) {

    }

    public void stop() {
        leftDemand = 0.0;
        rightDemand = 0.0;
    }

    @Override
    public void updateOutputs() {
        leftDrive.set(leftDemand);
        rightDrive.set(rightDemand);

        leftDemand = 0.0;
        rightDemand = 0.0;
    }

    @Override
    public void test(double time) {
        if(time < 0.1) {
            navxTestAngle = navx.getAngle();
            navxTestPassed = false;
        }

        if(!navxTestPassed) {
            navxTestPassed = navx.getAngle() != navxTestAngle;
        }

        navxReport.setBoolean(navxTestPassed);
    }

    public void setCoastMode(boolean coasting) {
        CANSparkMax.IdleMode newMode = coasting ? IdleMode.kCoast : IdleMode.kBrake;
        leftDrive.setIdleMode(newMode);
        rightDrive.setIdleMode(newMode);
        leftFollower.setIdleMode(newMode);
        rightFollower.setIdleMode(newMode);
    }

    public void resetPose() {
        this.odometry.set(new Pose(0, 0, getPose().getHeading()), 0.0, 0.0);
        initialPosition = new Point(this.getPose());
    }

    public double getDistanceFromStart() {
        Point currentPosition = new Point(this.getPose());
        return currentPosition.subtract(initialPosition).magnitude();
    }

    public void zeroHeading() {
        this.navx.zeroYaw();
        updateHeading();
    }

    public boolean isCalibrating() {
        return this.navx.isCalibrating();
    }
}