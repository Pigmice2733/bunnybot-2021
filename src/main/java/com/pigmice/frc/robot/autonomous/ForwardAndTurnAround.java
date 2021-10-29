package com.pigmice.frc.robot.autonomous;

import java.util.Arrays;

import com.pigmice.frc.robot.autonomous.tasks.Drive;
import com.pigmice.frc.robot.autonomous.tasks.Turn;
import com.pigmice.frc.robot.subsystems.Drivetrain;

public class ForwardAndTurnAround extends Autonomous {
    public ForwardAndTurnAround(Drivetrain drivetrain) {
        this.subroutines = Arrays.asList(
            new Drive(drivetrain, 2.0),
            //new Turn(drivetrain, Math.PI),
            new Drive(drivetrain, 2.0)
        );
    }

    public void initialize() {
        super.initialize();
    }

    public String name() {
        return "Forward and turn around";
    }
}
