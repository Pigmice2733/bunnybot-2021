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
        boolean getYButton();
        
        double getLeft();

        double getRight();
    }
    
    //private interface OperatorProfile {
    //    boolean intake();
    //}

    public static void bindTriggers(){
        XboxController controller = new XboxController(0);
        JoystickButton lbButton = new JoystickButton(controller, 4);
        final Intake intakeSubsystem = Intake.getInstance();
        lbButton.toggleWhenPressed(new InstantCommand(intakeSubsystem::toggle));
    }

    private class EasySMX implements DriverProfile/*,OperatorProfile*/ {
        private final XboxController joystick;

        public EasySMX(XboxController joystick) {
            this.joystick = joystick;
        }

        @Override
        public double driveSpeed() {
            return getLeft();
        }

        @Override
        public double turnSpeed() {
            return joystick.getX(Hand.kRight) / 2;
        }

        @Override 
        public boolean getAButton() {
            return joystick.getAButtonPressed();
        }

        @Override
        pubic boolean getYButton() {
            return joystick.getYbuttonPressed();
        }

        @Override
        public double getLeft() {
            return joystick.getY(Hand.kLeft);
        }

        @Override
        public double getRight() {
            return joystick.getY(Hand.kRight);
        }
    }

    private class XBox implements DriverProfile/*,OperatorProfile*/ {
        private final XboxController joystick;

        public XBox(XboxController joystick) {
            this.joystick = joystick;
        }

        @Override
        public double driveSpeed() {
            return getLeft();
        }

        @Override
        public double turnSpeed() {
            return joystick.getX(Hand.kRight) / 2;
        }

        @Override
        public boolean getAButton() {
            return joystick.getAButtonPressed();
        }

        @Override 
        public boolean getYbutton() {
            return joystick.getYButtonPressed();
        }

        @Override
        public double getLeft() {
            return joystick.getY(Hand.kLeft);
        }

        @Override
        public double getRight() {
            return joystick.getY(Hand.kRight);
        }
    }

    DriverProfile driver;
    //OperatorProfile operator;

    public Controls() {
        XboxController driverJoystick = new XboxController(0);

        if (driverJoystick.getName().equals("EasySMX CONTROLLER")) {
            driver = new EasySMX(driverJoystick);
        } else if (driverJoystick.getName().equals("Controller (XBOX 360 For Windows)")) {
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

    public void initialize() {
    }

    public void update() {
        
    }

    public double turnSpeed() {
        final double steering = driver.turnSpeed();
        return steering / 4;
    }

    public double driveSpeed() {
        double value = driver.driveSpeed();
        return Math.abs(value) < 0.2 ? 0 : -value / 4;
    }
    
    /*public boolean intake() {
        return operator.intake();
    } */
    
    public boolean getAButton() {
        return driver.getAButton();
    }

    public double leftSpeed() {
        return -driver.getLeft();
    }

    public double rightSpeed() {
        return -driver.getRight();
    }
    
    public boolean getYbutton() {
        return driver.getYbutton(); 
    }

}
