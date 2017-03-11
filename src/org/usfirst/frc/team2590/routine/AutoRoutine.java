package org.usfirst.frc.team2590.routine;

import edu.wpi.first.wpilibj.Timer;

public abstract class AutoRoutine {

  public abstract void run();
  public abstract void end();

  public static void waitUntilDone(double timeOut , Checkable condition) {
    double start = Timer.getFPGATimestamp();
    while(Timer.getFPGATimestamp() - start < timeOut) {
      if(condition.checker()) {
        return;
      }
      try {
        Thread.sleep(10);
      } catch (Exception e) {}
    }
  }

}
