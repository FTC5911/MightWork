package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.arcrobotics.ftclib.controller.PIDController;


public class collection {


    public collection(HardwareMap hardwareMap) {
        double p = 0.2;
        double i = 0.0;
        double d = 0.005;


        intake = hardwareMap.get(DcMotor.class, "Intake");
        pixie = hardwareMap.get(Servo.class, "pixie");
       // intakeSpin = hardwareMap.get(DcMotor.class, "intakeSpin");



        //intakeSpin.setDirection(DcMotorSimple.Direction.FORWARD);



        //controller = new PIDController(p, i, d);

    }

    private DcMotor intake;
    //private DcMotor intakeSpin;

    private Servo pixie;
    //private PIDController controller;




    /*public void dont_move() {
        intakeSpin.setPower(0.1);
    }*/



    public void grab_pixel() {
        intake.setPower(-0.85);

    }

    public void reverse_intake() {
        intake.setPower(0.85);


    }

    /*public void spin_up() {
        intakeSpin.setPower(.5);


    }*/

    /*public void spin_down() {
        intakeSpin.setPower(-.5);


    }*/

    /*public void no_y() {
        intakeSpin.setPower(0);

    }*/

    public void no_feed() {
        intake.setPower(0);

    } public void close_pixie(){
        pixie.setPosition(0);
    } public void open_pixie(){
        pixie.setPosition(1);
    } public void middle_pixie(){
        pixie.setPosition(0);
    }
    /*public void moveToStage(String stage) {

        int slidePosition = intakeSpin.getCurrentPosition();

        int Position[] = {500, 1350};
        String Stage[] = {"GROUND", "LOW"};

        int stageIndex = Byte.MAX_VALUE;
        int res = 0;


        for (int i = 0; i < Stage.length; i++) {

            if (Stage[i].equals(stage)) {
                stageIndex = i;
                break;
            }
        }

        if (stageIndex != Byte.MAX_VALUE) {

            res = Position[stageIndex];
            double pid = controller.calculate(slidePosition, res);

            int error = res - slidePosition;

            double power = pid + 0.1;

            if (Math.abs(error) > 0) {

                //liftRight.setPower(power);
                intakeSpin.setPower(power);

            } else {

                dont_move();

            }



        }*/
    }



