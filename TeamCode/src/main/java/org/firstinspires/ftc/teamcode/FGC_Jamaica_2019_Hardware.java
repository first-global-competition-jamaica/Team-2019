package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This is the robots hardware.
 * In here all the robot's electronic physical components are initialized.
 */


public class FGC_Jamaica_2019_Hardware {

    // Wheel motors
    public DcMotor FRight    = null;
    public DcMotor FLeft     = null;
    public DcMotor BRight    = null;
    public DcMotor BLeft     = null;
    // Intake Motors
    public DcMotor Intake    = null;
    // Lift Motors
    public DcMotor liftMotor1 = null;
    public DcMotor liftMotor2 = null;
    public DcMotor liftMotor3 = null;
    // Basket Servos
    public Servo basketServo1 = null;
    public Servo basketServo2 = null;
    // Sensors
    public DigitalChannel limit_lift_switch = null;
    // Shortened hardware map
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
     liftMotor1 = hwmap.get(DcMotor.class, "liftLeft");
     liftMotor2 = hwmap.get(DcMotor.class, "liftRight");
     liftMotor3 = hwmap.get(DcMotor.class,  "liftMotor3");
     basketServo1 = hwmap.get(Servo.class, "servo1");
     basketServo2 = hwmap.get(Servo.class, "servo2");

     //Motor and Servo directions
     FLeft.setDirection(DcMotor.Direction.FORWARD);
     BLeft.setDirection(DcMotor.Direction.FORWARD);
     FRight.setDirection(DcMotor.Direction.REVERSE);
     BRight.setDirection(DcMotor.Direction.REVERSE);
     liftMotor1.setDirection(DcMotor.Direction.REVERSE);
     liftMotor2.setDirection(DcMotor.Direction.REVERSE);
     liftMotor3.setDirection(DcMotor.Direction.REVERSE);
     basketServo1.setDirection(Servo.Direction.FORWARD);
     basketServo2.setDirection(Servo.Direction. REVERSE);
     // Config Setting
     liftMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
     limit_lift_switch.setMode(DigitalChannel.Mode.INPUT);



 }
}
