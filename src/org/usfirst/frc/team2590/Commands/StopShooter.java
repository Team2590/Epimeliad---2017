package org.usfirst.frc.team2590.Commands;

import org.usfirst.frc.team2590.robot.Robot;

public class StopShooter extends NemesisCommand {

  @Override
  public void run() {
    Robot.shooter.stopShooter();
  }

  @Override
  public boolean done() {
    return false;
  }

}