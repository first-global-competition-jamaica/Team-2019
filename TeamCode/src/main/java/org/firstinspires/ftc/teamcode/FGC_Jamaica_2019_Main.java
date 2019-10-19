package org.firstinspires.ftc.teamcode;


import android.graphics.ImageFormat;
import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Position;


@TeleOp (name = "Main Code", group = "FGC-Jamaica")


//TODO: Make a section In THe engeneering Notebook That speaks about our General Expeirience Coding For the comptition. Objectives for programming.



public class FGC_Jamaica_2019_Main extends LinearOpMode {

    private FGC_Jamaica_2019_Hardware robot_hardware = new FGC_Jamaica_2019_Hardware();
    private ElapsedTime runtime = new ElapsedTime();

    private double LeftP;
    private double RightP;
    private double SpeedMultiplier = 0.5;
    private boolean liftUp = false;
    private boolean a_press, gamemode = false;
    private int lift_halfway_point = -517/2;
    private int midpoint= 0;
    private boolean killswitch  = false;

//--------------------------------------------------------------------------------------------------
//                                       ALL METHODS                                              //
//--------------------------------------------------------------------------------------------------

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        robot_hardware.init(hardwareMap);
        telemetry.update();
        midpoint = robot_hardware.liftMotor2.getCurrentPosition()+ lift_halfway_point;

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {

            movement();

            intake();

            lift();

            basket();

            killSwitch();

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }

//--------------------------------------------------------------------------------------------------
//                                         CHASSIS CODE                                           //
//--------------------------------------------------------------------------------------------------

    private void movement() {
        LeftP = gamepad1.left_stick_y;
        RightP = gamepad1.right_stick_y;

        //Speed changer
        speedChanger();

        // Code for the GameMode using one Joystick to control the robot
        if (gamepad1.left_stick_button && gamepad1.a) {
            gamemode = true;
        } else if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
            gamemode = false;
        }

        if (gamemode) {
            LeftP = gamepad1.left_stick_y - gamepad1.left_stick_x;
            RightP = gamepad1.left_stick_y + gamepad1.left_stick_x;
        }
        if (gamepad1.y) {
            LeftP = 0.6;
            RightP = 0.6;

        }        else if (!gamepad1.y) {
            LeftP = gamepad1.left_stick_y - gamepad1.left_stick_x;
            RightP = gamepad1.left_stick_y + gamepad1.left_stick_x;

        }
        //Reduce the speed of the robot

        LeftP = LeftP * SpeedMultiplier;
        RightP = RightP * SpeedMultiplier;


        // Set the values to the wheel
        robot_hardware.FLeft.setPower(LeftP);
        robot_hardware.FRight.setPower(RightP);
        robot_hardware.BLeft.setPower(LeftP);
        robot_hardware.BRight.setPower(RightP);
        telemetry.addData("Wheel Power:", "left (%.2f), right (%.2f)", LeftP, RightP);
    }

//--------------------------------------------------------------------------------------------------
//                                    INTAKE CONVEYOR                                             //
//--------------------------------------------------------------------------------------------------

    private void intake() {
        // code for the button action

        if (!a_press) {
//            if (gamepad1.right_trigger > 0){
//                robot_hardware.Intake.setPower(1);
//            }else if (gamepad1.left_trigger >0){
//                robot_hardware.Intake.setPower(-1);
//            }else{
//                robot_hardware.Intake.setPower(0);
//

            //code to control the rate of the intake
            if (gamepad1.right_trigger > 0) {
                robot_hardware.Intake.setPower(gamepad2.right_trigger);
                telemetry.addData("Conveyor Status", "ON");
            } else if (gamepad1.left_trigger > 0) {
                robot_hardware.Intake.setPower(-gamepad2.left_trigger);
                telemetry.addData("Conveyor Status", "ON");
            } else {
                robot_hardware.Intake.setPower(0);
                telemetry.addData("Conveyor Status", "OFF");
            }
        }

        // code for the switching action for the conveyor
        if (gamepad1.a) {
            robot_hardware.Intake.setPower(-1);
            a_press = true;
            telemetry.addData("Conveyor Status", "ON");
        } else if (gamepad1.b) {
            robot_hardware.Intake.setPower(0);
            a_press = false;
            telemetry.addData("Conveyor Status", "OFF");
        }

    }

//--------------------------------------------------------------------------------------------------
//                                       LINEAR SLIDE
//--------------------------------------------------------------------------------------------------
    private  void lift() {
        if(!robot_hardware.limit_lift_switch.getState()){
            if(robot_hardware.liftMotor2.getCurrentPosition() > midpoint && gamepad2.right_stick_y < 0){
                robot_hardware.liftMotor1.setPower(gamepad2.right_stick_y);
                robot_hardware.liftMotor2.setPower(gamepad2.right_stick_y);
            }else if(robot_hardware.liftMotor2.getCurrentPosition() < midpoint && gamepad2.right_stick_y > 0){
                robot_hardware.liftMotor1.setPower(gamepad2.right_stick_y);
                robot_hardware.liftMotor2.setPower(gamepad2.right_stick_y);
            }else{
                robot_hardware.liftMotor1.setPower(0);
                robot_hardware.liftMotor2.setPower(0);
            }
        }else{
            robot_hardware.liftMotor1.setPower(gamepad2.right_stick_y);
            robot_hardware.liftMotor2.setPower(gamepad2.right_stick_y);
        }

        telemetry.addData("Lift Status", "Lift Up");
        telemetry.addData("Lift position", ""+ robot_hardware.liftMotor2.getCurrentPosition());
        telemetry.addData("switch state", ""+robot_hardware.limit_lift_switch.getState());
        telemetry.addData("midpoint", ""+midpoint);
        telemetry.addData("lift stick", ""+gamepad2.right_stick_y);

        telemetry.addData("Lift speed", gamepad2.right_stick_y*100);
    }

//--------------------------------------------------------------------------------------------------
//                                        BASKET                                                  //
//--------------------------------------------------------------------------------------------------

    public void basket(){
        if(!robot_hardware.limit_lift_switch.getState() && robot_hardware.liftMotor2.getCurrentPosition() < midpoint ){
            liftUp = true;
        }else{
            liftUp = false;
        }
        if (liftUp) {
            if (gamepad2.dpad_up) {
                //Empties the Basket of pollutants
                robot_hardware.basketServo1.setPosition(0.0);//Right Servo
                robot_hardware.basketServo2.setPosition(0.55);//Left Servo
            } else if (gamepad2.dpad_down) {
                //Returns the basket to it's original position
                for(int a = 0; a <4; a++){
                    double servo1Pos = robot_hardware.basketServo1.getPosition();
                    double servo2Pos = robot_hardware.basketServo2.getPosition();
                    robot_hardware.basketServo1.setPosition((servo1Pos+0.1));
                    robot_hardware.basketServo2.setPosition((servo2Pos+0.1));
                    sleep(100);
                }
            }

        }
    }

//--------------------------------------------------------------------------------------------------
//                                        SPEED CHANGER                                           //
//--------------------------------------------------------------------------------------------------

    public void speedChanger (){
        if(gamepad1.dpad_up && SpeedMultiplier <= 1 ) {
            SpeedMultiplier = SpeedMultiplier + 0.05;

        }else if(gamepad1.dpad_down && SpeedMultiplier >=0.35){
            SpeedMultiplier = SpeedMultiplier - 0.05;
        }
        telemetry.addData("Status", "Max speed:");
    }

//--------------------------------------------------------------------------------------------------
//                                    EMERGENCY SHUTOFF                                           //
//--------------------------------------------------------------------------------------------------

    public void killSwitch() {
        if (gamepad2.y && gamepad2.left_bumper && gamepad2.right_bumper)
            {killswitch = true;}
        {

            if (killswitch = true) {
                robot_hardware.liftMotor1.setPower(0);
                robot_hardware.liftMotor2.setPower(0);
                robot_hardware.FLeft.setPower(0);
                robot_hardware.FRight.setPower(0);
                robot_hardware.BLeft.setPower(0);
                robot_hardware.BRight.setPower(0);

            } else if (gamepad2.start && gamepad2.a && gamepad2.b){
                killswitch = false;
            }else{
                killswitch = false;
            }


            }
        }
    }

//--------------------------------------------------------------------------------------------------
//                                  CONTROLLER DOCUMENTATION                                      //
//--------------------------------------------------------------------------------------------------

//      These Are all the controller buttons and their uses in this method
//
//      CONTROLLER 1
//      Dpad_up    -> sets the value of speed reducer to  speed
//      Dpad_down  -> sets the value of speed reducer to
//      Dpad_left  -> N/A
//      Dpad_right ->N/A
//      leftAnalogStick -> left motor power
//      RightAnalogStick -> right motor power
//      buttonA -> starts intake
//      buttonB -> stops intake
//      buttonX ->N/A
//      buttonY ->N/A
//      right_bumper ->
//      left_bumper ->
//
//      CONTROLLER 2
//
//      Dpad_up    -> sets the value of speed reducer to  speed
//      Dpad_down  -> sets the value of speed reducer to
//      Dpad_left  -> N/A
//      Dpad_right ->N/A
//      leftAnalogStick -> left motor power
//      RightAnalogStick -> right motor power
//      buttonA -> starts intake
//      buttonB -> stops intake
//      buttonX ->N/A
//      buttonY ->N/A
//      right_bumper ->
//      left_bumper ->
