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
//TODO: Make and test the servo code
//TODO: Test The New Speed Reducer
//TODO: Magnetic Limit Switch
//TODO: Make A Doccumentation Comment With All our Controlls
//TODO: Push Code to Kevonteh And Phillip
//TODO: Make a section In THe engeneering Notebook That speaks about our General Expeirience Coding For the comptition. Objectives for programming.



public class FGC_Jamaica_2019_Main extends LinearOpMode {

    private FGC_Jamaica_2019_Hardware robot_hardware = new FGC_Jamaica_2019_Hardware();
    private ElapsedTime runtime = new ElapsedTime();

    private double LeftP;
    private double RightP;
    private double SpeedMultiplier = 0.5;
   // private double dampeningThreshold = 0.05;
    private boolean a_press, gamemode = false;






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

            speedChangerVer2();

            Limit();

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
        //Reduce the speed of the robot

        LeftP = LeftP * SpeedMultiplier;
        RightP = RightP * SpeedMultiplier;


        // Set the values to the wheel
        robot_hardware.FLeft.setPower(LeftP);
        robot_hardware.FRight.setPower(RightP);
        robot_hardware.BLeft.setPower(LeftP);
        robot_hardware.BRight.setPower(RightP);
        double rightmotor = (robot_hardware.FRight.getPower() + robot_hardware.BRight.getPower())/2;
        double leftmotor = (robot_hardware.FLeft.getPower() + robot_hardware.BLeft.getPower())/2;
        telemetry.addData("Controller Power:", "left (%.2f), right (%.2f)", LeftP, RightP);
        telemetry.addData("Actual Motor Power:","left (%.2f), right (%.2f)", leftmotor, rightmotor);
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




    private  void lift() {

        // Initialization block
        int TicInitialOne = 0;       int TicInitialTwo = 0;
        int TicFinalOne = 0;         int deltaTic1Two = 0;
        int deltaTicOne = 0;         int deltaTicTwo = 0;
        int deltaTic = 0;

        if (gamepad2.left_bumper ) {
            robot_hardware.liftMotor1.setPower(0.3);
            robot_hardware.liftMotor2.setPower(0.3);
        }
        telemetry.addData("Lift Status", "Lift On");
        telemetry.update();

        if (gamepad2.right_bumper) {
            robot_hardware.liftMotor1.setPower(-0.3);
            robot_hardware.liftMotor2.setPower(-0.3);
        }
        telemetry.addData("Lift Status", "Lift Down");





        telemetry.addData("motordirection", "Lift Up");
    }

//--------------------------------------------------------------------------------------------------
//                                    LIMIT SWITCH
//--------------------------------------------------------------------------------------------------


    private void Lift();{

         if (robot_hardware.limit_lift_switch.getState()) {
            robot_hardware.liftMotor1.setPower(0);
            robot_hardware.liftMotor1.setPower(0);
        }else{
            robot_hardware.liftMotor1.setPower(0.3);
            robot_hardware.liftMotor2.setPower(0.3);
        }
    }





    public void basket(){
        if (gamepad2.dpad_up) {

            robot_hardware.basketServo1.setPosition(0.55);
            robot_hardware.basketServo2.setPosition(0.55);
        }else if(gamepad2.dpad_down)
        {
            robot_hardware.basketServo1.setPosition(0);
              robot_hardware.basketServo2.setPosition(0);

        }
    }







    public void speedChanger() {
        if (gamepad1.dpad_up) {

           SpeedMultiplier = 0.5;
        } else if (gamepad1.dpad_down) {
            SpeedMultiplier = 0.35;
        } else if (gamepad1.dpad_left) {
            SpeedMultiplier = 1;
        }
        telemetry.addData("Status","Max speed: " + SpeedMultiplier);
        telemetry.update();


    }
    //New code~Aldane Stennett 6/10/2019 dd/mm/yy
    public void speedChangerVer2 (){
        if(gamepad1.dpad_up && SpeedMultiplier <= 1 ) {
            SpeedMultiplier = SpeedMultiplier + 0.05;

        }else if(gamepad1.dpad_down && SpeedMultiplier >=0.35){
            SpeedMultiplier = SpeedMultiplier - 0.05;
        }
        telemetry.addData("Status", "Max speed:");
        telemetry.update();
    }


//--------------------------------------------------------------------------------------------------
//                                    EMERGENCY SHUTOFF                                           //
//--------------------------------------------------------------------------------------------------

    public void killSwitch() {
        { if(gamepad2.y && gamepad2.left_bumper && gamepad2.right_bumper)
             {  robot_hardware.liftMotor1.setPower(0);
                robot_hardware.liftMotor2.setPower(0);
                robot_hardware.FLeft.setPower(0);
                robot_hardware.FRight.setPower(0);
                robot_hardware.BLeft.setPower(0);
                robot_hardware.BRight.setPower(0);
             }
        }
    }

//--------------------------------------------------------------------------------------------------
//                                  CONTROLLER DOCUMENTATION                                      //
//--------------------------------------------------------------------------------------------------
       /**These Are all the Gamepad buttons and their uses in this method
         * Dpad_up    -> sets the value of speed reducer to  speed
         * Dpad_down  -> sets the value of speed reducer to
         * Dpad_left  ->
         * Dpad_right ->
         * leftAnalogStick
         * RightAnalogStick
         * buttonA
         * buttonB
         * buttonX
         * buttonY
         *
         */


    }