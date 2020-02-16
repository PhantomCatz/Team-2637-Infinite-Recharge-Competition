/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.Mechanisms.CatzIndexer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  CatzIndexer indexer;
  private ArrayList<Double> dataArray;

  @Override
  public void robotInit() {

    indexer = new CatzIndexer();
  }

  @Override
  public void robotPeriodic() {
    
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    
  }

  @Override
  public void teleopPeriodic() {
    indexer.showSmartDashboard();
    indexer.runIndexer();
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void disabledInit(){
    
    
    /*//Uncomment this to collect data from the ultrasonic sensor
    dataArray = indexer.getDataArray();

    //create txt file
    String fileDirectory = "//media//sda1//testData.txt";
    try {
      File myObj = new File(fileDirectory);
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
      System.out.println(fileDirectory);
    } catch (IOException e) {
      System.out.println("Error creating a new file.");
      e.printStackTrace();
    }

    //write to file
    try {
      FileWriter myWriter = new FileWriter(fileDirectory, false);

      for(int i = 0; i < dataArray.size(); i ++){
        myWriter.append("bd: " + dataArray.get(i) + "\n");
      }

      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("Error writing to the file");
      e.printStackTrace();
    }

    System.out.println("Disabled Init Run Finished");
    */
    indexer.resetData();
    
  }

}
