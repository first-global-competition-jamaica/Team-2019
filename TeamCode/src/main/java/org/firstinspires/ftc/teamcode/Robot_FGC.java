package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp (name = "FGC Robot", group = "FGC")

public class Robot_FGC extends LinearOpMode {

    private Hardware_FGC RB = new Hardware_FGC();
    private ElapsedTime runtime  = new ElapsedTime();

    private double LeftP ;
    private double RightP;
    private double SpeedReducer = 0.35;
    private  double dampeningThreshold = 0.05;
    private boolean a_press, gamemode = false;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        RB.init(hardwareMap);

        waitForStart();
        runtime.reset();
        while (opModeIsActive()){

            Movement();

            Intake();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }


    private void Movement(){
        LeftP  =  gamepad1.left_stick_y;
        RightP =  gamepad1.right_stick_y;

        //Speed changer
        speedChanger();

        // Code for the GameMode using one Joystick to control the robot
        if(gamepad1.left_stick_button && gamepad1.a){
            gamemode = true;
        }else if(gamepad1.left_stick_button && gamepad1.right_stick_button){
            gamemode = false;
        }

        if(gamemode){
            LeftP  = gamepad1.left_stick_y - gamepad1.left_stick_x;
            RightP = gamepad1.left_stick_y + gamepad1.left_stick_x;
        }
        //Reduce the speed of the robot

        LeftP = LeftP * SpeedReducer;
        RightP = RightP * SpeedReducer;

        //dampen the speed
        double[] newSpeed = speedDampener(LeftP, RightP);
        LeftP = newSpeed[0];
        RightP = newSpeed[1];

        // Set the values to the wheel
        RB.FLeft.setPower(LeftP);
        RB.FRight.setPower(RightP);
        RB.BLeft.setPower(LeftP);
        RB.BRight.setPower(RightP);
        double rightmotor = (RB.FRight.getPower() + RB.BRight.getPower())/2;
        double leftmotor = (RB.FLeft.getPower() + RB.BLeft.getPower())/2;
        telemetry.addData("Controller Power:", "left (%.2f), right (%.2f)", LeftP, RightP);
        telemetry.addData("Actual Motor Power:", "left (%.2f), right (%.2f)",leftmotor, rightmotor);



    }


    private void Intake(){
        // code for the button action

        if(!a_press){
//            if (gamepad1.right_trigger > 0){
//                RB.Intake.setPower(1);
//            }else if (gamepad1.left_trigger >0){
//                RB.Intake.setPower(-1);
//            }else{
//                RB.Intake.setPower(0);
//            }


            //code to control the rate of the intake
            if (gamepad2.right_trigger > 0){
                RB.Intake.setPower(gamepad2.right_trigger);
                telemetry.addData("Conveyor Status" , "ON");
            }
            else if (gamepad2.left_trigger > 0){
                RB.Intake.setPower(-gamepad2.left_trigger);
                telemetry.addData("Conveyor Status" , "ON");
            }else{
                RB.Intake.setPower(0);
                telemetry.addData("Conveyor Status" , "OFF");
            }
        }

        // code for the switching action for the conveyor
        if (gamepad2.a ) {
            RB.Intake.setPower(-1);
            a_press = true;
            telemetry.addData("Conveyor Status" , "ON");
        }
        if (gamepad2.b){
            RB.Intake.setPower(0);
            a_press = false;
            telemetry.addData("Conveyor Status" , "OFF");
        }

    }

    public void speedChanger(){
        if(gamepad1.dpad_up){
            SpeedReducer = 0.5;
        }else if(gamepad1.dpad_down){
            SpeedReducer = 0.35;
        }else if(gamepad1.dpad_left){
            SpeedReducer = 1;
        }

    }

    public double[] speedDampener(double leftPower, double rightPower){
        double  newLeftPower= 0;
        double newRightPower = 0;
        double[] values = new double[2];
        double rightmotor = (RB.FRight.getPower() + RB.BRight.getPower())/2;
        double leftmotor = (RB.FLeft.getPower() + RB.BLeft.getPower())/2;
        double motordifference = rightmotor + leftmotor;
        double powerdifference = leftPower + rightPower;
        double overalldifference = motordifference + powerdifference;
        if((overalldifference > 0 && overalldifference <  0.75) || (overalldifference < 0 && overalldifference >  -0.75)){
            if ((rightmotor > dampeningThreshold && leftmotor > dampeningThreshold) || (rightmotor < -dampeningThreshold && leftmotor < -dampeningThreshold)) {
                values[0] = newLeftPower;
                values[1] = newRightPower;
                return values;
            }

        }


        values[0] = leftPower;
        values[1] = rightPower;
        return values;
    }
}
