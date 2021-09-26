package com.pigmice.frc.robot;

import com.pigmice.frc.lib.inputs.Debouncer;
import com.pigmice.frc.lib.inputs.Toggle;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Controls {
    private interface DriverProfile {
        double driveSpeed();
        double turnSpeed();
    }

    private class EasySMX implements DriverProfile {
        private final XboxController joystick;

        public EasySMX(XboxController joystick) {
            this.joystick = joystick;
        }

        @Override
        public double driveSpeed() {
            return joystick.getY(Hand.kLeft);
        }

        @Override
        public double turnSpeed() {
            return joystick.getX(Hand.kRight);
        }
    }

    private class XBox implements DriverProfile {
        private final XboxController joystick;

        public XBox(XboxController joystick) {
            this.joystick = joystick;
        }

        @Override
        public double driveSpeed() {
            return joystick.getY(Hand.kLeft);
        }

        @Override
        public double turnSpeed() {
            return joystick.getX(Hand.kRight);
        }
    }

    DriverProfile driver;

    Debouncer debouncer;
    Toggle hoodToggle;
    Debouncer down;
    Debouncer up;

    public Controls() {
        XboxController driverJoystick = new XboxController(0);

        if (driverJoystick.getName().equals("EasySMX CONTROLLER")) {
            driver = new EasySMX(driverJoystick);
        } else if (driverJoystick.getName().equals("Controller (XBOX 360 For Windows)")) {
            driver = new XBox(driverJoystick);
        } else {
            driver = new XBox(driverJoystick);
        }
        hoodToggle = new Toggle(debouncer);
    }

    public void initialize() {
    }

    public void update() {
        debouncer.update();
        hoodToggle.update();
        up.update();
        down.update();
    }

    public double turnSpeed() {
        final double steering = driver.turnSpeed();
        return Math.pow(steering, 2) * Math.signum(steering);
    }

    public double driveSpeed() {
        return -driver.driveSpeed();
    }
    
    public boolean speedUp() {
        return up.get();
    }

    public boolean speedDown() {
        return down.get();
    }
}