package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
<<<<<<< HEAD
=======
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

>>>>>>> master
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class CatzShooter
{
    private WPI_TalonSRX shtrMtrCtrlA;
    private WPI_TalonSRX shtrMtrCtrlB;

    private final int SHTR_MC_ID_A = 40;
    private final int SHTR_MC_ID_B = 41;
    
<<<<<<< HEAD
    private boolean spinningUp;
    private boolean readyToShoot;

    private Thread shooterThread;
    private double shooterRPM;
    private double threshold;
    private double shooterPowerSum;
    private int counter;
    private double kF;
    private int numOfDataValues;

    private final double GEAR_RATIO = 1;
    

    public CatzShooter()
    {
        shtrMtrCtrlA = new WPI_TalonSRX (SHTR_MC_ID_A);
        shtrMtrCtrlB = new WPI_VictorSPX(SHTR_MC_ID_B);        
=======
    public final int SHOOTER_MC_A_PDP_PORT = 13;
    public final int SHOOTER_MC_B_PDP_PORT = 12;
    
    final double COUNTS_PER_REVOLUTION      = 4096.0;
    final double SEC_TO_MIN                 = 60.0;
    final double ENCODER_SAMPLE_RATE_MSEC   = 100.0;
    final double ENCODER_SAMPLE_PERIOD_MSEC = (1.0 / ENCODER_SAMPLE_RATE_MSEC);
    final double MSEC_TO_SEC                = 1000.0;
    final double FLYWHEEL_GEAR_REDUCTION    = 3.0;

    final double CONV_QUAD_VELOCITY_TO_RPM = ( ((ENCODER_SAMPLE_PERIOD_MSEC * MSEC_TO_SEC * SEC_TO_MIN) / COUNTS_PER_REVOLUTION));

    public CatzShooter()
    {
        shtrMtrCtrlA = new WPI_TalonSRX(SHTR_MC_ID_A);
        shtrMtrCtrlB = new WPI_TalonSRX(SHTR_MC_ID_B);
>>>>>>> master

        //Reset configuration
        shtrMtrCtrlA.configFactoryDefault();
        shtrMtrCtrlB.configFactoryDefault();

        //Set MC B to follow MC A
       shtrMtrCtrlB.follow(shtrMtrCtrlA);

        //Configure feedback device for PID loop
        shtrMtrCtrlA.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);
<<<<<<< HEAD
    
=======

        shtrMtrCtrlA.getSensorCollection().setQuadraturePosition(0,100);

>>>>>>> master
        //Set MC's to coast mode
        shtrMtrCtrlA.setNeutralMode(NeutralMode.Coast);
        shtrMtrCtrlB.setNeutralMode(NeutralMode.Coast);

        //Configure PID constants
        shtrMtrCtrlA.config_kP(0, 0.005);
        shtrMtrCtrlA.config_kI(0, 0.0);
        shtrMtrCtrlA.config_kD(0, 0.0);
        shtrMtrCtrlA.config_kF(0, 0.008); //shtrMtrCtrlA.config_kF(0, 0.008);
        shtrMtrCtrlA.config_IntegralZone(0, 0);
<<<<<<< HEAD

        spinningUp = false;
        readyToShoot = false;
    }   
=======
    }
>>>>>>> master

    public void setTargetRPM(double targetRPM)
    {
<<<<<<< HEAD
        double targetVelocity = targetRPM * GEAR_RATIO;
        shtrMtrCtrlA.set(targetVelocity);
=======
        shtrMtrCtrlA.set(ControlMode.Velocity, targetVelocity);
>>>>>>> master
    }
    public void controllableShooterPower(double power)
    {
        shtrMtrCtrlA.set(power);
    }
<<<<<<< HEAD
    public double getShooterRPM()
    {
        return (shtrMtrCtrlA.getSensorCollection().getQuadratureVelocity()*GEAR_RATIO);
    }
    public void noPower()
=======
    public void shooterFlyWheelDisable()
>>>>>>> master
    {
        shtrMtrCtrlA.set(0);
    }
    public void oneQuarterPower()
    {
        shtrMtrCtrlA.set(0.25);
    }
    public void halfPower()
    {
        shtrMtrCtrlA.set(0.5);
    }
<<<<<<< HEAD
=======

>>>>>>> master
    public void threeQuartersPower()
    {
        shtrMtrCtrlA.set(0.75);
    }
    public void maxPower()
    {
        shtrMtrCtrlA.set(1);
    }

    public double getFlywheelShaftPosition()
    {
        return shtrMtrCtrlA.getSensorCollection().getQuadraturePosition();
    }

    public double getFlywheelShaftVelocity()
    {
        return ((double) shtrMtrCtrlA.getSensorCollection().getQuadratureVelocity()) * CONV_QUAD_VELOCITY_TO_RPM; 
        //return ((double) shtrMtrCtrlA.getSensorCollection().getPulseWidthVelocity()); 
    }

    public double getaEncoderVelocity()
    {
        return ((double) shtrMtrCtrlA.getSensorCollection().getAnalogInVel());
    }

<<<<<<< HEAD
    public int getDrvTrainRTPosition()
    {
        return shtrMtrCtrlA.getSensorCollection().getQuadraturePosition();
    }
    public int getDrvTrainRTVelocity()
    {
        return shtrMtrCtrlA.getSensorCollection().getQuadratureVelocity();
    }

    public boolean isShooterSpinningUp()
    {
        return spinningUp;
    }
    public boolean isShooterReadyToShoot()
    {
        return readyToShoot;
    }

    public void startShooterControlLoop(double targetRPM)
    {
        shooterThread = new Thread(() ->
        {   
            if((shooterRPM < targetRPM - threshold) || (shooterRPM > targetRPM + threshold))
            {
                spinningUp   = true;
                readyToShoot = false;

                setTargetRPM(targetRPM);
            }
            else if(shooterRPM > (targetRPM - threshold) && (shooterRPM < (targetRPM + threshold)))
            {
                spinningUp   = false;
                readyToShoot = true;

                if(counter < numOfDataValues)
                {
                    shooterPowerSum += shtrMtrCtrlA.get();
                    counter++;
                }
                else if(counter == numOfDataValues)
                {
                    kF = (shooterPowerSum / ((double)numOfDataValues));
                }
                else
                {
                    shtrMtrCtrlA.set(kF);
                }

            }
        });
        shooterThread.start();
    }

    public void stopShooterControlLoop()
    {
        shooterThread.interrupt();
        spinningUp = false;
        readyToShoot = false;
=======
    public void testShootPower(double power)
    {
        shtrMtrCtrlA.set(power);
    }

    public void stopMotor(){
        shtrMtrCtrlA.set(0);
>>>>>>> master
    }
}