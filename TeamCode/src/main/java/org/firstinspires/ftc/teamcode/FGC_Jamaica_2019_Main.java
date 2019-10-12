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

//TODO: Make the limit switch code
//TODO: test the servo code
//TODO: Test The New Speed Reducer
//TODO: Magnetic Limit Switch installation
//TODO:
//TODO: Make a section In THe engeneering Notebook That speaks about our General Expeirience Coding For the comptition. Objectives for programming.



public class FGC_Jamaica_2019_Main extends LinearOpMode {

    private FGC_Jamaica_2019_Hardware robot_hardware = new FGC_Jamaica_2019_Hardware();
    private ElapsedTime runtime = new ElapsedTime();

    private double LeftP;
    private double RightP;
    private double SpeedMultiplier = 0.5;
   // private double dampeningThreshold = 0.05;
    private boolean a_press, gamemode = false;
    private int TicInitialOne = 0;
    private int TicInitialTwo = 0;
    private int TicFinalOne = 0;
    private int TicFinalTwo = 0 ;
    private int deltaTicOne = -TicInitialOne;
    private int deltaTicTwo = TicInitialTwo ;









    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot_hardware.init(hardwareMap);

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {

            movement();

            intake();

            lift();

            basket();

            Limit();

           // killSwitch();


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
        //Reduce the speed of the robot

        LeftP = LeftP * SpeedMultiplier;
        RightP = RightP * SpeedMultiplier;


        // Set the values to the wheel
        robot_hardware.FLeft.setPower(LeftP);
        robot_hardware.FRight.setPower(RightP);
        robot_hardware.BLeft.setPower(LeftP);
        robot_hardware.BRight.setPower(RightP);
        telemetry.addData("Controller Power:", "left (%.2f), right (%.2f)", LeftP, RightP);
    }

//--------------------------------------------------------------------------------------------------
//                                    INTAKE CONVEYOR
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
            if (gamepad2.right_trigger > 0) {
                robot_hardware.Intake.setPower(gamepad2.right_trigger);
                telemetry.addData("Conveyor Status", "ON");
            } else if (gamepad2.left_trigger > 0) {
                robot_hardware.Intake.setPower(-gamepad2.left_trigger);
                telemetry.addData("Conveyor Status", "ON");
            } else {
                robot_hardware.Intake.setPower(0);
                telemetry.addData("Conveyor Status", "OFF");
            }
        }

        // code for the switching action for the conveyor
        if (gamepad2.a) {
            robot_hardware.Intake.setPower(-1);
            a_press = true;
            telemetry.addData("Conveyor Status", "ON");
        } else if (gamepad2.b) {
            robot_hardware.Intake.setPower(0);
            a_press = false;
            telemetry.addData("Conveyor Status", "OFF");
        }

    }

    private  void lift() {

        robot_hardware.liftMotor1.setPower(gamepad2.right_stick_y);
        robot_hardware.liftMotor2.setPower(gamepad2.right_stick_y);
        telemetry.addData("Lift Status", "Lift On");
        telemetry.addData("Lift position", robot_hardware.liftMotor2.getCurrentPosition());
        telemetry.addData("Lift speed", gamepad2.right_stick_y*100);
        //telemetry.addData("motor direction", "Lift Up");
    }

//--------------------------------------------------------------------------------------------------
//                                    LIMIT SWITCH                                                //
//--------------------------------------------------------------------------------------------------

   public void Limit() {
        if( robot_hardware.limit_lift_switch.getState() == true) {
            robot_hardware.liftMotor1.setPower(0);
            robot_hardware.liftMotor1.setPower(0);
        }else if(robot_hardware.limit_lift_switch.getState() == false){
            robot_hardware.liftMotor1.setPower(0.2);
            robot_hardware.liftMotor2.setPower(0.2);
        }
    }//Unfinished

    public void basket(){
        if (gamepad2.dpad_up) {
            //Empties the Basket of pollutants
            robot_hardware.basketServo1.setPosition(0.05);//Right Servo
            robot_hardware.basketServo2.setPosition(0.6);//Left Servo
        }else if(gamepad2.dpad_down) {
            //Returns the basket to it's original position
            robot_hardware.basketServo1.setPosition(0.4);//Right Servo
            robot_hardware.basketServo2.setPosition(0.95);//Left Servo
            if(robot_hardware.basketButton.getState() == false){
                while(robot_hardware.basketButton.getState() == false){
                    double servo1Pos = robot_hardware.basketServo1.getPosition();
                    double servo2Pos = robot_hardware.basketServo2.getPosition();
                    robot_hardware.basketServo1.setPosition((servo1Pos+0.01));
                    robot_hardware.basketServo2.setPosition((servo2Pos+0.01));
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
        if(gamepad2.y && gamepad2.left_bumper  && gamepad2.right_bumper)
        {  robot_hardware.liftMotor1.setPower(0);
            robot_hardware.liftMotor2.setPower(0);
            robot_hardware.FLeft.setPower(0);
            robot_hardware.FRight.setPower(0);
            robot_hardware.BLeft.setPower(0);
            robot_hardware.BRight.setPower(0);

        } else if(gamepad2.a && gamepad2.b){
            robot_hardware.liftMotor1.setPower(0.3);
            robot_hardware.liftMotor2.setPower(0.3);
            robot_hardware.FLeft.setPower(0.35);
            robot_hardware.FRight.setPower(0.35);
            robot_hardware.BLeft.setPower(0.35);
            robot_hardware.BRight.setPower(0.35);

        }
    }//unfinished
//--------------------------------------------------------------------------------------------------
//                                  CONTROLLER DOCUMENTATION                                      //
//--------------------------------------------------------------------------------------------------

    /*These Are all the controller buttons and their uses in this method

      CONTROLLER 1
      Dpad_up    -> sets the value of speed reducer to  speed
      Dpad_down  -> sets the value of speed reducer to
      Dpad_left  -> N/A
      Dpad_right ->N/A
      leftAnalogStick -> left motor power
      RightAnalogStick -> right motor power
      buttonA -> starts intake
      buttonB -> stops intake
      buttonX ->N/A
      buttonY ->N/A
      right_bumper ->
      left_bumper ->

      CONTROLLER 2

      Dpad_up    -> sets the value of speed reducer to  speed
      Dpad_down  -> sets the value of speed reducer to
      Dpad_left  -> N/A
      Dpad_right ->N/A
      leftAnalogStick -> left motor power
      RightAnalogStick -> right motor power
      buttonA -> starts intake
      buttonB -> stops intake
      buttonX ->N/A
      buttonY ->N/A
      right_bumper ->
      left_bumper ->

     */

}

