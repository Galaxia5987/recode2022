package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.bits.RunAllBits;
import frc.robot.valuetuner.WebConstant;

import java.util.HashMap;
import java.util.Objects;

public final class Autonomous {
    private static final WebConstant autoId = WebConstant.of("Autonomous", "Id", 0);

    private static final String[] placeNumberToName = {
            "Bottom",
            "Middle",
            "Top"
    };
    private static final String[] numBallsToString = {
            "Zero",
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six"
    };
    private static final HashMap<String, AutoCommand> nameToCommand = new HashMap<>();

    public static String autoIdToName(int id) {
        if (id == 0) {
            return "RunAllBits";
        }
        int place = id / 10, numBalls = id % 10;
        return placeNumberToName[place - 1] + numBallsToString[numBalls];
    }

    public static Command getAutoCommand(String name) {
        AutoCommand command = nameToCommand.get(name);
        if (Objects.isNull(command))
            return new RunAllBits();
        else
            return command;
    }

    public static Command get() {
        return getAutoCommand(autoIdToName((int) autoId.get()));
    }

    public static class AutoCommand extends SequentialCommandGroup {

        public AutoCommand(String name) {
            nameToCommand.put(name, this);
        }
    }
}
