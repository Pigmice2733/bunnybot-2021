package com.pigmice.frc.robot.commands.routines;

import com.pigmice.frc.robot.subsystems.impl.Drivetrain;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import com.pigmice.frc.robot.commands.subroutines.DriveDistance;

public class LeaveLine extends SequentialCommandGroup {
    public LeaveLine(Drivetrain drivetrain) {
        addCommands(new DriveDistance(1.0, drivetrain));
    }
}