package model;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import no.hiof.andrekar.badhabits.MainActivity;

public class SaveData {

    public static JSONObject readFromJson(File file) {
        JSONObject json = null;
        try {
            InputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String myJson = new String(buffer, "UTF-8");
            json = new JSONObject(myJson);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public void saveToFile(Habit habit, String fileName) {

        //TODO: Read existing data from file

        try {
            File targetDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if(!targetDir.exists())
                targetDir.mkdirs();

            File outFile = new File(targetDir, fileName);
            FileWriter fileWriter = new FileWriter(outFile, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            //writer.write(habit.getTitle());
            //writer.write(habit.getDescription() + "\n");
            //writer.write(habit.getStartDate().toString()+ "\n\n");
            //writer.flush();
            //writer.close();
            //fileWriter.close();

            //test habit
            JSONObject jsonObject = toJSon(new Habit(habit.getTitle(),habit.getDescription(),new Date()));

            Writer output;
            outFile = new File(targetDir, "testJ.json");
            output = new BufferedWriter(new FileWriter(outFile));
            output.write(jsonObject.toString());
            output.close();
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

    public JSONObject toJSon(Habit habit){
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("title",habit.getTitle());
            jsonObject.put("description",habit.getDescription());
            //jsonObject.put("favourite", habit.getIsFavourite());
            //jsonObject.put("startDate", habit.getStartDate());

        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject readJSONFile(Context context){
        JSONObject json = null;
        try {
            InputStream is = context.getAssets().open("testJ.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String myJson = new String(buffer, "UTF-8");
            json = new JSONObject(myJson);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
