package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This is the robots hardware.
 * In here all the robot's electronic physical components are initialized.
 */


public class Hardware_FGC {


    public static DcMotor FRight    = null;
    public static DcMotor FLeft     = null;
    public static DcMotor BRight    = null;
    public static DcMotor BLeft     = null;
    public static DcMotor Intake    = null;

    public static HardwareMap hwmap;




    Hardware_FGC (){

    }

 public static void init (HardwareMap ahwmap){
  hwmap = ahwmap;

     FRight = hwmap.get(DcMotor.class, "FRight");
     FLeft  = hwmap.get(DcMotor.class, "FLeft");
     BRight = hwmap.get(DcMotor.class, "BRight");
     BLeft  = hwmap.get(DcMotor.class, "BLeft");
     Intake = hwmap.get(DcMotor.class, "Intake");


     FLeft.setDirection(DcMotor.Direction.FORWARD);
     FRight.setDirection(DcMotor.Direction.REVERSE);
     BLeft.setDirection(DcMotor.Direction.FORWARD);
     BRight.setDirection(DcMotor.Direction.REVERSE);

 }
}
