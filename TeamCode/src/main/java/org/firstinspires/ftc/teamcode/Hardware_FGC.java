package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Hardware_FGC {


    public DcMotor FRight    = null;
    public DcMotor FLeft     = null;
    public DcMotor BRight   = null;
    public DcMotor BLeft     = null;

    public HardwareMap hwmap;


    Hardware_FGC (){

    }

 public void init (HardwareMap ahwmap){
  hwmap = ahwmap;

     FRight = hwmap.get(DcMotor.class, "FRight");
     FLeft  = hwmap.get(DcMotor.class, "FLeft");
     BRight = hwmap.get(DcMotor.class, "BRight");
     BLeft  = hwmap.get(DcMotor.class, "BLeft");


     FLeft.setDirection(DcMotor.Direction.FORWARD);
     FRight.setDirection(DcMotor.Direction.REVERSE);
     BLeft.setDirection(DcMotor.Direction.FORWARD);
     BRight.setDirection(DcMotor.Direction.REVERSE);

 }
}
