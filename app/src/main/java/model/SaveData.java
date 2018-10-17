package model;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import no.hiof.andrekar.badhabits.MainActivity;

public class SaveData {
    Habit h;
    String filename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/TestGSon";

    public void readFromFile() {
        //Create a new Gson object
        Gson gson = new Gson();
        Habit.habits.clear();



        try {
        //Read the employee.json file
            BufferedReader br = new BufferedReader(
                new FileReader(filename+"Date.txt"));

            //convert the json to  Java object (Employee)

            Type collectionType = new TypeToken<ArrayList<DateHabit>>(){}.getType();
            ArrayList<DateHabit> habitsF = gson.fromJson(br, collectionType);
            for (int i = 0; i < habitsF.size(); i++ ) {

                    Habit habit = (DateHabit) habitsF.get(i);
                    Habit.habits.add(habit);
                    DateHabit.dateHabits.add((DateHabit) habit);
                }
            }
            catch (IOException e)
        {
            e.printStackTrace();
        }

        try {
            //Read the employee.json file
            BufferedReader br = new BufferedReader(
                    new FileReader(filename+"Eco.txt"));

            //convert the json to  Java object (Employee)

            Type collectionType = new TypeToken<ArrayList<EconomicHabit>>(){}.getType();
            ArrayList<EconomicHabit> habitsF = gson.fromJson(br, collectionType);
            for (int i = 0; i < habitsF.size(); i++ ) {

                Habit habit = (EconomicHabit) habitsF.get(i);
                Habit.habits.add(habit);
                EconomicHabit.ecohabits.add((EconomicHabit) habit);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
}

    public void saveToFile(Habit habit, int typeHabit) {
        try {
            // Create a new Gson object
            Gson gson = new Gson();
            Habit.habits.add(habit);

            //convert the Java object to json
            //Write JSON String to file
            if(typeHabit == 1) {

                EconomicHabit.ecohabits.add((EconomicHabit) habit);
                String jsonString = gson.toJson(EconomicHabit.ecohabits);

                FileWriter fileWriter = new FileWriter(filename + "Eco.txt");
                fileWriter.write(jsonString);
                fileWriter.close();
                EconomicHabit.ecohabits.clear();
            } else if (typeHabit == 2) {

                DateHabit.dateHabits.add((DateHabit) habit);
                String jsonString = gson.toJson(DateHabit.dateHabits);

                FileWriter fileWriter = new FileWriter(filename+"Date.txt");
                fileWriter.write(jsonString);
                fileWriter.close();
                DateHabit.dateHabits.clear();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateData(int typeHabit) {
        Gson gson = new Gson();

        try {
            if (typeHabit == 1) {
                String jsonString = gson.toJson(EconomicHabit.ecohabits);
                FileWriter fileWriter = new FileWriter(filename + "Eco.txt");
                fileWriter.write(jsonString);
                fileWriter.close();
            } else if (typeHabit == 2) {
                String jsonString = gson.toJson(DateHabit.dateHabits);
                FileWriter fileWriter = new FileWriter(filename + "Date.txt");
                fileWriter.write(jsonString);
                fileWriter.close();
            }
        } catch (Exception e) {
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
}
