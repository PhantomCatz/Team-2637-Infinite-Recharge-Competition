package frc.Mechanisms;

import java.util.ArrayList;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.DataLogger.DataCollection;
import frc.robot.Robot;

public class CatzIndexer 
{
    public DigitalInput indexerEntranceSwitch;
    public DigitalInput indexerExitSwitch;
    public Ultrasonic   ballSensor;

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

    public boolean transferingBallToIndexer = false;

    private boolean shooterRunning     = false;
    public boolean reachedMaxCapacity = false;

    private final double BELT_SPEED = 0.4;
    private final double BALL_IN_RANGE_THRESHOLD = 6.0;
    public double sensorRange = 999.0;

    public int ballCount = 0;
    public final int MAX_NUM_BALLS = 5; //

    private final boolean BALL_PRESENT = false; //Bump switch pressed reads false, not pressed reads true

    //public static ArrayList<Double> indexerArrayList;  

    //public Timer indexTime2;

    private Thread indexerThread;

    public CatzIndexer()
    {
        indexerEntranceSwitch = new DigitalInput(INDEXER_ENTRANCE_SWITCH_DIO_PORT);
        indexerExitSwitch     = new DigitalInput(INDEXER_EXIT_SWITCH_DIO_PORT);

        indexerMtrCtrl = new CANSparkMax(INDEXER_MC_CAN_ID, MotorType.kBrushless);

        indexerMtrCtrl.restoreFactoryDefaults();
        indexerMtrCtrl.setIdleMode(IdleMode.kBrake);
        indexerMtrCtrl.setSmartCurrentLimit(INDEXER_MC_CURRENT_LIMIT);
        //indexerMtrCtrl.set(0.0);

        ballSensor = new Ultrasonic(BALL_SENSOR_INPUT_DIO_PORT,BALL_SENSOR_OUTPUT_DIO_PORT);//(input,output)
        ballSensor.setAutomaticMode(true);

        //indexerArrayList = new ArrayList<Double>();

    }

    public void startIndexerThread()
    {
        indexerThread = new Thread(() ->
        {
            while(true)
            {
            //indexerArrayList.add(1000.0);

            //indexerArrayList.add(Robot.dataCollectionTimer.get());
    
            indexerEntranceSwitchState = indexerEntranceSwitch.get();
            indexerExitSwitchState     = indexerExitSwitch.get();
    
            if(shooterRunning)
            {
                //indexerArrayList.add(1.0);
                indexerMtrCtrl.set(BELT_SPEED);
                System.out.println("1");
                ballCount = 0; //this is assuming that when we run the shooter, it will shoot all balls from the indexer
            }
            else 
            {
                //indexerArrayList.add(2.0);
                if(indexerExitSwitchState == BALL_PRESENT)
                {
                    //indexerArrayList.add(10.0);
                    reachedMaxCapacity = true;
                    indexerMtrCtrl.set(0.0);
                }
                else  
                {   
                    //indexerArrayList.add(20.0);
    
                    sensorRange = getUltraSonicSensorReading();
                    
                    //indexerArrayList.add(sensorRange);
    
                    //System.out.println(sensorRange + ", " + BALL_IN_RANGE_THRESHOLD);
                    if(sensorRange < BALL_IN_RANGE_THRESHOLD)
                    { 
                        //indexerArrayList.add(21.0);
                        //indexTime2.stop();                   //Indexer Motor Timeout
                        //indexTime2.reset();                  //Indexer Motor Timeout
                        transferingBallToIndexer = true;
                        indexerMtrCtrl.set(BELT_SPEED);
                        //System.out.println("2," +sensorRange);
                    }
                    else 
                    {
                        //indexerArrayList.add(22.0);
                        if(transferingBallToIndexer == true)
                        {
                            //indexerArrayList.add(23.0);
                            if(indexerEntranceSwitchState == BALL_PRESENT)
                            {
                                //indexerArrayList.add(24.0);
                                indexerMtrCtrl.set(0);
                                transferingBallToIndexer = false;
                                ballCount ++;
                                if(ballCount >= MAX_NUM_BALLS)
                                {
                                    //indexerArrayList.add(30.0);
                                    reachedMaxCapacity = true;
                                }
                            }
                            //indexTime2.start();                     //Indexer Motor Timeout
    
                            /*if(indexTime2.get() > 200 && indexerEntranceSwitchState == BALL_NOT_PRESENT)
                            {
                                transferingBallToIndexer = false;
                                indexerMtrCtrl.set(0.0);
                            }*/
                        }
                    }
                }           
            }
    
            //indexerArrayList.add(9999.0);
            //Timer.delay(0.002);
            }
        }
            );
            
        indexerThread.start();
        }

    public void stopIndexerThread()
    {
        indexerThread.interrupt();
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

    /*public void printTraceData()
    {
        int size = indexerArrayList.size();

        for (int i = 0; i <size;i++)
        {
           System.out.println(indexerArrayList.get(i));
        }  
    }*/
    public void indexerMotorOnly()
    {
        indexerMtrCtrl.set(BELT_SPEED);
    }

    public void indexerMotorOff()
    {
        indexerMtrCtrl.set(0.0);
    }
}