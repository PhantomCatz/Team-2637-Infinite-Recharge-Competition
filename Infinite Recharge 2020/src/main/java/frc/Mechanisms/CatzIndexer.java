package frc.Mechanisms;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;

public class CatzIndexer { //CHANGE NAMING SCHEME THINGY DANIEL!!!! okay

    private DigitalInput bumpSwitch1;//the one closer to intake (make naming more meaningful) intex, index exit etc.
    private DigitalInput bumpSwitch2;//the one inside indexer

    private boolean switch1State;//same name add switch
    private boolean switch2State;
    private boolean ballInbetweenSwitches = false;//Transferring ball to indexer or something

    private final double beltSpeed = 0.5;

    private CANSparkMax beltMotor;

    private boolean shooterRunning = false;

    private final int bumpSwitch1ID = 1;//dio port number and check the values with Landon
    private final int bumpSwitch2ID = 0;
    private final int beltMotorID = 4;//can id

    public CatzIndexer(){
        bumpSwitch1 = new DigitalInput(bumpSwitch1ID);
        bumpSwitch2 = new DigitalInput(bumpSwitch2ID);

        beltMotor = new CANSparkMax(beltMotorID, MotorType.kBrushless);//maybe add current limit
        //default config here
        beltMotor.setIdleMode(IdleMode.kBrake);
    }

    public void runIndexer(){
        
        if(!shooterRunning)//switch position of this with the else
        {
            switch1State = bumpSwitch1.get();
            switch2State = bumpSwitch2.get();

            //make everything easier to read and simplify logic to the best of my ability
            if(!switch1State && !switch2State && !ballInbetweenSwitches)
            {
                beltMotor.set(0);
            }
            else if (!switch1State && !switch2State && ballInbetweenSwitches)
            {
                beltMotor.set(-beltSpeed);
            }
            else if (!switch1State && switch2State)
            {
                ballInbetweenSwitches = false;
                beltMotor.set(0);
            }
            else 
            {
                ballInbetweenSwitches = true;
                beltMotor.set(-beltSpeed);
            }
        }
        else 
        {

            beltMotor.set(-beltSpeed);
        }

    }

    public void setShooterIsRunning(boolean isRunning){
        this.shooterRunning = isRunning;
    }

}
