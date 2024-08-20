package src.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Logs {
    public static void writeLog(String username, String operation, String table){
        try (FileWriter fileWriter = new FileWriter("../Logs.txt", true)) {

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            fileWriter.write(username + " : " + operation + table + " " + timestamp);
            fileWriter.write(System.lineSeparator());
            fileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
