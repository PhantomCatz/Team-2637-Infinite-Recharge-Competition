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
    private DigitalInput indexerEntranceBumpSwitch;

    private Ultrasonic   ballSensor;

    private CANSparkMax  indexerMtrCtrl;

    private boolean indexerEntranceBumpSwitchState = false;
    private boolean transferingBallToIndexer       = false;

    private boolean shooterRunning     = false;
    private boolean reachedMaxCapaticy = false;

    private final int INDEXER_ENTRANCE_BUMPSWITCH_DIO_PORT = 1;

    public  final int INDEXER_MC_PDP_PORT = 10;
    private final int INDEXER_MC_CAN_ID =20;

    private final int BALL_SENSOR_INPUT_DIO_PORT = 4;
    private final int BALL_SENSOR_OUTPUT_DIO_PORT = 5;

    private final double beltSpeed = 0.5;
    private double sensorRange = 0;
    private int ballCount = 0;

    private ArrayList<Double> dataArray;

    public CatzIndexer()
    {
        indexerEntranceBumpSwitch = new DigitalInput(INDEXER_ENTRANCE_BUMPSWITCH_DIO_PORT);

        indexerMtrCtrl = new CANSparkMax(INDEXER_MC_CAN_ID, MotorType.kBrushless);//maybe add current limit
        indexerMtrCtrl.restoreFactoryDefaults();
        indexerMtrCtrl.setIdleMode(IdleMode.kBrake);

        ballSensor = new Ultrasonic(BALL_SENSOR_INPUT_DIO_PORT,BALL_SENSOR_OUTPUT_DIO_PORT);//(input,output)
        ballSensor.setAutomaticMode(true);

        dataArray = new ArrayList<>();
    }

    public void runIndexer()
    {
        dataArray.add(getUltraSonicSensorReading());
        indexerEntranceBumpSwitchState = getBumpSwitchState();

        if(shooterRunning)
        {
            indexerMtrCtrl.set(beltSpeed);
            ballCount = 0; //this is assuming that when we run the shooter, it will shoot all balls from the indexer
        }
        else 
        {
            //todo: make everything easier to read and simplify logic to the best of my ability
            if(!isBallInIntake() && !transferingBallToIndexer && !indexerEntranceBumpSwitchState)
            {
                indexerMtrCtrl.set(0);
            }
            else if(isBallInIntake()){
                transferingBallToIndexer = true;
                indexerMtrCtrl.set(beltSpeed);
            }
            else if (!isBallInIntake() && transferingBallToIndexer && !indexerEntranceBumpSwitchState)
            {
                transferingBallToIndexer = true;
                indexerMtrCtrl.set(beltSpeed);
            }
            else if (!isBallInIntake() && transferingBallToIndexer && indexerEntranceBumpSwitchState)
            {
                ballCount ++;
                transferingBallToIndexer = false;
                indexerMtrCtrl.set(0);
            }
        }

        if(ballCount >= 5)
        {
            reachedMaxCapaticy = true;
        }

    }

    public void showSmartDashboard()
    {
        sensorRange = ballSensor.getRangeInches();
        dataArray.add(getUltraSonicSensorReading());
        SmartDashboard.putNumber("Ball Count in Indexer: ", ballCount);
        SmartDashboard.putBoolean("Indexer Reached Max Capacity", reachedMaxCapaticy);
        SmartDashboard.putNumber("Ball Sensor", sensorRange);
        SmartDashboard.putBoolean("is Transfering ball", transferingBallToIndexer);
        SmartDashboard.putBoolean("Indexer bump switch state", getBumpSwitchState());
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
        return reachedMaxCapaticy;
    }

    public boolean getBumpSwitchState()
    {
        return !indexerEntranceBumpSwitch.get();
    }

    public double getUltraSonicSensorReading()
    {
        return ballSensor.getRangeInches();
    }

    public boolean isBallInIntake()
    {
        sensorRange = ballSensor.getRangeInches();
        if (sensorRange < 5)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public ArrayList<Double> getDataArray()
    {
        return dataArray;
    }

}