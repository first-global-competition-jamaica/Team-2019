package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp (name = "FGC Robot", group = "FGC")

public class Robot_FGC extends LinearOpMode {

    private Hardware_FGC RB = new Hardware_FGC();
    private ElapsedTime runtime  = new ElapsedTime();

    double LeftP ;
    double RightP;
    private boolean a_press = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        RB.init(hardwareMap);
        runtime.reset();



        waitForStart();
        while (opModeIsActive()){

            Movement();

            Intake();
        }
    }


    private void Movement(){
        LeftP  =  gamepad1.left_stick_y  * 0.25;
        RightP =  gamepad1.right_stick_y * 0.25;
        RB.FLeft.setPower(LeftP);
        RB.FRight.setPower(RightP);
        RB.BLeft.setPower(LeftP);
        RB.BRight.setPower(RightP);

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
            if (gamepad1.right_trigger > 0){
                RB.Intake.setPower(gamepad1.right_trigger);
            }
            else if (gamepad1.left_trigger > 0){
                RB.Intake.setPower(-gamepad1.left_trigger);
            }else{
                RB.Intake.setPower(0);
            }
        }

        // code for the switching action for the conveyor
        if (gamepad1.a) {
            RB.Intake.setPower(1);
            a_press = true;
            telemetry.addData("Conveyor Status" , "ON");
            telemetry.update();
        }
        if (gamepad1.b){
            RB.Intake.setPower(0);
            a_press = false;
            telemetry.addData("Conveyor Status" , "OFF");
            telemetry.update();
        }

    }

}
