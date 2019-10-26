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

//TODO: Stop intake when lift is active untested

//TODO: Fix the speed telemetry
//TODO: Fix how runtime is displayed


public class FGC_Jamaica_2019_Main extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    //New hardware mapping variables
    private FGC_Jamaica_2019_Hardware robot_hardware = new FGC_Jamaica_2019_Hardware();


    private double LeftP;
    private double RightP;
    private double boxPower;
    private double speedMultiplier = 0.5;
    private boolean liftUp = false;
    private boolean intake_state = false;
    private int lift_halfway_point = -517 / 2;
    private int midpoint = 0;
    private double liftSpeed = 1;
    private double speedMultiplier2 = 0;
    private boolean buttonpressL =false;
    private boolean buttonpressR =false;




    @Override
    public void runOpMode() {
        robot_hardware.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //initialize the midpoint value for the lift
        midpoint = robot_hardware.liftMotor2.getCurrentPosition() + lift_halfway_point;

        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {

            movement();

            intake();

            lift();

            //basket();

            speedChanger();


            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }

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

    private void intake() {
        // Code to switch the intake on or off
        if (!liftUp) {
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
        } else {
            robot_hardware.Intake.setPower(0);
        }
    }

    public void speedChanger() {
        //set the maximum speed limit base of dpad value
        if (gamepad1.dpad_up && speedMultiplier <= 1) {
            speedMultiplier = speedMultiplier + 0.05;

        } else if (gamepad1.dpad_down && speedMultiplier >= 0.35) {
            speedMultiplier = speedMultiplier - 0.05;
        }
        telemetry.addData("maxSpeed", speedMultiplier);
    }

    public void basket() {


            while (gamepad2.a ) {
                boxPower = 1;
                robot_hardware.boxMotor.setPower(boxPower);
            }
    }


    private void lift() {

        if(gamepad2.left_bumper){
            buttonpressL = true;
        }else if(gamepad2.right_bumper){
            buttonpressR =true;
        }
             if(buttonpressL){
                liftSpeed = 1;
                robot_hardware.liftMotor1.setPower(liftSpeed);
                 robot_hardware.liftMotor2.setPower(liftSpeed);
             }else if(buttonpressR){
                 liftSpeed = -1;
                 robot_hardware.liftMotor1.setPower(liftSpeed);
                 robot_hardware.liftMotor2.setPower(liftSpeed);
             }
        }
    }
    
