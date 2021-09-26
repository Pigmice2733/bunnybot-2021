package com.pigmice.frc.robot.subsystems;

public interface ISubsystem {
    public void initialize();

    public void updateDashboard();

    public void updateInputs();

    public void updateOutputs();

    public void test(double currentTestTime);
}
