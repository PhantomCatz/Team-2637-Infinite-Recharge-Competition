package frc.Mechanisms;

import java.util.ArrayList;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CatzIndexer 
{
    private DigitalInput indexerEntranceSwitch;
    private DigitalInput indexerExitSwitch;
    private Ultrasonic   ballSensor;

    private final int INDEXER_ENTRANCE_SWITCH_DIO_PORT = 3;
    private final int INDEXER_EXIT_SWITCH_DIO_PORT = 4;
    private final int BALL_SENSOR_INPUT_DIO_PORT = 5;
    private final int BALL_SENSOR_OUTPUT_DIO_PORT = 6;

    private CANSparkMax  indexerMtrCtrl;

    public  final int INDEXER_MC_PDP_PORT = 10;
    private final int INDEXER_MC_CAN_ID = 20;
    private final int INDEXER_MC_CURRENT_LIMIT = 60; //TBD

    private boolean indexerEntranceSwitchState = false;
    private boolean indexerExitSwitchState = false;

    private boolean transferingBallToIndexer = false;

    private boolean shooterRunning     = false;
    private boolean reachedMaxCapacity = false;

    private final double BELT_SPEED = 0.5;
    private final double BALL_IN_RANGE_THRESHOLD = 5.0;
    private double sensorRange = 0.0;
    private int ballCount = 0;

    private final boolean BALL_NOT_PRESENT = false;
    private final boolean BALL_PRESENT = true; 

    public static ArrayList<Integer> indexerArrayList;  

    public CatzIndexer()
    {
        indexerEntranceSwitch = new DigitalInput(INDEXER_ENTRANCE_SWITCH_DIO_PORT);
        indexerExitSwitch     = new DigitalInput(INDEXER_EXIT_SWITCH_DIO_PORT);

        indexerMtrCtrl = new CANSparkMax(INDEXER_MC_CAN_ID, MotorType.kBrushless);

        indexerMtrCtrl.restoreFactoryDefaults();
        indexerMtrCtrl.setIdleMode(IdleMode.kBrake);
        indexerMtrCtrl.setSmartCurrentLimit(INDEXER_MC_CURRENT_LIMIT);

        ballSensor = new Ultrasonic(BALL_SENSOR_INPUT_DIO_PORT,BALL_SENSOR_OUTPUT_DIO_PORT);//(input,output)
        ballSensor.setAutomaticMode(true);

        indexerArrayList = new ArrayList<Integer>();

    }

    public void runIndexerMtrCtrl(double power)
    {
        indexerMtrCtrl.set(power);
    }

    public void runIndexer()
    {
        indexerEntranceSwitchState = indexerEntranceSwitch.get();
        indexerExitSwitchState     = indexerExitSwitch.get();

        if(shooterRunning)
        {
            indexerMtrCtrl.set(BELT_SPEED);
            ballCount = 0; //this is assuming that when we run the shooter, it will shoot all balls from the indexer
        }
        else 
        {
            //todo: make everything easier to read and simplify logic to the best of my ability
            if(!isBallInIntexer() && !transferingBallToIndexer && !indexerEntranceSwitchState)
            {
                indexerMtrCtrl.set(0);
            }
            else if(isBallInIntexer())
            {
                transferingBallToIndexer = true;
                indexerMtrCtrl.set(BELT_SPEED);
            }
            else if (!isBallInIntexer() && transferingBallToIndexer && !indexerEntranceSwitchState)
            {
                transferingBallToIndexer = true;
                indexerMtrCtrl.set(BELT_SPEED);
            }
            else if (!isBallInIntexer() && transferingBallToIndexer && indexerEntranceSwitchState)
            {
                ballCount ++;
                transferingBallToIndexer = false;
                indexerMtrCtrl.set(0);
            }
        }

        if(ballCount >= 5)
        {
            reachedMaxCapacity = true;
        }

    }

    public void runIndexer2()
    {
        indexerArrayList.add(1000);

        indexerEntranceSwitchState = indexerEntranceSwitch.get();
        indexerExitSwitchState     = indexerExitSwitch.get();

        if(shooterRunning)
        {
            indexerArrayList.add(1);
            indexerMtrCtrl.set(BELT_SPEED);
            ballCount = 0; //this is assuming that when we run the shooter, it will shoot all balls from the indexer
        }
        else 
        {
            indexerArrayList.add(2);
            if(indexerExitSwitchState == BALL_PRESENT)
            {
                indexerArrayList.add(10);
                reachedMaxCapacity = true;
            }
            else  
            {   
                indexerArrayList.add(20);
                if(isBallInIntexer() == true)
                { 
                    indexerArrayList.add(21);
                    transferingBallToIndexer = true;
                    indexerMtrCtrl.set(BELT_SPEED);
                }
                else 
                {
                    indexerArrayList.add(22);
                    if(transferingBallToIndexer == true)
                    {
                        indexerArrayList.add(23);
                        if(indexerEntranceSwitchState == BALL_PRESENT)
                        {
                            indexerArrayList.add(24);
                            indexerMtrCtrl.set(0);
                            transferingBallToIndexer = false;
                            ballCount ++;
                            if(ballCount >= 5)
                            {
                                indexerArrayList.add(30);
                                reachedMaxCapacity = true;
                            }
                        }
                    }
                }
            }           
        }

        indexerArrayList.add(9999);
    }

    public void showSmartDashboard()
    {
        sensorRange = ballSensor.getRangeInches();
        SmartDashboard.putNumber("Ball Count in Indexer: ", ballCount);
        SmartDashboard.putBoolean("Indexer Reached Max Capacity", reachedMaxCapacity);
        SmartDashboard.putNumber("Ball Sensor", sensorRange);
        SmartDashboard.putBoolean("is Transfering ball", transferingBallToIndexer);
        SmartDashboard.putBoolean("Indexer Enterance switch state", indexerEntranceSwitch.get());
        SmartDashboard.putBoolean("Indexer Exit switch state", indexerExitSwitch.get());

    }

    public void resetData()
    {
        ballCount = 0;
    }

    public void setShooterIsRunning(boolean isRunning)
    {
        this.shooterRunning = isRunning;
    }

    public int getBallCount()
    {
        return ballCount;
    }

    public boolean reachedMaxCapacity()
    {
        return reachedMaxCapacity;
    }

    public double getUltraSonicSensorReading()
    {
        return ballSensor.getRangeInches();
    }

    public boolean isBallInIntexer()
    {
        sensorRange = ballSensor.getRangeInches();

        if (sensorRange < BALL_IN_RANGE_THRESHOLD)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void printTraceData()
    {
        int size = indexerArrayList.size();

        for (int i = 0; i <size;i++)
        {
           System.out.println(indexerArrayList.get(i));
        }  
    }


}