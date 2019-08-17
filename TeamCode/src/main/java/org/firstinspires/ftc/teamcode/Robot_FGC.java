package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp (name = "FGC Robot", group = "FGC")

public class Robot_FGC extends LinearOpMode {

    private Hardware_FGC RB = new Hardware_FGC();
    private double LeftP ;
    private double RightP;

    // varibales
    public boolean a_press = false;

    @Override
    public void runOpMode() {
        RB.init(hardwareMap);
        LeftP  =  gamepad1.left_stick_y  * 0.25;
        RightP =  gamepad1.right_stick_y * 0.25;

        waitForStart();
        while (opModeIsActive()){

            Movement();

            Intake();

        }

    }


    private void Movement(){
        RB.FLeft.setPower(LeftP);
        RB.FRight.setPower(RightP);
        RB.BLeft.setPower(LeftP);
        RB.BRight.setPower(RightP);

    }

    private void Intake(){
        if(!a_press){
            if (gamepad1.right_trigger > 0){
                RB.Intake.setPower(1);
            }else if (gamepad1.left_trigger >0){
                RB.Intake.setPower(-1);
            }else{
                RB.Intake.setPower(0);
            }
        }

        if (gamepad1.a) {
            RB.Intake.setPower(1);
            a_press = true;
            telemetry.addData("Conveyor Status" , "ON");
            telemetry.update();
        }else if (gamepad1.b){
            RB.Intake.setPower(0);
            a_press = false;
            telemetry.addData("Conveyor Status" , "OFF");
            telemetry.update();
        }


    }
}
