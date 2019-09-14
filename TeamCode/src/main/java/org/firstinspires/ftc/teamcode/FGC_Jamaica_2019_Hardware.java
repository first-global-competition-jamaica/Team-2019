package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * This is the robots hardware.
 * In here all the robot's electronic physical components are initialized.
 */


public class FGC_Jamaica_2019_Hardware {


    public DcMotor FRight    = null;
    public DcMotor FLeft     = null;
    public DcMotor BRight    = null;
    public DcMotor BLeft     = null;
    public DcMotor Intake    = null;
    public DcMotor lift_motor1 = null;
    public DcMotor lift_motor2 = null;
    public DigitalChannel limit_lift_switch = null;
    public HardwareMap hwmap;



 public void  init (HardwareMap ahwmap){
     this.hwmap = ahwmap;
     FRight = hwmap.get(DcMotor.class, "FRight");
     FLeft  = hwmap.get(DcMotor.class, "FLeft");
     BRight = hwmap.get(DcMotor.class, "BRight");
     BLeft  = hwmap.get(DcMotor.class, "BLeft");
     Intake = hwmap.get(DcMotor.class, "Intake");
     limit_lift_switch = hwmap.get(DigitalChannel.class, "lift_switch");
     lift_motor1 = hwmap.get(DcMotor.class, "limit_Left");
     lift_motor2 = hwmap.get(DcMotor.class, "limit_right");

     FLeft.setDirection(DcMotor.Direction.FORWARD);
     FRight.setDirection(DcMotor.Direction.REVERSE);
     BLeft.setDirection(DcMotor.Direction.FORWARD);
     BRight.setDirection(DcMotor.Direction.REVERSE);
     lift_motor1.setDirection(DcMotor.Direction.REVERSE);
     lift_motor2.setDirection(DcMotor.Direction.REVERSE);
     limit_lift_switch.setMode(DigitalChannel.Mode.INPUT);

 }
}
