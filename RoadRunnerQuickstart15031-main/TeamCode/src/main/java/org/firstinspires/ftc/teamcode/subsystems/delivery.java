package org.firstinspires.ftc.teamcode.subsystems;


//import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//import com.arcrobotics.ftclib.controller.PIDController;
//import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class delivery {


    public delivery(HardwareMap hardwareMap) {


        double p = 0.2;
        double i = 0.0;
        double d = 0.005;

        //double kp = 0.0;
        //double ki = 0.0;
        //double kd = 0.00091;

        //liftRight = hardwareMap.get(DcMotorEx.class,"liftRight");
        liftLeft = hardwareMap.get(DcMotorEx.class, "liftLeft");

        //liftRight.setDirection(DcMotorSimple.Direction.FORWARD); //this might not be needed; or the left slide should be the one being reversed
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        plane = hardwareMap.get(Servo.class, "plane");
        keith = hardwareMap.get(Servo.class, "keith");
        hang = hardwareMap.get(DcMotor.class, "hang");
        the_gallows = hardwareMap.get(DcMotor.class, "the_gallows");


        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hang.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        the_gallows.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);

       // controller = new PIDController(p, i, d);
       // controller2 = new PIDController(kp, ki, kd);
        //reset();


    }

    //private DcMotor liftRight;
    private DcMotor liftLeft;
    private Servo plane;

    private DcMotor hang;

    private DcMotor the_gallows;
    private Servo keith;





    //private PIDController controller;

    //private PIDController controller2;

    public void extend(double liftSpeed) {

        //liftRight.setPower(Math.abs(liftSpeed));
        liftLeft.setPower(Math.abs(liftSpeed));

    }

    public void retract(double slideSpeed) {

        //liftRight.setPower(-Math.abs(slideSpeed));
        liftLeft.setPower(-Math.abs(slideSpeed));

    }


    public void stall() {

        //liftRight.setPower(0.1);
        liftLeft.setPower(-0.08);

    }

    public void launch() {
        plane.setPosition(0.25);

    }

    public void reload() {
        plane.setPosition(0);

    }

    public void dump() {
        keith.setPosition(0.25);
    }

    public void back_to_start() {
        keith.setPosition(0.55);
    }

    public void gyat() {
        keith.setPosition(0.5);
    }

    public void initiate_hang() {
        hang.setPower(1);
        the_gallows.setPower(1);
    }

    public void lower_hang() {
        hang.setPower(-1);
        the_gallows.setPower(-1);
    }

    public void stop_hang() {
        hang.setPower(0);
        the_gallows.setPower(0);
    }







    public void moveToStage(String stage) {

        int slidePosition = liftLeft.getCurrentPosition();

        int Position[] = {-75, 1000, 1750, 2500};
        String Stage[] = {"GROUND", "LOW", "MID", "HIGH"};

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
            //double pid = controller.calculate(slidePosition, res);

            int error = res - slidePosition;

            double power = 0.7;

            if (Math.abs(error) > 100) {

                //liftRight.setPower(power);
                liftLeft.setPower(power);

            } else {

                stall();

            }


        }
    }

    /*public void stage_hang(String bigstage) {

        int slidePosition = hang.getCurrentPosition();

        int Position[] = {-100, 100};
        String Stage[] = {"GROUND", "LOW"};

        int stageIndex = Byte.MAX_VALUE;
        int res = 0;


        for (int ki = 0; ki < Stage.length; ki++) {

            if (Stage[ki].equals(bigstage)) {
                stageIndex = ki;
                break;
            }
        }

        if (stageIndex != Byte.MAX_VALUE) {

            res = Position[stageIndex];
            double pid = controller2.calculate(slidePosition, res);


            double power = pid + 0;
            hang.setPower(power);


        }


    }*/

    public void reset() {
        hang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }
}