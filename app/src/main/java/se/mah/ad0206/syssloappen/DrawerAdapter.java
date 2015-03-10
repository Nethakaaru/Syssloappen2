package se.mah.ad0206.syssloappen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Sebastian Aspegren on 2015-03-09.
 */
public class DrawerAdapter extends ArrayAdapter<String> {
    private String[] pages;


    public DrawerAdapter(Context context, String[] pages) {
        super(context, R.layout.draweritem,pages);
        this.pages=pages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.draweritem, parent, false);
        TextView item  = (TextView) convertView.findViewById(R.id.DrawerItem);
        item.setText(pages[position]);

        return convertView;
    }

}
