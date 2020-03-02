package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;

public class CatzClimber
{
    public CANSparkMax climbMtrCtrlA; 
    public CANSparkMax climbMtrCtrlB;

    private WPI_VictorSPX lightsaber;

    private final int CLIMB_MC_A_CAN_ID      = 30; 
    private final int CLIMB_MC_B_CAN_ID      = 31;

    public  final int CLIMB_MC_A_PDP_PORT    = 2;
    public  final int CLIMB_MC_B_PDP_PORT    = 3;

    private final int LIGHTSABER_MC_CAN_ID   = 50;

    public  final int LIGHTSABER_MC_PDP_PORT = 9; 

    private final double CLIMB_MOTOR_POWER        =  0.5;
    private final double LIGHTSABER_EXT_MOTOR_PWR = -0.4;
    private final double LIGHTSABER_RET_MOTOR_PWR =  0.4;
    private final double LIGHTSABER_OFF_MOTOR_PWR =  0.0;

    private final int CLIMB_MC_CURRENT_LIMIT    = 80;

    final double CLIMB_THREAD_PERIOD           = 0.020;
    final double CLIMB_TIMEOUT                 = 5.000;
    final double LIGHTSABER_MIN_HEIGHT_TIMEOUT = 2.000;    

    private final int CLIMB_NULL_MODE    = 0;
    private final int LIGHTSABER_EXTEND  = 1;
    private final int LIGHTSABER_RETRACT = 2;
    private final int CLIMB_RUN_WINCH    = 3;

    private int climbCountLimit               = 0;
    private int lightsaberMinHeightCountLimit = 0;
    private int climbCount                    = 0;
    private int lightsaberMinHeightCount      = 0;

    Thread climbThread;

    private int mode = CLIMB_NULL_MODE;

    public CatzClimber()
    {
        /***********************************************************************
        *  climb gearbox config
        ***********************************************************************/
        climbMtrCtrlA = new CANSparkMax(CLIMB_MC_A_CAN_ID, MotorType.kBrushed);
        climbMtrCtrlB = new CANSparkMax(CLIMB_MC_B_CAN_ID, MotorType.kBrushed);

        //Reset configuration
        climbMtrCtrlA.restoreFactoryDefaults();
        climbMtrCtrlB.restoreFactoryDefaults();

        //set current Limiting
        climbMtrCtrlA.setSmartCurrentLimit(CLIMB_MC_CURRENT_LIMIT);
        climbMtrCtrlB.setSmartCurrentLimit(CLIMB_MC_CURRENT_LIMIT);

        //set climb motor controller B to follow A
        climbMtrCtrlB.follow(climbMtrCtrlA);

        //Configure MC's to brake mode
        climbMtrCtrlA.setIdleMode(IdleMode.kBrake);
        climbMtrCtrlB.setIdleMode(IdleMode.kBrake);

        /***********************************************************************
        *  climb lightsaber config
        ***********************************************************************/
        lightsaber = new WPI_VictorSPX(LIGHTSABER_MC_CAN_ID);

        lightsaber.configFactoryDefault();                //Reset to Factory Def
        lightsaber.setNeutralMode(NeutralMode.Brake);     //Set breke mode
        
    }

    public void climbRunWinch()
    {   
        mode = CLIMB_RUN_WINCH;
        climbMtrCtrlA.set(CLIMB_MOTOR_POWER);  
    }

    public void lightsaberExtend()
    {
        //mode = LIGHTSABER_EXTEND;
        lightsaber.set(ControlMode.PercentOutput, LIGHTSABER_EXT_MOTOR_PWR);
    }   
    
    public void lightsaberRetract()
    {
        //mode = LIGHTSABER_RETRACT;
        lightsaber.set(ControlMode.PercentOutput, LIGHTSABER_RET_MOTOR_PWR);
    }

    public void lightsaberOff()
    {
        lightsaber.set(ControlMode.PercentOutput, LIGHTSABER_OFF_MOTOR_PWR);
    }

    public void climbControl() 
    {

        climbThread = new Thread(() -> //start of thread
        {
            boolean lightsaberRunning = false;

            while(true)
            {
                switch(mode)
                {
                    case LIGHTSABER_EXTEND:
                        if(lightsaberRunning == false)
                        {
                            lightsaber.set(ControlMode.PercentOutput, LIGHTSABER_EXT_MOTOR_PWR);
                            lightsaberRunning = true;
                        }    

                        if(lightsaberMinHeightCount >= lightsaberMinHeightCountLimit)
                        {
                            lightsaber.set(ControlMode.PercentOutput, LIGHTSABER_OFF_MOTOR_PWR);
                            lightsaberRunning = false;
                            mode = CLIMB_NULL_MODE;
                            lightsaberMinHeightCount = 0;
                        }
                        else
                        {
                            lightsaberMinHeightCount++;
                        }

                    break;

                    case LIGHTSABER_RETRACT:
                    break;

                    case CLIMB_RUN_WINCH:
                    break;

                    default:

                    break;
                }


                Timer.delay(CLIMB_THREAD_PERIOD);
            }
        });
        climbThread.start();
    }
}