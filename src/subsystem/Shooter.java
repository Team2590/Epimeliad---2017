package subsystem;

import org.usfirst.frc.team2590.controllers.PositionVelocityController;
import org.usfirst.frc.team2590.controllers.VelocityController;
import org.usfirst.frc.team2590.looper.Loop;
import org.usfirst.frc.team2590.robot.RobotMap;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter implements RobotMap{
	
	//system states
	private enum shooterState {
		STOP , SHOOT_READY , SHOOT_NOW , SPEED_UP
	};
	private shooterState shooter = shooterState.STOP;
	
	//basic variables
	private double stp;
	private final double TOLERANCE = 50; //the controller should be tuned more so this can be < 10
	
	//sensors motors and controllers
	private Victor feederVictor;
	private Victor shooterVictor;
	private Encoder shooterEncoder;
	private PositionVelocityController shooterController;
	
	public Shooter() {
		//variables
		stp = 0;
		
		//controller
		//0.0042 0.45 1.0E-4
		//0.06 0.2 5E-4
		//0.0045 0.45 1.0E-4
		//0,25 0.5 0.014
		shooterController = new PositionVelocityController(0.016, 0.04, 0.014 , 16 );
		
		//motors
		feederVictor = new Victor(feederPWM);
		shooterVictor = new Victor(shooterPWM);
		shooterVictor.setInverted(true);
		
		//encoder and settings
		shooterEncoder = new Encoder(shooterEncA , shooterEncB , false , EncodingType.k1X);
		shooterEncoder.setPIDSourceType(PIDSourceType.kRate);
		
		//360 ticks in 1 revolution , dots are for float point math
		shooterEncoder.setDistancePerPulse(1./360.);

	}
	
	private Loop loop_ = new Loop() {

		@Override
		public void onStart() {
			
		}

		@Override
		public void loop() {
			
			switch(shooter) {
			  case STOP : 
				feederVictor.set(0);
			    shooterVictor.set(0);
				break;
			  case SHOOT_READY : //only shoots when the wheel hits speed
			    feederVictor.set( ( Math.abs( (shooterEncoder.getRate()*60) - stp) <= TOLERANCE) ? 1 : 0);
				//fall through
			  case SPEED_UP : //spins up to setpoint speed
				//shooterVictor.set(shooterController.calculate(shooterEncoder.getRate()*60));
				  shooterVictor.set(0.5);
				break;
			  case SHOOT_NOW : //spins up to speed while shooting holds the setpoint rpm
				//shooterVictor.set(shooterController.calculate(shooterEncoder.getRate()*60));				  
				 shooterVictor.set(0.5);
				feederVictor.set(1);
				break;
			  default : //nothing here , just adhering to googles style guide
			}
			SmartDashboard.putNumber("Encoder", shooterEncoder.getRate()*60);
		
		}

		@Override
		public void onEnd() {
			shooter = shooterState.STOP;
		}
		
	};
	
	public Loop getShooterLoop() {
		return loop_;
	}
	
	
	public void setSetpoint(double setpoint) {
		stp = setpoint;
		shooterController.setSetpoint(setpoint);
	}
	
	/**
	 * Gets the shooter wheels up to speed
	 * does not move the feeder
	 */
	public void getReady() {
		shooter = shooterState.SPEED_UP;
	}
	
	/**
	 * Spins the feeder roller immediatly and
	 * uses the controller to maintain setpoint
	 */
	public void takeShot() {
		shooter = shooterState.SHOOT_NOW;
	}
	
	/**
	 * Stops the wheels and feeder
	 */
	public void stopShot() {
		shooter = shooterState.STOP;
	}

	/**
	 * Only starts the feeder when the shooter is 
	 * within a certain tolerance of a setpoint
	 */
	public void patienceIsKey() {
		shooter = shooterState.SHOOT_READY;
	}
	
	public double getEncoderVal() {
		return shooterEncoder.getRate()*60;
	}
	
}
