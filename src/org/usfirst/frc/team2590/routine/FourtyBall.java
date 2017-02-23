package org.usfirst.frc.team2590.routine;

import org.usfirst.frc.team2590.Commands.RunPath;
import org.usfirst.frc.team2590.navigation.PathSegment;
import org.usfirst.frc.team2590.navigation.Point;
import org.usfirst.frc.team2590.robot.Robot;

public class FourtyBall extends AutoRoutine {

  //points
  private Point hitHopper;
  private Point startPoint;
  private Point catchBalls;
  
  //segments
  private PathSegment openHopper;
  private PathSegment catchHopper;
  
  //paths
  private RunPath getHopperBalls;
  
  public FourtyBall() {
    //points
    hitHopper = new Point(8,1,0);
    startPoint = new Point(0,0,0);
    catchBalls = new Point(5,1,0);
    
    //segments
    openHopper = new PathSegment(startPoint, hitHopper);
    catchHopper = new PathSegment(hitHopper , catchBalls);
    
    //path
    getHopperBalls = new RunPath(openHopper, catchHopper);
  }
  
  @Override
  public void run() {
    //get ready for the path
    Robot.driveT.resetSensors();
    Robot.driveT.shiftHigh();
    
    //run the path
    getHopperBalls.startChange();
    getHopperBalls.flip();
    getHopperBalls.run();
    waitUntilDone(3, getHopperBalls.done());
    
    //get ready for the return
    Robot.shooter.setSetpoint(6600);
    Robot.shooter.revShooter();
    
  }

  @Override
  public void end() {
    
  }

 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /* 
o        o      o    o   o   o   oo  oo  oo  oo}
  o     o  oo  ooo  oo  ooo ooo  ooo ooo ooo oooooo}
                 oo  oo   oo  oo  ooo ooo ooo oooo ooooo}
                       o    o   o   oo  oo  oo  oooo oooooo}
                                      o   o   o   ooo  oo ooooo}
                                                           o ooo}
                              _________            \|/         ++
                  ___________ |_______|______/-\___|-|_^_/-\___||_n
           n_____/    OOO    \_ |[][] |----------------------|----|\
           |          (_)      ||     |______________________|_|| ||P
           |         MEMES     | -----/====+______|| _---====--||- /
          q|===================|_|=_/o====+|=====[__]o====+|==[__]-p\
_______________(_)(_)______(_)(_)_____\__/_\__/_\__/__\__/_\__/_(_)____\
           */

}