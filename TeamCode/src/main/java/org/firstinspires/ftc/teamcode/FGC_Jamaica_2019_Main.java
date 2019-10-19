package org.firstinspires.ftc.teamcode;


import android.graphics.ImageFormat;
import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Position;


@TeleOp (name = "Main Code", group = "FGC-Jamaica")


//TODO: Disengage the intake when lift up is true
//TODO: Fix the problem with the intake not taking input from the triggers
//TODO: Fix the speed telemetry
//TODO: Stop intake when lift is active
//TODO: increase srevo tilt angle
//TODO: Fix how runtime is displayed

public class FGC_Jamaica_2019_Main extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    //New hardware mapping variables
    private FGC_Jamaica_2019_Hardware robot_hardware = new FGC_Jamaica_2019_Hardware();

    // power variables for the driving wheels
    private double LeftP;
    private double RightP;
    private boolean wheel_brake = false;
    //private  boolean gamemode = false; only activate if one joystick mode needed

    // The speed limit for the drive wheels
    private double speedMultiplier = 0.5;
    private boolean liftUp = false;
    // Intake control variable
    private boolean intake_state = false;

    //lift system control variables
    private int lift_halfway_point = -517 / 2;
    private int midpoint = 0;

    //servo position initialization
    private double right_servo_open = 0.285;
    private double left_servo_open = 0.6;

    //Robot shutdown controller
    private boolean killswitch = false;

    private boolean liftDisable = false;

    //--------------------------------------------------------------------------------------------------
//                                       ALL METHODS                                              //
//--------------------------------------------------------------------------------------------------
    @Override
    public void runOpMode() {
        robot_hardware.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //initialize the midpoint value for the lift
        midpoint = robot_hardware.liftMotor2.getCurrentPosition() + lift_halfway_point;
        //initialize servo
        robot_hardware.basketServo1.setPosition(right_servo_open+0.5);
        robot_hardware.basketServo2.setPosition(left_servo_open+0.5);

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
        // set the powers for the wheels from the Joystick values for gamepad1
        LeftP = gamepad1.left_stick_y;
        RightP = gamepad1.right_stick_y;

        //Speed changing function
        speedChanger();

        // Code for the GameMode using one Joystick to control the robot
        //only activate if one joystick mode is needed
//        if (gamepad1.left_stick_button && gamepad1.a) {
//            gamemode = true;
//        }else(gamepad1.left_stick_button && gamepad1.right_stick_button){ gamemode = false; }
//        if(gamemode) {
//            LeftP = gamepad1.left_stick_y - gamepad1.left_stick_x;
//            RightP = gamepad1.left_stick_y + gamepad1.left_stick_x;
//        }

        // Sets the maximum speed of the motor
        LeftP = LeftP * speedMultiplier;
        RightP = RightP * speedMultiplier;

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
        // Code to switch the intake on or off
        if(!liftUp) {
            if (!intake_state) {
                if (gamepad1.right_trigger > 0) {
                    robot_hardware.Intake.setPower(gamepad1.right_trigger);
                    telemetry.addData("Conveyor Status: ", "REVERSE");
                } else if (gamepad1.left_trigger > 0) {
                    robot_hardware.Intake.setPower(-gamepad1.left_trigger);
                    telemetry.addData("Conveyor Status: ", "ON");
                } else {
                    robot_hardware.Intake.setPower(0);
                    telemetry.addData("Conveyor Status: ", "OFF");
                }
            }

            // Code to toggle the intake on or off
            if (gamepad1.a) {
                robot_hardware.Intake.setPower(-1);
                telemetry.addData("Conveyor Status: ", "ON");
                intake_state = true;
            } else if (gamepad1.b) {
                robot_hardware.Intake.setPower(0);
                telemetry.addData("Conveyor Status: ", "OFF");
                intake_state = false;

            }
        }else{
            robot_hardware.Intake.setPower(0);
        }
    }

//--------------------------------------------------------------------------------------------------
//                                       LINEAR SLIDE
//--------------------------------------------------------------------------------------------------
    private void lift() {
        if(!liftDisable) {
            if (!robot_hardware.limit_lift_switch.getState()) {
                if (robot_hardware.liftMotor2.getCurrentPosition() > midpoint && gamepad2.right_stick_y < 0) {
                    //allow the lift to move up if its at the extreme bottom
                    robot_hardware.liftMotor1.setPower(gamepad2.right_stick_y);
                    robot_hardware.liftMotor2.setPower(gamepad2.right_stick_y);
                    robot_hardware.liftMotor3.setPower(gamepad2.right_stick_y);
                    telemetry.addData("Lift Status: ", "Going up");
                } else if (robot_hardware.liftMotor2.getCurrentPosition() < midpoint && gamepad2.right_stick_y > 0) {
                    //allow the lift to move down if its at the extreme top
                    robot_hardware.liftMotor1.setPower(gamepad2.right_stick_y);
                    robot_hardware.liftMotor2.setPower(gamepad2.right_stick_y);
                    robot_hardware.liftMotor3.setPower(gamepad2.right_stick_y);
                    telemetry.addData("Lift Status: ", "Going down");
                } else {
                    //Turn the lift off if an opposing movement occur
                    robot_hardware.liftMotor1.setPower(0);
                    robot_hardware.liftMotor2.setPower(0);
                    robot_hardware.liftMotor3.setPower(0);
                    telemetry.addData("Lift Status: ", "At the limit");
                }
            } else {
                // allow the lift to be freely controlled when none of the magnetic switches
                // are detected
                robot_hardware.liftMotor1.setPower(gamepad2.right_stick_y);
                robot_hardware.liftMotor2.setPower(gamepad2.right_stick_y);
                robot_hardware.liftMotor3.setPower(gamepad2.right_stick_y);
                telemetry.addData("Lift Status:", "Free to move");
            }
        }

        // Check if the lift is up and assign the correct state for the lift up variable
        if (!robot_hardware.limit_lift_switch.getState() && robot_hardware.liftMotor2.getCurrentPosition() < midpoint) {
            liftUp = true;
        } else {
            liftUp = false;
        }


        //telemetry.addData("Lift position", ""+ robot_hardware.liftMotor2.getCurrentPosition());
        telemetry.addData("switch state", "" + robot_hardware.limit_lift_switch.getState());
        telemetry.addData("midpoint", "" + midpoint);

        telemetry.addData("Lift speed", gamepad2.right_stick_y * 100);
    }

//--------------------------------------------------------------------------------------------------
//                                        BASKET
//--------------------------------------------------------------------------------------------------

    public void basket() {
        // Only allow the basket to open when the lif is up
        if (liftUp) {
            if (gamepad2.dpad_up) {
                //Empties the Basket of pollutants
                robot_hardware.basketServo1.setPosition(right_servo_open-0.1);//Right Servo
                robot_hardware.basketServo2.setPosition(left_servo_open-0.1);//Left Servo
                liftDisable = true;
            } else if (gamepad2.dpad_down) {
                //Returns the basket to it's original position
                if(liftDisable){
                    for (int a = 0; a < 5; a++) {
                        double servo1Pos = robot_hardware.basketServo1.getPosition();
                        double servo2Pos = robot_hardware.basketServo2.getPosition();
                        robot_hardware.basketServo1.setPosition((servo1Pos + 0.1));
                        robot_hardware.basketServo2.setPosition((servo2Pos + 0.1));
                        sleep(100);
                    }
                    liftDisable = false;
                }

            }

        }
    }

//----------------------------------------------------------------------------------------------
//                                        SPEED CHANGER
//--------------------------------------------------------------------------------------------------
    public void speedChanger() {
        //set the maximum speed limit base of dpap value
        if (gamepad1.dpad_up && speedMultiplier <= 1) {
            speedMultiplier = speedMultiplier + 0.05;

        } else if (gamepad1.dpad_down && speedMultiplier >= 0.35) {
            speedMultiplier = speedMultiplier - 0.05;
        }
        telemetry.addData("Status", "Max speed:" +speedMultiplier);
    }

    public void wheelBrake(){
        if(gamepad1.y){
            wheel_brake = true;

        }else if(gamepad1.x){
            wheel_brake = false;
        }

        if(wheel_brake){
            robot_hardware.FLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot_hardware.FRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot_hardware.BLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot_hardware.BRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

//--------------------------------------------------------------------------------------------------
//                                    EMERGENCY SHUTOFF
//--------------------------------------------------------------------------------------------------

    public void killSwitch() {
        if (gamepad2.y && gamepad2.left_bumper && gamepad2.right_bumper) {
            killswitch = true;
        } else if (gamepad2.start && gamepad2.a && gamepad2.b) {
            killswitch = false;
        }

        if (killswitch = true) {
            robot_hardware.liftMotor1.setPower(0);
            robot_hardware.liftMotor2.setPower(0);
            robot_hardware.FLeft.setPower(0);
            robot_hardware.FRight.setPower(0);
            robot_hardware.BLeft.setPower(0);
            robot_hardware.BRight.setPower(0);
        }
    }
}

//--------------------------------------------------------------------------------------------------
//                                  CONTROLLER DOCUMENTATION
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
