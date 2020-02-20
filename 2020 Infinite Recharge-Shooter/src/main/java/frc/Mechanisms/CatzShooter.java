package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;



public class CatzShooter
{
    public WPI_TalonSRX shtrMtrCtrlA;
    public WPI_TalonSRX shtrMtrCtrlB;

    private final int SHTR_MC_ID_A = 1; //TBD 40
    private final int SHTR_MC_ID_B = 2; //41
    
    final double COUNTS_PER_REVOLUTION      = 4096.0;
    final double SEC_TO_MIN                 = 60.0;
    final double ENCODER_SAMPLE_RATE_MSEC   = 100.0;
    final double ENCODER_SAMPLE_PERIOD_MSEC = (1.0 / ENCODER_SAMPLE_RATE_MSEC);
    final double MSEC_TO_SEC                = 1000.0;
    final double FLYWHEEL_GEAR_REDUCTION    = 3.0;

    final double CONV_QUAD_VELOCITY_TO_RPM = ( ((ENCODER_SAMPLE_PERIOD_MSEC * MSEC_TO_SEC * SEC_TO_MIN) / COUNTS_PER_REVOLUTION));

    public static final int SHOOT_IDLE_MODE        = 0;
    public static final int SHOOT_FROM_TARGET_ZONE = 1;
    public static final int SHOOT_FROM_START_LINE  = 2;

    
    final double SHOOTER_TARGET_VEL_TARGET_ZONE_RPM = 4000.0; //TBD
    final double SHOOTER_TARGET_VEL_START_LINE_RPM  = 5000.0; //RPM
 
    final double SHOOTER_BANG_BANG_MAX_RPM_OFFSET = 5.0; 
    final double SHOOTER_BANG_BANG_MIN_RPM_OFFSET = 5.0;
  
    final double SHOOTER_BANG_BANG_START_LINE_MAX_POWER = 0.95;
    final double SHOOTER_BANG_BANG_START_LINE_MIN_POWER = 0.45;

    final double SHOOTER_BANG_BANG_TARGET_ZONE_MAX_POWER = 0.80;
    final double SHOOTER_BANG_BANG_TARGET_ZONE_MIN_POWER = 0.45;

    final double SHOOTER_OFF_POWER  = 0.0;
    final double SHOOTER_IDLE_POWER = 0.0;


    public double targetRPM    = 0.0;
    public double shooterPower = 0.0;

    private boolean readyToFire =     false;  //TBD MOVE TO SHOOTER

    public boolean logTestData = false;

    
    

    public CatzShooter()
    {
        shtrMtrCtrlA = new WPI_TalonSRX(SHTR_MC_ID_A);
        shtrMtrCtrlB = new WPI_TalonSRX(SHTR_MC_ID_B);

        //Reset configuration
        shtrMtrCtrlA.configFactoryDefault();
        shtrMtrCtrlB.configFactoryDefault();

        //Set MC B to follow MC A
       shtrMtrCtrlB.follow(shtrMtrCtrlA);

        //Configure feedback device for PID loop
        shtrMtrCtrlA.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);

        shtrMtrCtrlA.getSensorCollection().setQuadraturePosition(0,100);

        //Set MC's to coast mode
        shtrMtrCtrlA.setNeutralMode(NeutralMode.Coast);
        shtrMtrCtrlB.setNeutralMode(NeutralMode.Coast);

        //Configure PID constants
        shtrMtrCtrlA.config_kP(0, 0.005);
        shtrMtrCtrlA.config_kI(0, 0.0);
        shtrMtrCtrlA.config_kD(0, 0.0);
        shtrMtrCtrlA.config_kF(0, 0.008); //shtrMtrCtrlA.config_kF(0, 0.008);
        shtrMtrCtrlA.config_IntegralZone(0, 0);
    }

    public void setTargetVelocity(double targetVelocity)  //TBD
    {
        shtrMtrCtrlA.set(ControlMode.Velocity, targetVelocity);
    }




    public void shooterFlyWheelDisable()
    {
        shtrMtrCtrlA.set(0);
    }



    public double getFlywheelShaftPosition()
    {
        return shtrMtrCtrlA.getSensorCollection().getQuadraturePosition();
    }

    public double getFlywheelShaftVelocity()
    {
        return (Math.abs((double) shtrMtrCtrlA.getSensorCollection().getQuadratureVelocity()) * CONV_QUAD_VELOCITY_TO_RPM); 
    }


    

    public void setShooterVelocity(int shootingPosition)
    {
        boolean validLocation = true;
        double flywheelShaftVelocity2 = -1.0;
        double minRPM = 0.0;
        double maxRPM = 0.0;
        double minPower = 0.0;
        double maxPower = 0.0;
        double shootTime = 0.0;

//        System.out.println("***** SET SHOOTER VELOCITY *****");
        
        flywheelShaftVelocity2 = getFlywheelShaftVelocity();

        switch (shootingPosition)
        {
            case SHOOT_FROM_TARGET_ZONE:
            
                targetRPM= SHOOTER_TARGET_VEL_TARGET_ZONE_RPM;
                minRPM = targetRPM - SHOOTER_BANG_BANG_MIN_RPM_OFFSET;
                maxRPM = targetRPM + SHOOTER_BANG_BANG_MAX_RPM_OFFSET;
                minPower = SHOOTER_BANG_BANG_TARGET_ZONE_MIN_POWER;
                maxPower = SHOOTER_BANG_BANG_TARGET_ZONE_MAX_POWER;
               // System.out.println("T");
                if(logTestData == true){
                    minPower = maxPower;
                    
                }
                    shootTime = Robot.dataCollectionTimer.get();
                break;

            case SHOOT_FROM_START_LINE:

                targetRPM = SHOOTER_TARGET_VEL_START_LINE_RPM ;
                minRPM    = targetRPM - SHOOTER_BANG_BANG_MIN_RPM_OFFSET;
                maxRPM    = targetRPM + SHOOTER_BANG_BANG_MAX_RPM_OFFSET;
                minPower  = SHOOTER_BANG_BANG_START_LINE_MIN_POWER;
                maxPower  = SHOOTER_BANG_BANG_START_LINE_MAX_POWER;

                if(logTestData == true){
                    minPower = maxPower;
                    
                }
                    shootTime = Robot.dataCollectionTimer.get();
               // System.out.println("T0: " + shootTime );
                        break;

           /* case SHOOT_IDLE_MODE:
                validLocation = false;
                shooterPower = SHOOTER_IDLE_POWER;
                break; */

            default: 
                validLocation = false;
                shooterPower  = SHOOTER_OFF_POWER;
                break;
        }

        if (validLocation == true) {
            //System.out.println("valid location is true");
            shootTime = Robot.dataCollectionTimer.get();
            if(logTestData == true){
                System.out.println("T2: " + shootTime + " : " + flywheelShaftVelocity2 + " Power: " + shooterPower);
            }

            if (flywheelShaftVelocity2 > maxRPM) {
                shooterPower = minPower;
               // System.out.println("set min "  + shooterPower); 

              
                //System.out.println("T1: " + shootTime );
                }
            else if(flywheelShaftVelocity2 < minRPM) {
                shooterPower = maxPower;
            }

        }

        shtrMtrCtrlA.set(shooterPower);
        //System.out.println(shooterPower);            
        
    }

 
  
   

    public void displaySmartDashboard(){
        SmartDashboard.putNumber("RPM",             getFlywheelShaftVelocity() );
        SmartDashboard.putNumber("Power",           shooterPower);
        SmartDashboard.putNumber("Target Velocity", targetRPM);
        SmartDashboard.putNumber("ENC Position",    getFlywheelShaftPosition());
    }

}