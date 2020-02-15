package frc.Mechanisms;

import java.util.ArrayList;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CatzIndexer {

    private DigitalInput intexerBumpSwitch;
    private DigitalInput indexerEntranceBumpSwitch;
    private Ultrasonic ballSensor;

    private boolean intexerBumpSwitchState         = false;
    private boolean indexerEntranceBumpSwitchState = false;
    private boolean transferingBallToIndexer       = false;

    private boolean shooterRunning     = false;
    private boolean reachedMaxCapaticy = false;

    private CANSparkMax indexerMtrCtrl;

    private final int INTEXER_BUMPSWITCH_DIO_PORT          = 0;
    private final int INDEXER_ENTRANCE_BUMPSWITCH_DIO_PORT = 1;

    private final int INDEXER_MC_CAN_ID = 30;

    private final double beltSpeed = 0.5;
    
    private double sensorRange = 0;

    private int ballCount = 0;

    private ArrayList<Double> dataArray;

    public CatzIndexer(){
        intexerBumpSwitch = new DigitalInput(INTEXER_BUMPSWITCH_DIO_PORT);
        indexerEntranceBumpSwitch = new DigitalInput(INDEXER_ENTRANCE_BUMPSWITCH_DIO_PORT);

        indexerMtrCtrl = new CANSparkMax(INDEXER_MC_CAN_ID, MotorType.kBrushless);//maybe add current limit
        indexerMtrCtrl.restoreFactoryDefaults();
        indexerMtrCtrl.setIdleMode(IdleMode.kBrake);

        ballSensor = new Ultrasonic(2,3);//(input,output)
        ballSensor.setAutomaticMode(true);

        dataArray = new ArrayList<>();
    }

    public void runIndexer(){
        dataArray.add(getUltraSonicSensorReading());
        intexerBumpSwitchState = intexerBumpSwitch.get();
        indexerEntranceBumpSwitchState = getBumpSwitchState();

        if(shooterRunning)
        {
            indexerMtrCtrl.set(beltSpeed);
            ballCount = 0; //this is assuming that when we run the shooter, it will shoot all balls from the indexer
        }
        else 
        {

            //todo: make everything easier to read and simplify logic to the best of my ability
            if(!transferingBallToIndexer && !isBallInIntake() && !indexerEntranceBumpSwitchState)
            {
                indexerMtrCtrl.set(0);
            }
            else if(isBallInIntake()){
                transferingBallToIndexer = true;
                indexerMtrCtrl.set(beltSpeed);
                ballCount++;
            }
            else if (!isBallInIntake() && !indexerEntranceBumpSwitchState && transferingBallToIndexer)
            {
                transferingBallToIndexer = true;
                indexerMtrCtrl.set(beltSpeed);
            }
            else if (!isBallInIntake() && transferingBallToIndexer && indexerEntranceBumpSwitchState)
            {
                transferingBallToIndexer = false;
                indexerMtrCtrl.set(0);
            }
        }

        if(ballCount >= 5)
        {
            reachedMaxCapaticy = true;
        }
    }

    public void showSDS(){
        sensorRange = ballSensor.getRangeInches();
        dataArray.add(getUltraSonicSensorReading());
        SmartDashboard.putNumber("Ball Count in Indexer: ", ballCount);
        SmartDashboard.putBoolean("Indexer Reached Max Capacity", reachedMaxCapaticy);
        SmartDashboard.putNumber("Ball Sensor", sensorRange);
        SmartDashboard.putBoolean("transfering ball", transferingBallToIndexer);
        SmartDashboard.putBoolean("bump switch state", getBumpSwitchState());
    }

    public void setShooterIsRunning(boolean isRunning){
        this.shooterRunning = isRunning;
    }

    public int getBallCount(){
        return ballCount;
    }

    public boolean reachedMaxCapacity(){
        return reachedMaxCapaticy;
    }

    public boolean getBumpSwitchState(){
        return !indexerEntranceBumpSwitch.get();
    }

    public double getUltraSonicSensorReading(){
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

    public ArrayList<Double> getDataArray(){
        return dataArray;
    }

}
