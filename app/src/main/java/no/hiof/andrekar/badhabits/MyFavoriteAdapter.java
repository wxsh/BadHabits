package no.hiof.andrekar.badhabits;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.ViewHolder>{

        private static final String TAG = "RecyclerViewAdapter";

        private Context mContext;

        private boolean mHaveFavorite;

    public MyFavoriteAdapter(Context context) {
            mContext = context;
        }

        @Override
        public MyFavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favorite_listitem, parent, false);
            MyFavoriteAdapter.ViewHolder holder = new MyFavoriteAdapter.ViewHolder(view);

            return holder;
        }

        //TODO: fix some bugs, like clickable when empty
    //DONE: Empty item view does not show if you remove the last favourite.

        @Override
        public void onBindViewHolder(final MyFavoriteAdapter.ViewHolder holder, final int position) {

        if (position == 0){
            mHaveFavorite = Habit.getHaveFavorite();
        }

            if (mHaveFavorite) {
                holder.parentLayout.setClickable(true);
                holder.emptyFav.setVisibility(View.GONE);
                holder.favoriteButton.setVisibility(View.VISIBLE);
                holder.habitDescription.setVisibility(View.VISIBLE);
                holder.habitGoal.setVisibility(View.VISIBLE);
                holder.habitName.setVisibility(View.VISIBLE);


                if (!Habit.habits.get(position).getIsFavourite()) {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                } else {
                    holder.itemView.setVisibility(View.VISIBLE);
                    if (Habit.getNumFavourites() == 1) {
                        holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    } else if (Habit.getNumFavourites() == 2) {
                        holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(750, ViewGroup.LayoutParams.MATCH_PARENT));
                    } else if (Habit.getNumFavourites() >= 3) {
                        holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(500, ViewGroup.LayoutParams.MATCH_PARENT));
                    }


                        holder.habitName.setText(Habit.habits.get(position).getTitle().toString());
                    holder.habitDescription.setText(Habit.habits.get(position).getDescription().toString());

                    //>DONE: DONE
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


                            if (Habit.habits.get(position).getIsFavourite()) {
                                Habit.habits.get(position).setFavourite(false);
                                holder.favoriteButton.setImageResource(R.drawable.star_off);
                            } else {
                                Habit.habits.get(position).setFavourite(true);
                                holder.favoriteButton.setImageResource(R.drawable.star_on);
                            }
                            SaveData saveData = new SaveData();
                            if (Habit.habits.get(position).getClass() == DateHabit.class) {
                                saveData.saveData(Habit.habits.get(position), 2);
                            } else if (Habit.habits.get(position).getClass() == EconomicHabit.class) {
                                saveData.saveData(Habit.habits.get(position), 1);
                            }

                            Collections.sort(Habit.habits, Habit.HabitComparator);
                            MainActivity.updateRecyclerView();
                        }
                    });

                    holder.failedButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder failedBuilder = new AlertDialog.Builder(view.getContext());
                            failedBuilder.setMessage("Don't worry, even if you fail, you can still do this! Do you want to reset the days since last fail?").setTitle("Failed habit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Habit habit = Habit.habits.get(position);
                                    SaveData saveData = new SaveData();
                                    Date currentTime = Calendar.getInstance().getTime();
                                    habit.setFailDate(currentTime.getTime());
                                    if (habit instanceof EconomicHabit) {
                                        saveData.saveData(Habit.habits.get(position), 1);
                                    } else if (habit instanceof DateHabit) {
                                        saveData.saveData(Habit.habits.get(position), 2);
                                    }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // CANCEL AND DO NOTHING
                                }
                            });
                            // Create the AlertDialog object and return it
                            AlertDialog dialog = failedBuilder.create();
                            dialog.show();
                        }
                    });
                }
            }
            else if (position == 0) {
                holder.parentLayout.setClickable(false);
                holder.emptyFav.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.favoriteButton.setVisibility(View.GONE);
                holder.habitDescription.setVisibility(View.GONE);
                holder.habitGoal.setVisibility(View.GONE);
                holder.habitName.setVisibility(View.GONE);
            }
            else {
                holder.emptyFav.setVisibility(View.GONE);
                holder.favoriteButton.setVisibility(View.GONE);
                holder.habitDescription.setVisibility(View.GONE);
                holder.habitGoal.setVisibility(View.GONE);
                holder.habitName.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));

            }
        }


        @Override
        public int getItemCount() {
            return Habit.habits.size();
        }


        public static class ViewHolder extends RecyclerView.ViewHolder{

            TextView habitName;
            TextView habitGoal;
            TextView habitDescription;
            ImageButton favoriteButton;
            ImageButton failedButton;
            RelativeLayout parentLayout;

            TextView emptyFav;

            public ViewHolder(View itemView) {
                super(itemView);
                habitName = itemView.findViewById(R.id.fav_habit_name);
                habitGoal = itemView.findViewById(R.id.fav_habit_goal);
                habitDescription = itemView.findViewById(R.id.fav_habit_description);
                favoriteButton = itemView.findViewById(R.id.fav_favoriteBtn);
                parentLayout = itemView.findViewById(R.id.fav_parent_layout);
                failedButton = itemView.findViewById(R.id.fav_btn_habitFailed);

                emptyFav = itemView.findViewById(R.id.empty_fav);
            }
        }


}
