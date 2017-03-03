package org.usfirst.frc.team2590.navigation;

public class DriveAtAngleController {

  double kF;
  double kT;
  double kI;
  double A;
  double B;
  private double lastOut;
  private double lastError;
  double error;
  double maxAcc;
  
  double velocity;
  boolean flipped;
  double angleStp;
  double distanceStp;

  public DriveAtAngleController(double maxAcc , double kF , double kT , double kI) {

    
    error = 0;
    lastOut = 0;
    lastError = 0;
    velocity = 0;
    angleStp = 0;
    this.kF = kF;
    this.kT = kT;
    this.kI = kI;
    
    distanceStp = 0;
    flipped = false;
    this.maxAcc = maxAcc;
  }

  public void setSetpoint(double setPoint , double angle ) {
    error = setPoint;
    angleStp = angle;
    distanceStp = setPoint;
  }
  
  public void changeF(double newF) {
    this.kF = newF;
  }
  
  public double getSetpoint() {
    return distanceStp;
  }
  
  public void reset() {
    lastOut = 0;
    lastError = 0;
  }

  public double calculate(double processVar , double gyroA , boolean right , double dt) {
    //calculate error
    error = distanceStp-processVar;
    
    //velocity calculations
    velocity = Math.sqrt(Math.abs(2*maxAcc*error));
    flipped = error < 0;
    
    //checkk if its inverted
    velocity *= (flipped ? -1 : 1);

    A = kT + ((kI*dt)/2);
    B = ((kI*dt)/2) - kT;
    
    double turnOutPut = lastOut + (A*(angleStp-gyroA)) + (B*lastError);
    
    //if it is flip the output
    double out = ((velocity*kF)) + (turnOutPut * (right?1:-1));
    lastError = (angleStp-gyroA);


    //do this other wise it will continue driving once youve hit drive setpoint because
    //it wants to get to the angle setpoint
    if(Math.abs(error) > 0.1) {
      lastOut = turnOutPut;
      return out;
    }
   
    return 0.0;
  }

  public boolean isDone() {
    return Math.abs(error) < 0.1;
  }
}
