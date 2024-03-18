package org.firstinspires.ftc.teamcode.subsystems.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.teamcode.subsystems.LEDs;
import org.firstinspires.ftc.teamcode.subsystems.drive;
import org.firstinspires.ftc.teamcode.subsystems.delivery;
import org.firstinspires.ftc.teamcode.subsystems.collection;

import java.sql.Timestamp;

@Autonomous
@Disabled
public class autoRizzisty extends LinearOpMode{

     @Override
     public void runOpMode(){

          drive drive = new drive(hardwareMap);
          collection bf = new collection(hardwareMap);
          delivery gf = new delivery(hardwareMap);


          waitForStart();
          if (isStopRequested()) return;
          while (opModeIsActive())
          {
               drive.drive(1, 1, 1, 1);
               sleep(1000);
               drive.drive(0,0,0,0);
               sleep(1000);


          }

     }


}


