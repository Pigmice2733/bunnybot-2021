package com.pigmice.frc.robot.autonomous;

import java.util.ArrayList;
import java.util.List;

import com.pigmice.frc.robot.Dashboard;
import com.pigmice.frc.robot.autonomous.tasks.ITask;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Autonomous {
    protected List<ITask> subroutines = new ArrayList<>();
    private int state = -1;

    public void initialize() {
        state = -1;
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
        if(subroutines.size() == 0) {
            return;
        }

        // Initialize first state
        if (state < 0) {
            state = 0;
            subroutines.get(state).initialize();
        }

        // Update state, advance to next state if done
        if (state < subroutines.size()) {
            boolean done = subroutines.get(state).update();
            if (done) {
                state++;
                if (state < subroutines.size()) {
                    subroutines.get(state).initialize();
                } else {
                    System.out.println("Auto done");
                }
            }
        }
    };
}