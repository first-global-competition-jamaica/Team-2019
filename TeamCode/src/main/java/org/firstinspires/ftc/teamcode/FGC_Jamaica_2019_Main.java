package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp (name = "Main Code", group = "FGC-Jamaica")

public class FGC_Jamaica_2019_Main extends LinearOpMode {

    private FGC_Jamaica_2019_Hardware robot_hardware = new FGC_Jamaica_2019_Hardware();
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

        robot_hardware.init(hardwareMap);

        waitForStart();
        runtime.reset();
        while (opModeIsActive()){

            movement();

            intake();

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }


    private void movement(){
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
        robot_hardware.FLeft.setPower(LeftP);
        robot_hardware.FRight.setPower(RightP);
        robot_hardware.BLeft.setPower(LeftP);
        robot_hardware.BRight.setPower(RightP);
        double rightmotor = (robot_hardware.FRight.getPower() + robot_hardware.BRight.getPower())/2;
        double leftmotor = (robot_hardware.FLeft.getPower() + robot_hardware.BLeft.getPower())/2;
        telemetry.addData("Controller Power:", "left (%.2f), right (%.2f)", LeftP, RightP);
        telemetry.addData("Actual Motor Power:", "left (%.2f), right (%.2f)",leftmotor, rightmotor);
    }

    private void intake(){
        // code for the button action

        if(!a_press){
//            if (gamepad1.right_trigger > 0){
//                robot_hardware.Intake.setPower(1);
//            }else if (gamepad1.left_trigger >0){
//                robot_hardware.Intake.setPower(-1);
//            }else{
//                robot_hardware.Intake.setPower(0);
//            }


            //code to control the rate of the intake
            if (gamepad2.right_trigger > 0){
                robot_hardware.Intake.setPower(gamepad2.right_trigger);
                telemetry.addData("Conveyor Status" , "ON");
            }
            else if (gamepad2.left_trigger > 0){
                robot_hardware.Intake.setPower(-gamepad2.left_trigger);
                telemetry.addData("Conveyor Status" , "ON");
            }else{
                robot_hardware.Intake.setPower(0);
                telemetry.addData("Conveyor Status" , "OFF");
            }
        }

        // code for the switching action for the conveyor
        if (gamepad2.a ) {
            robot_hardware.Intake.setPower(-1);
            a_press = true;
            telemetry.addData("Conveyor Status" , "ON");
        }
        if (gamepad2.b){
            robot_hardware.Intake.setPower(0);
            a_press = false;
            telemetry.addData("Conveyor Status" , "OFF");
        }

    }

    private void lift(){
        if(gamepad1.left_bumper){
            robot_hardware.lift1.setpower(0.45);
            robot_hardware.lift2.setpower(0.45);

        }else if(gamepad1.right_bumper){
            robot_hardware.lift1.setpower(-0.45);

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
    /**These Are all the Gamepad buttons and their uses in this method
     * Dpad_up   -> sets the value of speed reducer to  speed
     * Dpad_down -> sets the value of speed reducer to
     * Dpad_left
     * Dpad_right
     */


    public double[] speedDampener(double leftPower, double rightPower){
        double  newLeftPower= 0;
        double newRightPower = 0;
        double[] values = new double[2];
        double rightmotor = (robot_hardware.FRight.getPower() + robot_hardware.BRight.getPower())/2;
        double leftmotor = (robot_hardware.FLeft.getPower() + robot_hardware.BLeft.getPower())/2;
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