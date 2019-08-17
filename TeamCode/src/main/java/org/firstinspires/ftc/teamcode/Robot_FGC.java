package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp (name = "FGC Robot", group = "FGC")

public class Robot_FGC extends LinearOpMode {

    private Hardware_FGC RB = new Hardware_FGC();
    private double LeftP ;
    private double RightP;

    @Override
    public void runOpMode() {
        RB.init(hardwareMap);


        waitForStart();
        while (opModeIsActive()){
            LeftP  =  gamepad1.left_stick_y  * 0.25;
            RightP =  gamepad1.right_stick_y * 0.25;
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
        if (gamepad1.right_trigger > 0){
            RB.Intake.setPower(gamepad1.right_trigger);
        }
            else if (gamepad1.left_trigger >0){
                RB.Intake.setPower(-gamepad1.left_trigger);
        }


        if (gamepad1.a) {
            RB.Intake.setPower(1);
            telemetry.addData("Conveyor Status" , "ON");
            telemetry.update();
        }
            else if (gamepad1.b){
                RB.Intake.setPower(0);
                telemetry.addData("Conveyor Status" , "OFF");
                telemetry.update();
        }





    }
}
