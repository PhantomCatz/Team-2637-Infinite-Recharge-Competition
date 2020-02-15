package frc.Mechanisms;

import com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;

public class CatzIndexer 
{
    private DigitalInput intexerBumpSwitch;
    private DigitalInput indexerEntranceBumpSwitch;

    private boolean intexerBumpSwitchState         = false;
    private boolean indexerEntranceBumpSwitchState = false;
    private boolean transferingBallToIndexer       = false;

    private boolean shooterRunning     = false;
    private boolean reachedMaxCapaticy = false;

    private CANSparkMax indexerMtrCtrl;

    private final int INTEXER_BUMPSWITCH_DIO_PORT          = 1;
    private final int INDEXER_ENTRANCE_BUMPSWITCH_DIO_PORT = 0;

    private final int INDEXER_MC_CAN_ID = 20;

    private final double beltSpeed = 0.5;

    private int ballCount = 0;

    public CatzIndexer()
    {
        intexerBumpSwitch         = new DigitalInput(INTEXER_BUMPSWITCH_DIO_PORT);
        indexerEntranceBumpSwitch = new DigitalInput(INDEXER_ENTRANCE_BUMPSWITCH_DIO_PORT);

        indexerMtrCtrl = new CANSparkMax(INDEXER_MC_CAN_ID, MotorType.kBrushless);//maybe add current limit

        //Reset configuration
        indexerMtrCtrl.restoreFactoryDefaults();

        //Set indexer to brake mode
        indexerMtrCtrl.setIdleMode(IdleMode.kBrake);
    }

    public void runIndexer()
    {    
        if(shooterRunning)
        {
            indexerMtrCtrl.set(beltSpeed);
            ballCount = 0; //this is assuming that when we run the shooter, it will shoot all balls from the indexer
        }
        else 
        {
            intexerBumpSwitchState = intexerBumpSwitch.get();
            indexerEntranceBumpSwitchState = indexerEntranceBumpSwitch.get();

            //todo: make everything easier to read and simplify logic to the best of my ability
            if(!transferingBallToIndexer && !intexerBumpSwitchState && !indexerEntranceBumpSwitchState)
            {
                indexerMtrCtrl.set(0);
            }
            else if(intexerBumpSwitchState){
                transferingBallToIndexer = true;
                indexerMtrCtrl.set(beltSpeed);
                ballCount++;
            }
            else if (!intexerBumpSwitchState && !indexerEntranceBumpSwitchState && transferingBallToIndexer)
            {
                indexerMtrCtrl.set(beltSpeed);
            }
            else if (!intexerBumpSwitchState && indexerEntranceBumpSwitchState)
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

    public void indexerOverride(double power)
    {
        indexerMtrCtrl.set(power);
    }

}