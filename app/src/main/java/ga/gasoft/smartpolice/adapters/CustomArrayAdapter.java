package ga.gasoft.smartpolice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ga.gasoft.smartpolice.R;

public class CustomArrayAdapter extends ArrayAdapter<String> {

private List<String> objects;
private Context context;

public CustomArrayAdapter(Context context, int resourceId,
     List<String> objects) {
     super(context, resourceId, objects);
     this.objects = objects;
     this.context = context;
}

@Override
public View getDropDownView(int position, View convertView,
                            ViewGroup parent) {
    return getCustomView(position, convertView, parent);
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
  return getCustomView(position, convertView, parent);
}

public View getCustomView(int position, View convertView, ViewGroup parent) {

LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
View row=inflater.inflate(R.layout.spinner_drop_down, parent, false);
TextView label=(TextView)row.findViewById(R.id.TextView);
 label.setText(objects.get(position));


return row;
}

}