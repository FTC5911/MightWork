 /*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import org.firstinspires.ftc.teamcode.subsystems.collection;
import org.firstinspires.ftc.teamcode.subsystems.delivery;
import org.firstinspires.ftc.teamcode.subsystems.drive;

/*
 * This sample demonstrates how to run analysis during INIT
 * and then snapshot that value for later use when the START
 * command is issued. The pipeline is re-used from SkystoneDeterminationExample
 */
@Autonomous
public class Gyatto_blue_far extends LinearOpMode
{

    OpenCvWebcam webcam;
    Pipeline_new_camera_blue.SkystoneDeterminationPipeline pipeline_new_camera;
    Pipeline_new_camera_blue.SkystoneDeterminationPipeline.SkystonePosition snapshotAnalysis = Pipeline_new_camera_blue.SkystoneDeterminationPipeline.SkystonePosition.LEFT; // default

    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;


    private ElapsedTime runtime = new ElapsedTime();

    // Calculate the COUNTS_PER_INCH for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double COUNTS_PER_MOTOR_REV = 384.5;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // No External Gearing.
    static final double WHEEL_DIAMETER_INCHES = 3.77953;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.4;
    static final double TURN_SPEED = 0.5;


    @Override
    public void runOpMode()
    {

        collection bf = new collection(hardwareMap);
        delivery gf = new delivery(hardwareMap);

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");


        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);


        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        /**
         * NOTE: Many comments have been omitted from this sample for the
         * sake of conciseness. If you're just starting out with EasyOpenCv,
         * you should take a look at {@link InternalCamera1Example} or its
         * webcam counterpart, {@link WebcamExample} first.
         */


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline_new_camera = new Pipeline_new_camera_blue.SkystoneDeterminationPipeline();
        webcam.setPipeline(pipeline_new_camera);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(2560,1472, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}


        });

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            telemetry.addData("Realtime analysis", pipeline_new_camera.getAnalysis());
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }

        /*
         * The START command just came in: snapshot the current analysis now
         * for later use. We must do this because the analysis will continue
         * to change as the camera view changes once the robot starts moving!
         */
        snapshotAnalysis = pipeline_new_camera.getAnalysis();

        /*
         * Show that snapshot on the telemetry
         */
        telemetry.addData("Snapshot post-START analysis", snapshotAnalysis);
        telemetry.update();

        switch (snapshotAnalysis)
        {
            case LEFT:
            {
                bf.close_pixie();
                forward(DRIVE_SPEED,-21,-21 ,5);
                forward(DRIVE_SPEED,-20,20,5);
                forward(DRIVE_SPEED,-8.5,-8.5,5);
                bf.open_pixie();
                forward(DRIVE_SPEED,7,7,5);
                forward(DRIVE_SPEED,-3, 3,5);
                Strafe(DRIVE_SPEED,-30,-30,5);
                forward(DRIVE_SPEED,-80,-80,5);
                Strafe(DRIVE_SPEED,20,20,5);
                forward(DRIVE_SPEED,-8,-8,5);
                gf.retract(1);
                sleep(750);
                gf.stall();
                sleep(1000);
                gf.dump();
                sleep(1000);
                gf.gyat();
                sleep(1000);
                gf.extend(.4);
                sleep(1000);
                gf.stall();
                forward(DRIVE_SPEED,9,9,5);
                Strafe(DRIVE_SPEED,-30,-30,5);

                break;
            }

            case RIGHT:
            {
                bf.close_pixie();
                sleep(5000);
                //Strafe(DRIVE_SPEED,5,5,5);
                forward(DRIVE_SPEED,-24,-24 ,5);
                forward(DRIVE_SPEED,20.5,-20.5,5);
                forward(DRIVE_SPEED,-3,-3,5);
                sleep(500);
                bf.open_pixie();
                sleep(500);
                forward(DRIVE_SPEED,-1,1,5);
                sleep(500);
                forward(DRIVE_SPEED,4,4,5);
                Strafe(DRIVE_SPEED,30,30,5);
                forward(DRIVE_SPEED,75,75,5);
                Strafe(DRIVE_SPEED,-27.5,-27.5,5);
                forward(DRIVE_SPEED,39,-39,5);
                forward(DRIVE_SPEED,-10,-10,5);
                gf.retract(1);
                sleep(750);
                gf.stall();
                sleep(500);
                gf.dump();
                sleep(1000);
                gf.gyat();
                sleep(1000);
                gf.extend(1);
                sleep(750);
                forward(DRIVE_SPEED,5,5,5);
                Strafe(DRIVE_SPEED,-23,-23,5);
                forward(DRIVE_SPEED,-15,-15,5);



                break;
            }

            case CENTER:
            {
                bf.close_pixie();
                sleep(5000);
                forward(DRIVE_SPEED,-27,-27,5);
                bf.open_pixie();
                sleep(1000);
                forward(DRIVE_SPEED,10,10,5);
                Strafe(DRIVE_SPEED, -20, -20, 5);
                forward(DRIVE_SPEED, -33.5,-33.5,5);
                forward(TURN_SPEED,-20,20,5);
                forward(DRIVE_SPEED,-75,-75,5);
                Strafe(DRIVE_SPEED,30.5,30.5,5);
                forward(DRIVE_SPEED,-27,-27,5);
                gf.retract(1);
                sleep(800);
                gf.stall();
                gf.dump();
                sleep(1500);
                gf.gyat();
                sleep(1000);
                gf.extend(1);
                sleep(600);
                gf.stall();
                forward(DRIVE_SPEED,10,10,5);
                Strafe(DRIVE_SPEED,-28.5,-28.5,5);
                forward(DRIVE_SPEED,-15,-15,5);
                break;
            }

        }

        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive())
        {
            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }
    } public void forward(double speed,
                          double leftInches, double rightInches,
                          double timeoutS) {
    int newFrontRightTarget;
    int newFrontLeftTarget;
    int newBackLeftTarget;
    int newBackRightTarget;

    // Ensure that the OpMode is still active
    if (opModeIsActive()) {

        // Determine new target position, and pass to motor controller
        newFrontLeftTarget = frontLeft.getCurrentPosition() - (int) (leftInches * COUNTS_PER_INCH);
        newFrontRightTarget = frontRight.getCurrentPosition() - (int) (rightInches * COUNTS_PER_INCH);
        newBackLeftTarget = backLeft.getCurrentPosition() - (int) (leftInches * COUNTS_PER_INCH);
        newBackRightTarget = backRight.getCurrentPosition() - (int) (rightInches * COUNTS_PER_INCH);
        frontLeft.setTargetPosition(newFrontLeftTarget);
        frontRight.setTargetPosition(newFrontRightTarget);
        backLeft.setTargetPosition(newBackLeftTarget);
        backRight.setTargetPosition(newBackRightTarget);

        // Turn On RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        // reset the timeout time and start motion.
        runtime.reset();
        frontLeft.setPower(Math.abs(speed));
        frontRight.setPower(Math.abs(speed * 0.95));
        backLeft.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed * 0.95));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Running to", " %7d :%7d", newFrontLeftTarget, newFrontRightTarget,
                    newBackLeftTarget, newBackRightTarget);
            telemetry.addData("Currently at", " at %7d :%7d",
                    frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
                    backLeft.getCurrentPosition(), backRight.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion;
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        sleep(250);   // optional pause after each move.
    }
} public void Strafe(double speed,
                     double LeftInches, double RightInches,
                     double TimeoutS) {
    int NewFrontRightTarget;
    int NewFrontLeftTarget;
    int NewBackLeftTarget;
    int NewBackRightTarget;

    // Ensure that the OpMode is still active
    if (opModeIsActive()) {

        // Determine new target position, and pass to motor controller
        NewFrontLeftTarget = frontLeft.getCurrentPosition() - (int) (LeftInches * COUNTS_PER_INCH);
        NewFrontRightTarget = frontRight.getCurrentPosition() + (int) (RightInches * COUNTS_PER_INCH);
        NewBackLeftTarget = backLeft.getCurrentPosition() + (int) (LeftInches * COUNTS_PER_INCH);
        NewBackRightTarget = backRight.getCurrentPosition() - (int) (RightInches * COUNTS_PER_INCH);
        frontLeft.setTargetPosition(NewFrontLeftTarget);
        frontRight.setTargetPosition(NewFrontRightTarget);
        backLeft.setTargetPosition(NewBackLeftTarget);
        backRight.setTargetPosition(NewBackRightTarget);

        // Turn On RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        // reset the timeout time and start motion.
        runtime.reset();
        frontLeft.setPower(Math.abs(speed));
        frontRight.setPower(Math.abs(speed * 0.95));
        backLeft.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed * 0.95));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        while (opModeIsActive() &&
                (runtime.seconds() < TimeoutS) &&
                (frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Running to", " %7d :%7d", NewFrontLeftTarget, NewFrontRightTarget,
                    NewBackLeftTarget, NewBackRightTarget);
            telemetry.addData("Currently at", " at %7d :%7d",
                    frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
                    backLeft.getCurrentPosition(), backRight.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion;
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        sleep(250);   // optional pause after each move.
    }
}
}
