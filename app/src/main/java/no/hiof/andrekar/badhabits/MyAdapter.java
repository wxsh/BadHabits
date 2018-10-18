package no.hiof.andrekar.badhabits;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;

    public MyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.habitName.setText(Habit.habits.get(position).getTitle().toString());
        holder.habitDescription.setText(Habit.habits.get(position).getDescription().toString());

        //TODO: reminder
        if(Habit.habits.get(position).getClass() == EconomicHabit.class){
            //TODO: Matte
            holder.habitGoal.setText(Float.toString(((EconomicHabit)Habit.habits.get(position)).getGoalValue()));
        } else if (Habit.habits.get(position).getClass() == DateHabit.class){
            //TODO: bedre å returne hvor mange dager som gjenstår her kanskje?
            holder.habitGoal.setText(((DateHabit)Habit.habits.get(position)).getDateGoal());
        }

        if (Habit.habits.get(position).getIsFavourite()){
            holder.favoriteButton.setImageResource(R.drawable.star_on);
        }
        else {
            holder.favoriteButton.setImageResource(R.drawable.star_off);
        }


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + Habit.habits.get(position).getTitle());
                ShowHabitActivity.setCurrentNumber(position);
                Intent intent = new Intent(mContext, ShowHabitActivity.class);
                mContext.startActivity(intent);
            }
        });

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton imgB = view.findViewById(R.id.favoriteBtn);

                if (Habit.habits.get(position).getIsFavourite()){
                    Habit.habits.get(position).setFavourite(false);
                    imgB.setImageResource(R.drawable.star_off);
                }
                else {
                    Habit.habits.get(position).setFavourite(true);
                    imgB.setImageResource(R.drawable.star_on);
                }
                SaveData saveData = new SaveData();
                if (Habit.habits.get(position).getClass() == DateHabit.class) {
                    saveData.updateData(2);
                } else if(Habit.habits.get(position).getClass() == EconomicHabit.class) {
                    saveData.updateData(1);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Habit.habits.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView habitName;
        TextView habitGoal;
        TextView habitDescription;
        ImageButton favoriteButton;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habit_name);
            habitGoal = itemView.findViewById(R.id.habit_goal);
            habitDescription = itemView.findViewById(R.id.habit_description);
            favoriteButton = itemView.findViewById(R.id.favoriteBtn);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
