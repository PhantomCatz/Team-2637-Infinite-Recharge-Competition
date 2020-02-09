package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class CatzShooter
{
    private WPI_TalonSRX  shtrMtrCtrlA;
    private WPI_VictorSPX shtrMtrCtrlB;

    private final int SHTR_MC_ID_A = 40; 
    private final int SHTR_MC_ID_B = 41;
    
    public CatzShooter()
    {
        shtrMtrCtrlA = new WPI_TalonSRX (SHTR_MC_ID_A);
        shtrMtrCtrlB = new WPI_VictorSPX(SHTR_MC_ID_B);

        //Reset configuration
        shtrMtrCtrlA.configFactoryDefault();
        shtrMtrCtrlB.configFactoryDefault();

        //Set MC B to follow MC A
        shtrMtrCtrlB.follow(shtrMtrCtrlA);

        //Configure feedback device for PID loop
        shtrMtrCtrlA.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 100);
    
        //Set MC's to coast mode
        shtrMtrCtrlA.setNeutralMode(NeutralMode.Coast);
        shtrMtrCtrlB.setNeutralMode(NeutralMode.Coast);

        //Configure PID constants
        shtrMtrCtrlA.config_kP(0, 0.05);
        shtrMtrCtrlA.config_kI(0, 0.0005);
        shtrMtrCtrlA.config_kD(0, 0.1);
        shtrMtrCtrlA.config_kF(0, 0.005);
        shtrMtrCtrlA.config_IntegralZone(0, 0);

    }

    public void setTargetVelocity(double targetVelocity)
    {
        shtrMtrCtrlA.set(targetVelocity);
    }

    public void controllableShooterPower(double power)
    {
        shtrMtrCtrlA.set(power);
    }

    public void noPower()
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
    
    public void threeQuartersPower()
    {
        shtrMtrCtrlA.set(0.75);
    }

    public void maxPower()
    {
        shtrMtrCtrlA.set(1);
    }

    public double getShooterVelocity()
    {
        return shtrMtrCtrlA.getSensorCollection().getQuadratureVelocity();
    }

}