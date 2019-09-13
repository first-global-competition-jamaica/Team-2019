package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp (name = "FGC Robot", group = "FGC")

public class Robot_FGC extends LinearOpMode {

    private Hardware_FGC RB = new Hardware_FGC();
    private ElapsedTime runtime = new ElapsedTime();

    private double LeftP;
    private double RightP;
    boolean a_press = false;
    double multMax = 0.75;
    double multMed = 0.5;
    double multMin = 0.25;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        RB.init(hardwareMap);
        runtime.reset();


        waitForStart();
        while (opModeIsActive()) {

            Movement();

            Intake();

            VariableSpeed();

            //Linearslide();

            //Lift();

        }
    }


    private void Movement() {
        LeftP = gamepad1.left_stick_y * multMin;
        RightP = gamepad1.right_stick_y * multMin;
        RB.FLeft.setPower(LeftP);
        RB.FRight.setPower(RightP);
        RB.BLeft.setPower(LeftP);
        RB.BRight.setPower(RightP);

    }


    private void Intake() {
        // code for the button action

        if (!a_press) {
//            if (gamepad1.right_trigger > 0){
//                RB.Intake.setPower(1);
//            }else if (gamepad1.left_trigger >0){
//                RB.Intake.setPower(-1);
//            }else{
//                RB.Intake.setPower(0);
//            }

            //code to control the rate of the intake
            if (gamepad1.right_trigger > 0) {
                RB.Intake.setPower(gamepad1.right_trigger);
            } else if (gamepad1.left_trigger > 0) {
                RB.Intake.setPower(-gamepad1.left_trigger);
            } else {
                RB.Intake.setPower(0);
            }
        }

        // code for the switching action for the conveyor
        if (gamepad1.a) {
            RB.Intake.setPower(1);
            a_press = true;
            telemetry.addData("Conveyor Status", "ON");
            telemetry.update();
        }
        if (gamepad1.b) {
            RB.Intake.setPower(0);
            a_press = false;
            telemetry.addData("Conveyor Status", "OFF");
            telemetry.update();
        }

    }


    //this object contains the code for varying the speed of the
    //motor at the press of a bot

    private void VariableSpeed() {

        if( gamepad1.x = true){

            RB.FLeft.setPower(multMed);
            RB.FRight.setPower(multMed);
            RB.BLeft.setPower(multMed);
            RB.BRight.setPower(multMed);
            }
        else if(gamepad1.y = true){

            RB.FLeft.setPower(multMax);
            RB.FRight.setPower(multMax);
            RB.BLeft.setPower(multMax);
            RB.BRight.setPower(multMax);

            }





















    }
}
