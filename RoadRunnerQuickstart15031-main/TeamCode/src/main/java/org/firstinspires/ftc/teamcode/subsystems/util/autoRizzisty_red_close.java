package org.firstinspires.ftc.teamcode.subsystems.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.collection;
import org.firstinspires.ftc.teamcode.subsystems.delivery;
import org.firstinspires.ftc.teamcode.subsystems.drive;

@Autonomous
@Disabled

public class autoRizzisty_red_close extends LinearOpMode{

     @Override
     public void runOpMode(){

          drive drive = new drive(hardwareMap);
          collection bf = new collection(hardwareMap);
          delivery gf = new delivery(hardwareMap);


          waitForStart();
          if (isStopRequested()) return;
          while (opModeIsActive())
          {
               drive.drive(.2, .2, .2, .2);
               sleep(1000);
               drive.drive(0,0,0,0);
               sleep(1000);
               drive.drive(.2,-.2,-.2,.2);
               sleep(2000);
               drive.drive(0,0,0,0);
               sleep(26000);

          }

     }


}


