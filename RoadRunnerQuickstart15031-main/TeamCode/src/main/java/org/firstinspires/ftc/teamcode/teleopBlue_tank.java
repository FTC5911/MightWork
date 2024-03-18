package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.LEDs;
import org.firstinspires.ftc.teamcode.subsystems.collection;
import org.firstinspires.ftc.teamcode.subsystems.delivery;
import org.firstinspires.ftc.teamcode.subsystems.drive;

@Disabled
@TeleOp

public class teleopBlue_tank extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private ColorSensor colorSensor;
    private ColorSensor colorSensor2;


    String stage = "GROUND";
    //String hang_stage = "GROUND";

    public void runOpMode() throws InterruptedException {

        drive drive = new drive(hardwareMap);
        collection intake = new collection(hardwareMap);
        delivery slides = new delivery(hardwareMap);
        LEDs lusp = new LEDs(hardwareMap);

        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        colorSensor2 = hardwareMap.colorSensor.get("colorSensor2");

        boolean intakeToggle = false;
        boolean intakeToggle2 = false;
        boolean intakeToggle3 = false;
        boolean intakeToggle4 = false;

        int i = 0;
        int s = 0;

        //int ki = 0;
        //int ks = 0;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();

        Gamepad previousGamepad1 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        slides.dump();
        slides.reload();

        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {


            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            double pFrontRight = (y + x + rx) / denominator;
            double pBackRight = (y - x + rx) / denominator;
            double pFrontLeft = (y - x - rx) / denominator;
            double pBackLeft = (y + x - rx) / denominator;

            double right = gamepad1.right_stick_y;
            double left = gamepad1.left_stick_y;

            final double fallSpeed = 0.8;
            final double liftSpeed = 1;

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);


            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            if (gamepad1.right_stick_button) {
                drive.drive(0.7, -0.7,
                        - 0.7, 0.7);
            } else if (gamepad1.left_stick_button) {
                drive.drive(-0.7, 0.7,
                        0.7, -0.7);
            } else {
                drive.drive(right, right, left, left);
            }


            if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
                intakeToggle = !intakeToggle;
            }

           /* if (intakeToggle) {
                slides.ready_to_go();
            } else {
                slides.back_to_sleep();
            }*/


           /* if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
                intakeToggle2 = !intakeToggle2;
            }

            if (intakeToggle2) {
                slides.close_the_gates();
            } else {
                slides.the_gates();*/


                if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right) {
                    intakeToggle3 = !intakeToggle3;
                }
                if (intakeToggle3) {
                    slides.back_to_start();
                } else {
                    slides.dump();
                }

                if (currentGamepad1.square && !previousGamepad1.square) {
                    intakeToggle4 = !intakeToggle4;
                }
                if (intakeToggle4) {
                    slides.launch();
                } else {
                    slides.reload();
                }


                if (gamepad2.x) {
                    slides.lower_hang();
                } else if (gamepad2.triangle) {
                    slides.initiate_hang();
                } else {
                    slides.stop_hang();
                }


                if (gamepad2.dpad_up) {
                    slides.extend(liftSpeed);
                    s = 0;

                } else if (gamepad2.dpad_down) {
                    slides.retract(fallSpeed);
                    s = 0;

                } else if (s == 1) {
                    slides.moveToStage(stage);
                } else {
                    slides.stall();
                }
                if (gamepad1.right_trigger > 0.1) {
                    intake.grab_pixel();

                } else if (gamepad1.left_trigger > 0.1) {
                    intake.reverse_intake();

                } else {
                    intake.no_feed();
                }
                /*if (gamepad2.right_bumper) {
                    intake.spin_up();
                } else if (gamepad2.left_bumper) {
                    intake.spin_down();

                } else {
                    intake.dont_move();

                }*/
                resetRuntime();


                if (runtime.seconds() > 80 && runtime.seconds() < 90) {

                    lusp.flash("yellow", "purple", runtime);

                } else {


                    if (((DistanceSensor) colorSensor).getDistance(DistanceUnit.MM) < 35 ^
                            ((DistanceSensor) colorSensor2).getDistance(DistanceUnit.MM) < 52) {

                        lusp.setColor("yellow");

                        if (i == 0) {

                            gamepad1.rumble(500);
                            gamepad2.rumble(500);
                            ++i;

                        }


                    } else if (((DistanceSensor) colorSensor).getDistance(DistanceUnit.MM) < 35 &&
                            ((DistanceSensor) colorSensor2).getDistance(DistanceUnit.MM) < 52) {

                        lusp.setColor("green");
                        if (i == 1) {

                            gamepad1.rumble(500);
                            gamepad2.rumble(500);
                            ++i;

                        }
                    } else {

                        lusp.setColor("dark blue");
                        i = 0;

                    }
                }

            }
        }
    }

