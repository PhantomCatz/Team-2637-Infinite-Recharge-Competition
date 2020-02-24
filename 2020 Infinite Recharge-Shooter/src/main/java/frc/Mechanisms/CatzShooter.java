
package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
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

    public static final int SHOOTER_STATE_OFF           = 0;
    public static final int SHOOTER_STATE_RAMPING       = 1;
    public static final int SHOOTER_STATE_SET_SPEED     = 2;
    public static final int SHOOTER_STATE_READY         = 3;
    public static final int SHOOTER_STATE_SHOOTING      = 4;
    
 
    final double SHOOTER_TARGET_VEL_TARGET_ZONE_RPM = 4000.0; //TBD
    final double SHOOTER_TARGET_VEL_START_LINE_RPM  = 4300.0; //RPM

    final double SHOOTER_BANG_BANG_MAX_RPM_OFFSET = 5.0; 
    final double SHOOTER_BANG_BANG_MIN_RPM_OFFSET = 5.0;

    final double SHOOTER_RAMP_RPM_OFFSET = 100.0;

    final double SHOOTER_OFF_POWER   = 0.0;
    final double SHOOTER_RAMP_POWER  = -1.0;
    final double SHOOTER_SHOOT_POWER = -1.0;

    final int NUM_OF_DATA_SAMPLES_TO_AVERAGE = 5;

    public double targetRPM          = 0.0;
    public double targetRPMThreshold = 0.0;
    public double shooterPower       = 0.0;
    public double minPower           = 0.0;
    public double maxPower           = 0.0;

    private boolean readyToFire =     false; 

    public boolean logTestData = false;
    
    private Thread shooterThread;

    private int shooterState = SHOOTER_STATE_OFF;

    private int shootStateCount  = 0;
    

    private boolean shooterIsReady = false;
    double avgVelocity             = 0.0;
    
    

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

        setShooterVelocity();

 
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


    public void setTargetRPM(double velocity)
    {
        targetRPM = velocity;
    }

    public void shoot()
    {
        if(shooterState == SHOOTER_STATE_READY)
        {
        shootStateCount = 0;
        shooterState = SHOOTER_STATE_SHOOTING;
        shooterPower = SHOOTER_SHOOT_POWER;
        shtrMtrCtrlA.set(shooterPower);
        }
    }

    public void shooterOff()
    {
        targetRPM    = 0.0;
        shooterState = SHOOTER_STATE_OFF;
        shooterPower = SHOOTER_OFF_POWER;
        shtrMtrCtrlA.set(shooterPower);
    }

    public void bangBang(double minRPM, double maxRPM, double flywheelShaftVelocity)
    {

        if (flywheelShaftVelocity > maxRPM)
        {
            shooterPower = minPower; 
        }
        else if(flywheelShaftVelocity < minRPM) 
        {
            shooterPower = maxPower;
        }
    
        shtrMtrCtrlA.set(shooterPower);
    }

    public void setShooterVelocity()
    {
        final double SHOOTER_THREAD_WAITING_TIME = 0.040;
        shooterThread = new Thread(() ->
        {
            double flywheelShaftVelocity    = -1.0;
            double minRPM                   = 0.0;
            double maxRPM                   = 0.0;
            double shootTime                = 0.0;
            double[] velocityData           = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
            double sumOfVelocityData        = 0.0;
            int velocityDataIndex           = 0;
            boolean readyToCalculateAverage = false;
            boolean rumbleSet               = false;

            

        while(true)
        {
            
            shootTime = Robot.dataCollectionTimer.get();
            flywheelShaftVelocity = getFlywheelShaftVelocity();

            switch (shooterState)
            {
                case SHOOTER_STATE_OFF:
                    shooterPower = SHOOTER_OFF_POWER;

                    if(targetRPM > 0.0)
                    {
                        sumOfVelocityData       = 0.0;
                        velocityDataIndex       = 0;
                        readyToCalculateAverage = false;
                        shooterIsReady          = false;
                        shooterState            = SHOOTER_STATE_RAMPING;
                        targetRPMThreshold      = targetRPM - SHOOTER_RAMP_RPM_OFFSET;
                        minRPM                  = targetRPM - SHOOTER_BANG_BANG_MIN_RPM_OFFSET;
                        maxRPM                  = targetRPM + SHOOTER_BANG_BANG_MAX_RPM_OFFSET;
                        shooterPower            = SHOOTER_RAMP_POWER;

                        getBangBangPower();
                        shtrMtrCtrlA.set(shooterPower);

                        Robot.xboxAux.setRumble(RumbleType.kLeftRumble, 0);

                        for(int i = 0; i < NUM_OF_DATA_SAMPLES_TO_AVERAGE; i++ )
                        {
                            velocityData[i] = 0.0;
                        }

                        shootTime = Robot.dataCollectionTimer.get();
                        System.out.println("T1: " + shootTime + " : " + flywheelShaftVelocity + " Power: " + shooterPower);
                    }

                break;

                case SHOOTER_STATE_RAMPING:

                    if(flywheelShaftVelocity > targetRPMThreshold)
                    {
                        shooterState = SHOOTER_STATE_SET_SPEED;
                        shooterPower = maxPower;
                        shtrMtrCtrlA.set(shooterPower);
                        System.out.println("T2: " + shootTime + " : " + flywheelShaftVelocity + " Power: " + shooterPower + "AvgRPM" + avgVelocity);

                    }
                    break;

                case SHOOTER_STATE_SET_SPEED:

                    velocityData[velocityDataIndex++] = flywheelShaftVelocity;

                    if(velocityDataIndex == NUM_OF_DATA_SAMPLES_TO_AVERAGE)
                    {
                        velocityDataIndex = 0;
                        readyToCalculateAverage = true;
                    }

                    if(readyToCalculateAverage == true)
                    {
                        for(int i = 0; i < NUM_OF_DATA_SAMPLES_TO_AVERAGE; i++ )
                        {
                            sumOfVelocityData = sumOfVelocityData + velocityData[i];
                        }
                        avgVelocity = sumOfVelocityData / NUM_OF_DATA_SAMPLES_TO_AVERAGE;
                        sumOfVelocityData = 0.0;
                        System.out.println("AD: " + avgVelocity);
                    }


                    if(avgVelocity > minRPM && avgVelocity < maxRPM)
                    {
                        shooterState = SHOOTER_STATE_READY;
                    }

                    bangBang(minRPM, maxRPM, flywheelShaftVelocity);
            
                    if(logTestData == true){
                    shootTime = Robot.dataCollectionTimer.get();
                    System.out.println("T3: " + shootTime + " : " + flywheelShaftVelocity + " Power: " + shooterPower);
                    }

                break;

                case SHOOTER_STATE_READY:
                    shooterIsReady = true;

                    bangBang(minRPM, maxRPM, flywheelShaftVelocity);   

                    if(rumbleSet == false)
                    {
                        Robot.xboxAux.setRumble(RumbleType.kLeftRumble, 1);
                        rumbleSet = true;
                    }
                break;

                case SHOOTER_STATE_SHOOTING:
                    shootStateCount++;
                    if(shootStateCount > 35)
                    {
                        shooterOff();
                    }
                break;
                
                default: 
                    System.out.println("DEFAULT STATE");
                    shooterOff();
                break;
        }        
            Timer.delay(SHOOTER_THREAD_WAITING_TIME);
    
        }
    });
    
        shooterThread.start();
    
}
    
/*
    *  power 0.62  6100 rpm 
    *  power 0.55  5400 rpm
    *  power 0.54  5300 rpm
    *  power 0.52  5100 rpm
    *  power 0.50  4700 rpm
    *  power 0.48  4500 rpm
    *  power 0.45  4200 rpm
    */


    public void getBangBangPower()
    {

       double power =  (targetRPM / 10000.0) - 0.01;
       minPower = -(power - 0.05);
       maxPower = -(power + 0.05);

        
     /*   if(targetRPM < 4000.0)
        {
            maxPower = -0.50;
            minPower = -0.40;
        }
        else if(targetRPM >= 4000.0 && targetRPM < 4500.0)
        {
            maxPower = -0.50;
            minPower = -0.40;
        }
        else if(targetRPM >= 4500.0 && targetRPM < 5000.0)
        {
            maxPower = -0.50;
            minPower = -0.45;
        }
        else if(targetRPM >= 5000.0 && targetRPM < 5500.0)
        {
            maxPower = -0.55;
            minPower = -0.45;
        }
        else if (targetRPM >= 5500.0 && targetRPM <6000.0)
        {
            maxPower = -0.60;
            minPower = -0.50;
        }
        else if (targetRPM >= 6000.0)
        {
            maxPower = -0.90;
            minPower = -0.45;
        } */
        
    }


    public void debugSmartDashboard(){
        SmartDashboard.putNumber("RPM",             getFlywheelShaftVelocity() );
        SmartDashboard.putNumber("Power",           shooterPower);
        SmartDashboard.putNumber("Target Velocity", targetRPM);
        SmartDashboard.putNumber("ENC Position",    getFlywheelShaftPosition());
        SmartDashboard.putNumber("Average rpm",  avgVelocity);
    
    }

    public void smartdashboard()
    {
        SmartDashboard.putBoolean("Shooter ready", shooterIsReady); 

    }
}

