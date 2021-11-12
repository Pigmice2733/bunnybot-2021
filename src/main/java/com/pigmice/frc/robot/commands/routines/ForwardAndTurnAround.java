package com.pigmice.frc.robot.commands.routines;

import com.pigmice.frc.robot.subsystems.impl.Drivetrain;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import com.pigmice.frc.robot.commands.subroutines.DriveDistance;
import com.pigmice.frc.robot.commands.subroutines.TurnToAngle;

public class ForwardAndTurnAround extends SequentialCommandGroup {
    public ForwardAndTurnAround(Drivetrain drivetrain) {
        addCommands(
            new DriveDistance(2.0, drivetrain),
            
            new TurnToAngle(180, true, drivetrain),
            
            new DriveDistance(2.0, drivetrain)
        );
    }
}