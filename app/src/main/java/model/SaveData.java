package model;

import android.location.Location;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SaveData {


    public void saveToFile( Habit habit, String fileName) {
        try {
            File targetDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if(!targetDir.exists())
                targetDir.mkdirs();

            File outFile = new File(targetDir, fileName);
            FileWriter fileWriter = new FileWriter(outFile, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(habit.getTitle());
            writer.write(habit.getDescription() + "\n");
            writer.write(habit.getStartDate().toString()+ "\n\n");

            writer.flush();
            writer.close();
            fileWriter.close();
        }
        catch (Exception ex){
            Log.e("saveData.saveToFile", ex.getMessage());
        }

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
