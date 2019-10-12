package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
    public DcMotor liftMotor1 = null;
    public DcMotor liftMotor2 = null;
    public Servo basketServo1 = null;
    public Servo basketServo2 = null;
    public DigitalChannel limit_lift_switch = null;
    public DigitalChannel basketButton =null;

    public HardwareMap hwmap;


// this is the hardware mapping of the device
 public void  init (HardwareMap ahwmap){
     this.hwmap = ahwmap;
     FRight = hwmap.get(DcMotor.class, "FRight");
     FLeft  = hwmap.get(DcMotor.class, "FLeft");
     BRight = hwmap.get(DcMotor.class, "BRight");
     BLeft  = hwmap.get(DcMotor.class, "BLeft");
     Intake = hwmap.get(DcMotor.class, "Intake");
     limit_lift_switch = hwmap.get(DigitalChannel.class, "lift_switch");
     basketButton = hwmap.get(DigitalChannel.class, "touchSensor");
     liftMotor1 = hwmap.get(DcMotor.class, "liftLeft");
     liftMotor2 = hwmap.get(DcMotor.class, "liftRight");
     basketServo1 = hwmap.get(Servo.class, "servo1");
     basketServo2 = hwmap.get(Servo.class, "servo2");


     FLeft.setDirection(DcMotor.Direction.FORWARD);
     FRight.setDirection(DcMotor.Direction.REVERSE);
     BLeft.setDirection(DcMotor.Direction.FORWARD);
     BRight.setDirection(DcMotor.Direction.REVERSE);
     liftMotor1.setDirection(DcMotor.Direction.REVERSE);
     liftMotor2.setDirection(DcMotor.Direction.REVERSE);
     limit_lift_switch.setMode(DigitalChannel.Mode.INPUT);
     basketButton.setMode(DigitalChannel.Mode.INPUT);
     basketServo1.setDirection(Servo.Direction.FORWARD);
     basketServo2.setDirection(Servo.Direction. REVERSE);

     liftMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
     liftMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    /* FRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Front Left Motor Encoder
     FRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Back Left Motor Encoder
     FRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Front Right Motor Encoder
     FRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Back Right Motor Encoder
     FRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Intake Motor Encoder
     FRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //liftMotor1 Motor Encoder
     FRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //liftMotor2 Motor Encoder

*/








 }
}
