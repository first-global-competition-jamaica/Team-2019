package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This is the robots hardware.
 * In here all the robot's electronic physical components are initialized.
 */


public class Hardware_FGC {


    public DcMotor FRight    = null;
    public DcMotor FLeft     = null;
    public DcMotor BRight    = null;
    public DcMotor BLeft     = null;
    public DcMotor Intake    = null;
    public HardwareMap hwmap;



 public void  init (HardwareMap ahwmap){
     this.hwmap = ahwmap;
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
