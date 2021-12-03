package com.pigmice.frc.robot;
import com.pigmice.frc.robot.subsystems.Drivetrain;

public class ProgrammingChallenge1{
    public static void execute(Drivetrain dt){
dt.tankDrive(10,10);
if(){
    dt.stop();
}
dt.tankDrive(-10,-10);
if(){
    dt.stop();
}
    }

    public static void repeat(Drivetrain dt){

    }
}

//https://pdocs.kauailabs.com/navx-mxp/software/roborio-libraries/java/ for information on the navX library
//https://pdocs.kauailabs.com/navx-mxp/wp-content/uploads/2019/02/navx-mxp_robotics_navigation_sensor_user_guide.pdf for examples