package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.teamcode.subsystems.LEDs;
import org.firstinspires.ftc.teamcode.subsystems.drive;
import org.firstinspires.ftc.teamcode.subsystems.delivery;
import org.firstinspires.ftc.teamcode.subsystems.collection;

import java.sql.Timestamp;

@TeleOp

public class teleopBlue extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private ColorSensor fanum;
    private ColorSensor tax;

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

    private LinearOpMode myOpMode = null;

    String stage = "GROUND";
    //String hang_stage = "GROUND";


    public void runOpMode() throws InterruptedException {

        drive drive = new drive(hardwareMap);
        collection intake = new collection(hardwareMap);
        delivery slides = new delivery(hardwareMap);
        LEDs lusp = new LEDs(hardwareMap);

        fanum = hardwareMap.colorSensor.get("fanum");
        tax = hardwareMap.colorSensor.get("tax");

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        boolean intakeToggle = false;
        boolean intakeToggle2 = false;
        boolean intakeToggle3 = false;
        boolean intakeToggle4 = false;
        boolean intakeToggle5 = false;
        boolean intakeToggle6 = false;

        int i = 0;
        int s = 0;

        int Fanum = 0;
        int LEDPaint = 0;

        //int ki = 0;
        //int ks = 0;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        slides.back_to_start();
        slides.reload();
        intake.close_pixie();


        class LEDs {

            private LinearOpMode myOpMode = null;

            public LEDs(HardwareMap hardwareMap) {

                lusp = hardwareMap.get(Servo.class, "lusp");

            }


            private Servo lusp;


            public void setColor(String Color) {

                String color[] = {"green", "red", "rainbow", "blue", "purple", "dark blue", "pink", "white", "yellow", "djrainbow", "breathing_blue"};
                double colorID[] = {0.71, 0.67, 0.22, 0.65, 0.75, 0.73, 0.66, 0.76, 0.69, 0.31, 0.46};

                int colorIndex = Byte.MAX_VALUE;
                double res = 0;

                for (int Fanum = 0; Fanum < color.length; Fanum++) {

                    if (color[Fanum].equals(Color)) {
                        colorIndex = Fanum;
                        break;
                    }
                }

                if (colorIndex != Byte.MAX_VALUE) {

                    res = colorID[colorIndex];
                    lusp.setPosition(res);

                }
            }

            public void flash(String color1, String color2, ElapsedTime runTime) {

                if ((int) runTime.seconds() % 2 != 0) {
                    setColor(color1);

                } else {

                    setColor(color2);
                }

            }

        }


        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {


            double rx = (gamepad1.left_stick_y);
            double x = (gamepad1.left_stick_x * 1);
            double y = (gamepad1.right_stick_x);

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            double pFrontRight = (y + x + rx) / denominator;
            double pBackRight = (y - x + rx) / denominator;
            double pFrontLeft = (y + x - rx) / denominator;
            double pBackLeft = (y - x - rx) / denominator;

            //frontLeft.setPower(pFrontLeft * 1);
            //frontRight.setPower(pFrontRight * 1);
            //backLeft.setPower(pBackLeft * 1);
            //backRight.setPower(pBackRight * 1);


            final double fallSpeed = 1;
            final double liftSpeed = 1;

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);


            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            if (gamepad1.right_stick_button) {
                drive.drive(pFrontRight + 0.5, pBackRight - 0.5,
                        pFrontLeft - 0.5, pBackLeft + 0.5);
            } else if (gamepad1.left_stick_button) {
                drive.drive(pFrontRight - 0.5, pBackRight + 0.5,
                        pFrontLeft + 0.5, pBackLeft - 0.5);
            } else {
                drive.drive(pFrontRight * 0.85, pBackRight * 0.85, pFrontLeft, pBackLeft);

            }


            //else {
            // frontLeft.setPower(pFrontLeft * 1);
            //frontRight.setPower(pFrontRight * 1);
            //backRight.setPower(pBackRight * 1);
            //backLeft.setPower(pBackLeft * 1);
            //}

            /*if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
                intakeToggle = !intakeToggle;
            }

            if (intakeToggle) {
                slides.ready_to_go();
            } else {
                slides.back_to_sleep();
            }*/




                /*if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right) {
                    intakeToggle3 = !intakeToggle3;
                }
                if (intakeToggle3) {
                    slides.dump();
                } else if (gamepad2.dpad_left){
                    slides.gyat();
                }else {
                    slides.back_to_start();
                }*/


                /*if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right) {
                    intakeToggle3 = !intakeToggle3;
                }
                if (intakeToggle3) {
                    slides.dump();
                } else if (gamepad2.dpad_left){
                    slides.back_to_start();
                }*/
            if (gamepad1.left_trigger > 0.1) {
                slides.dump();
            } else if (gamepad1.right_trigger > 0.1) {
                slides.back_to_start();
            }


            if (currentGamepad1.square && !previousGamepad1.square) {
                intakeToggle4 = !intakeToggle4;
            }
            if (intakeToggle4) {
                slides.launch();
            } else {
                slides.reload();
            }



                /*if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right) {
                    intakeToggle5 = !intakeToggle5;
                    intakeToggle6 = !intakeToggle6;
                } else if (!intakeToggle5) {
                lusp.setColor("blue");
                } else if (intake)*/


            if (gamepad2.x) {
                slides.lower_hang();
            } else if (gamepad2.triangle) {
                slides.initiate_hang();
            } else {
                slides.stop_hang();
            }


            if (gamepad2.dpad_up) {
                slides.retract(liftSpeed);
                s = 0;

            } else if (gamepad2.dpad_down) {
                slides.extend(fallSpeed);
                s = 0;

            } else if (s == 1) {
                slides.moveToStage(stage);
            } else {
                slides.stall();
            }
            if (gamepad2.right_trigger > 0.1
            ) {
                intake.grab_pixel();

            } else if (gamepad2.left_trigger > 0.1) {
                intake.reverse_intake();

            } else {
                intake.no_feed();
            }
            if (gamepad1.dpad_left) {
                intake.open_pixie();
            } else if (gamepad1.dpad_right) {
                intake.close_pixie();
            }

                /*if (gamepad2.right_bumper) {
                    intake.spin_up();
                } else if (gamepad2.left_bumper) {
                    intake.spin_down();

                } else {
                    intake.dont_move();

                }*/
            resetRuntime();


            if (runtime.seconds() >= 80 && runtime.seconds() <= 90) {

                lusp.flash("yellow", "purple", runtime);

            } else {


                if (gamepad2.right_bumper) {
                    LEDPaint = LEDPaint + 1;
                } else if (LEDPaint == 1) {
                    lusp.setColor("yellow");
                } else if (LEDPaint == 2) {
                    lusp.setColor("green");
                } else if (LEDPaint == 3) {
                    lusp.setColor("purple");
                } else if (LEDPaint == 4) {
                    lusp.setColor("white");
                }
                if (LEDPaint == 5) {
                    LEDPaint = 1;
                }
            }

            if (gamepad2.left_bumper) {
                LEDPaint = 0;
                while (LEDPaint == 0) {

                    if (((DistanceSensor) fanum).getDistance(DistanceUnit.MM) >= 40 &&
                            ((DistanceSensor) tax).getDistance(DistanceUnit.MM) < 35) {

                        lusp.setColor("purple");

                    } else if (((DistanceSensor) fanum).getDistance(DistanceUnit.MM) < 35 &&
                            ((DistanceSensor) tax).getDistance(DistanceUnit.MM) >= 40) {

                        lusp.setColor("purple");

                    } else if (((DistanceSensor) fanum).getDistance(DistanceUnit.MM) < 35 &&
                            ((DistanceSensor) tax).getDistance(DistanceUnit.MM) < 35) {

                        lusp.setColor("yellow");
                    } else {

                        lusp.setColor("dark blue");


                    }
                    if (gamepad2.circle) {
                        LEDPaint = 1;
                    }
                }
            }
        }
    }
}


