package chessagents.agents.analysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GameLoggerImpl implements GameLogger {

    private static final String LOG_FILE_PATH = "experiment.csv";

    public GameLoggerImpl() {
        try {
            clearLogfile();
            writeToLogfile(buildHeaderString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearLogfile() {

    }

    private String buildHeaderString() {
        return "source,target,fen";
    }

    @Override
    public void logMoveAndState(String from, String to, String fenBeforeMove) {
        try {
            writeToLogfile(buildRowString(from, to, fenBeforeMove));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToLogfile(String line) throws IOException {
        var fw = new FileWriter(LOG_FILE_PATH, true);
        var bw = new BufferedWriter(fw);
        bw.newLine();
        bw.write(line);
        bw.close();
    }

    private String buildRowString(String from, String to, String fenBeforeMove) {
        return String.format("%s,%s,%s", from, to, fenBeforeMove);
    }
}
