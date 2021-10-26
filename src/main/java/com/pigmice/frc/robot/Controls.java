package com.pigmice.frc.robot;

import com.pigmice.frc.lib.inputs.Debouncer;
import com.pigmice.frc.lib.inputs.Toggle;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Controls {
    private interface DriverProfile {
        double driveSpeed();
        double turnSpeed();
        boolean getAButton();
    }

    private class EasySMX implements DriverProfile {
        private final XboxController joystick;

        public EasySMX(XboxController joystick) {
            this.joystick = joystick;
        }

        @Override
        public double driveSpeed() {
            double value = joystick.getY(Hand.kLeft);
            return Math.abs(value) < 0.2 ? 0 : value / 4;
        }

        @Override
        public double turnSpeed() {
            return joystick.getX(Hand.kRight) / 2;
        }

        @Override 
        public boolean getAButton() {
            return joystick.getAButtonPressed();
        }
    }

    private class XBox implements DriverProfile {
        private final XboxController joystick;

        public XBox(XboxController joystick) {
            this.joystick = joystick;
        }

        @Override
        public double driveSpeed() {
            double value = joystick.getY(Hand.kLeft);
            return Math.abs(value) < 0.2 ? 0 : value / 4;
        }

        @Override
        public double turnSpeed() {
            return joystick.getX(Hand.kRight) / 2;
        }

        @Override
        public boolean getAButton() {
            return joystick.getAButtonPressed();
        }
    }

    DriverProfile driver;

    public Controls() {
        XboxController driverJoystick = new XboxController(0);

        if (driverJoystick.getName().equals("EasySMX CONTROLLER")) {
            driver = new EasySMX(driverJoystick);
        } else if (driverJoystick.getName().equals("Controller (XBOX 360 For Windows)")) {
            driver = new XBox(driverJoystick);
        } else {
            driver = new XBox(driverJoystick);
        }
    }

    public void initialize() {
    }

    public void update() {
        
    }

    public double turnSpeed() {
        final double steering = driver.turnSpeed();
        return Math.pow(steering, 2) * Math.signum(steering);
    }

    public double driveSpeed() {
        return -driver.driveSpeed();
    }

    public boolean getAButton() {
        return driver.getAButton();
    }
}