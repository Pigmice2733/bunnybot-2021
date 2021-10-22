package com.pigmice.frc.robot.autonomous;

import java.util.Arrays;

import com.pigmice.frc.robot.autonomous.tasks.Drive;
import com.pigmice.frc.robot.subsystems.Drivetrain;

public class LeaveLine extends Autonomous {
    public LeaveLine(Drivetrain drivetrain) {
        this.subroutines = Arrays.asList(new Drive(drivetrain, 1.0));
    }

    public void initialize() {
        super.initialize();
    }

    public String name() {
        return "Leave line";
    }
}