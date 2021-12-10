package com.pigmice.frc.robot;

import com.pigmice.frc.lib.inputs.Debouncer;
import com.pigmice.frc.lib.inputs.Toggle;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Controls {
    private interface DriverProfile {
        double driveSpeed();
        double reverseSpeed();
        double turnSpeed();
        double turnSlow();
        /*boolean getAButton();
        
        double getLeft();

        double getRight();
        */
    }
    
    //private interface OperatorProfile {
    //    boolean intake();
    //}


    public class XBox implements DriverProfile /*,OperatorProfile*/ {
        private final XboxController joystick;

        public XBox(XboxController joystick) {
            this.joystick = joystick;
        }

        @Override
        public double driveSpeed() {
            return joystick.getTriggerAxis(Hand.kRight);
        }

        @Override
        public double reverseSpeed() {
            return joystick.getTriggerAxis(Hand.kLeft);
        }

        @Override
        public double turnSpeed() {
            return joystick.getX(Hand.kLeft) / 2;
        }

        @Override
        public double turnSlow() {
            return joystick.getX(Hand.kRight) / 8;
        }
    }

    DriverProfile driver;
    //OperatorProfile operator;

    public Controls() {
        XboxController driverJoystick = new XboxController(0);
        XBox driver = new XBox(driverJoystick);    
        
        /*if (driverJoystick.getName().equals("EasySMX CONTROLLER")) {
           // driver = new EasySMX(driverJoystick);
        if (driverJoystick.getName().equals("Controller (XBOX 360 For Windows)")) {
            driver = new XBox(driverJoystick);
        } else {
            driver = new XBox(driverJoystick);
        }
        /*  if (operatorJoystick.getName().equals("EasySMX CONTROLLER")) {
            operator = new EasySMX(operatorJoystick);
        } else if (operatorJoystick.getName().equals("Controller (XBOX 360 For Windows)")) {
            operator = new XBox(operatorJoystick);
        } else {
            operator = new XBox(operatorJoystick);
        } */
    }

    public double turnSpeed() {
        double fast = driver.turnSpeed();
        double slow = driver.turnSlow();
        double value = 0d;
        if (Math.abs(slow) >= 0.2) {
            value = slow / -4;
        } else if (Math.abs(fast) >= 0.2) {
            value = fast / -4;
        }
        return value;
    }

    public double driveSpeed() {
        double valueF = driver.driveSpeed();
        double valueR = driver.reverseSpeed();
        double value = 0d;
        if (Math.abs(valueF) >= 0.2) {
            value += valueF / -4;
        }
        if (Math.abs(valueR) >= 0.2) {
            value += valueR / 4;
        }
        return value;
    }
    
    /*public boolean intake() {
        return operator.intake();
    } */
}