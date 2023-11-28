// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.Utils;

public class Robot extends TimedRobot {


  private Utils utils;
  ShuffleboardTab tab = Shuffleboard.getTab(Constants.DRIVER_READOUT_TAB_NAME);


  @Override
  public void robotInit() {

    utils = new Utils();
    tab.addString("Validation of Target" , () -> getOut());
  }

  @Override
  public void teleopPeriodic() {}

  public String getOut() {
    System.out.println("called");
    if (utils.validateTarget()){
      System.out.println("called");
      return "Found Target: " + (Constants.IsTeamRed? "Red" : "Blue");
    }
    return "No Target Found.";
  }

  @Override
  public void robotPeriodic() {
      CommandScheduler.getInstance().run();
  }
}
