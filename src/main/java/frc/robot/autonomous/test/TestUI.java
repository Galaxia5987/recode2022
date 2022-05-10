package frc.robot.autonomous.test;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.RobotContainer;

public class TestUI extends SequentialCommandGroup {

    public TestUI(boolean testChassis) {
        if (testChassis) {
            addCommands(
                    new WaitUntilCommand(() -> RobotContainer.getInstance().chassisGetA())
                            .raceWith(new RunCommand(() -> System.out.println("Waiting for chassis A button"))),
                    new WaitUntilCommand(() -> RobotContainer.getInstance().chassisGetB())
                            .raceWith(new RunCommand(() -> System.out.println("Waiting for chassis B button"))),
                    new WaitUntilCommand(() -> RobotContainer.getInstance().chassisGetX())
                            .raceWith(new RunCommand(() -> System.out.println("Waiting for chassis X button"))),
                    new WaitUntilCommand(() -> RobotContainer.getInstance().chassisGetY())
                            .raceWith(new RunCommand(() -> System.out.println("Waiting for chassis Y button"))),
                    new RunCommand(() -> System.out.println("All inputs received."))
                            .withTimeout(3)
            );
        } else {
            addCommands(
                    new WaitUntilCommand(() -> RobotContainer.getInstance().climberGetA())
                            .raceWith(new RunCommand(() -> System.out.println("Waiting for climber A button"))),
                    new WaitUntilCommand(() -> RobotContainer.getInstance().climberGetB())
                            .raceWith(new RunCommand(() -> System.out.println("Waiting for climber B button"))),
                    new WaitUntilCommand(() -> RobotContainer.getInstance().climberGetX())
                            .raceWith(new RunCommand(() -> System.out.println("Waiting for climber X button"))),
                    new WaitUntilCommand(() -> RobotContainer.getInstance().climberGetY())
                            .raceWith(new RunCommand(() -> System.out.println("Waiting for climber Y button"))),
                    new RunCommand(() -> System.out.println("All inputs received."))
                            .withTimeout(3)
            );
        }
    }
}
