package se.mah.ad0206.syssloappen;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Sebastian Aspegren on 2015-03-05.
 *
 * A custom adapter for the list of the chores containing a textView for the points and one for the chore.
 */
public class ChoreListAdapter extends ArrayAdapter<String>{
    private String[] chores;
    private String[] points;

    /**
     * Constructor
     * @param context
     *                  The main activity.
     * @param chores
     *                  An array with all the chores.
     * @param points
     *                  An array with all the points for the chores.
     */
    public ChoreListAdapter(Activity context, String[] chores, String[] points) {
        super(context, R.layout.row, chores);
        this.chores=chores;
        this.points=points;
    }

    /**
     * A method that puts views into the list with the proper information.
     * @param position
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.row, parent, false);
       holder.choreName = (TextView) view.findViewById(R.id.LVTextViewChore);
        holder.chorePoints = (TextView) view.findViewById(R.id.LVTextViewPoints);
        view.setTag(holder);
        holder.choreName.setText(chores[position]);
        holder.chorePoints.setText("Poäng: " + points[position]);

        return view;
    }

    /**
     * ViewHolder for increased performance.
     */
   private static class ViewHolder {
        TextView choreName;
        TextView chorePoints;
    }
}
