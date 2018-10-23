package no.hiof.andrekar.badhabits;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.ViewHolder>{

        private static final String TAG = "RecyclerViewAdapter";

        private Context mContext;

    public MyFavoriteAdapter(Context context) {
            mContext = context;
        }

        @Override
        public MyFavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favorite_listitem, parent, false);
            MyFavoriteAdapter.ViewHolder holder = new MyFavoriteAdapter.ViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyFavoriteAdapter.ViewHolder holder, final int position) {

            if(Habit.habits.get(position).getIsFavourite()) {

                if (position % 2 == 1)
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.faveHabitBG));
                else
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.faveHabitBG2));


                holder.habitName.setText(Habit.habits.get(position).getTitle().toString());
                holder.habitDescription.setText(Habit.habits.get(position).getDescription().toString());

                //TODO: reminder
                if (Habit.habits.get(position).getClass() == EconomicHabit.class) {
                    holder.habitGoal.setText(((EconomicHabit) Habit.habits.get(position)).getProgress());
                } else if (Habit.habits.get(position).getClass() == DateHabit.class) {
                    holder.habitGoal.setText(((DateHabit) Habit.habits.get(position)).getDateGoal());
                }

                if (Habit.habits.get(position).getIsFavourite()) {
                    holder.favoriteButton.setImageResource(R.drawable.star_on);
                } else {
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

                        if (Habit.habits.get(position).getIsFavourite()) {
                            Habit.habits.get(position).setFavourite(false);
                            imgB.setImageResource(R.drawable.star_off);
                        } else {
                            Habit.habits.get(position).setFavourite(true);
                            imgB.setImageResource(R.drawable.star_on);
                        }
                        SaveData saveData = new SaveData();
                        if (Habit.habits.get(position).getClass() == DateHabit.class) {
                            saveData.updateData(2);
                        } else if (Habit.habits.get(position).getClass() == EconomicHabit.class) {
                            saveData.updateData(1);
                        }

                        Collections.sort(Habit.habits, Habit.HabitComparator);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {

            int favSize = 0;

            for(Habit h: Habit.habits){
                if(h.getIsFavourite())
                    favSize++;
            }

            return favSize;
        }


        public class ViewHolder extends RecyclerView.ViewHolder{

            TextView habitName;
            TextView habitGoal;
            TextView habitDescription;
            ImageButton favoriteButton;
            RelativeLayout parentLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                habitName = itemView.findViewById(R.id.fav_habit_name);
                habitGoal = itemView.findViewById(R.id.fav_habit_goal);
                habitDescription = itemView.findViewById(R.id.fav_habit_description);
                favoriteButton = itemView.findViewById(R.id.fav_favoriteBtn);
                parentLayout = itemView.findViewById(R.id.fav_parent_layout);
            }
        }



}
