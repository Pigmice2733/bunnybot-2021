package com.pigmice.frc.robot.autonomous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pigmice.frc.robot.Dashboard;
import com.pigmice.frc.robot.autonomous.tasks.ITask;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
    protected List<ITask> subroutines = new ArrayList<>();
    private int taskIndex = -1;
    private ITask currentTask;
    private boolean completed = false;

    public void initialize() {
        taskIndex = -1;
        currentTask = null;
        completed = false;
    }

    private static SendableChooser<Autonomous> chooser = new SendableChooser<>();

    public static void setOptions(List<Autonomous> routines) {
        chooser = new SendableChooser<>();

        for (Autonomous autonomous : routines) {
            chooser.addOption(autonomous.name(), autonomous);
        }

        chooser.addOption("None", new Autonomous());

        Shuffleboard.getTab(Dashboard.driverTabName).add("Auto Selector", chooser).withWidget(BuiltInWidgets.kComboBoxChooser);
    }

    public static Autonomous getSelected() {
        Autonomous selected = chooser.getSelected();

        if(selected == null) {
            return new Autonomous();
        }

        System.out.println(selected.name());

        return selected;
    }

    public String name() {
        return "Empty abstract auto";
    };

    public void update() {
        // Initialize first state
        if (!completed && currentTask == null) {
            if(subroutines.size() == 0) {
                return;
            }
            
            startNextTask();
        }

        // Update state, advance to next state if done
        boolean done = currentTask.update();
        if (done) {
            startNextTask();
        }
    }

    private boolean startNextTask() {
        taskIndex++;
        if (taskIndex < subroutines.size()) {
            currentTask = subroutines.get(taskIndex);

            SmartDashboard.putString("Current Task", currentTask.getClass().getSimpleName());
            currentTask.initialize();
        } else {
            completed = true;
            currentTask = null;
            SmartDashboard.putString("Current Task", currentTask.getClass().getSimpleName() + " IS DONE");
            return false;
        }

        return true;
    }
}