package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChecklistActivity extends ActionBarActivity {
    LinearLayout layout;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        layout = (LinearLayout) findViewById(R.id.linearMain);

        if (getIntent().getBooleanExtra("aromas", false)) {
            list = App.getConfigHelper().getAromas();
        } else if (getIntent().getBooleanExtra("varietals", false)) {
            list = App.getConfigHelper().getVarietals();
        } else if (getIntent().getBooleanExtra("descriptors", false)) {
            list = App.getConfigHelper().getDescriptors();
        } else {
            return;
        }

        JSONArray array = new JSONArray();
        try {
            String jsonArray = getIntent().getStringExtra("jsonArray");
            if (jsonArray != null) {
                array = new JSONArray(jsonArray);
            }

            for (int i = 0; i < list.size(); i++) {
                String listItem = list.get(i);
                CheckBox checkBox = new CheckBox(this);
                checkBox.setId(i);
                checkBox.setText(listItem);
                for (int j = 0; j < array.length(); j++) {
                    if (listItem.equals(array.get(j))) {
                        checkBox.setChecked(true);
                    }
                }
                layout.addView(checkBox);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checklist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            Intent intent = new Intent();
            ArrayList<String> checkedList = new ArrayList<String>();
            for (int i = 0; i < list.size(); i++) {
                CheckBox checkBox = (CheckBox) layout.findViewById(i);
                if (checkBox.isChecked()) {
                    checkedList.add(list.get(i));
                }
            }

            if (getIntent().getBooleanExtra("aromas", false)) {
                intent.putStringArrayListExtra("aromas", checkedList);
            } else if (getIntent().getBooleanExtra("varietals", false)) {
                intent.putStringArrayListExtra("varietals", checkedList);
            } else if (getIntent().getBooleanExtra("descriptors", false)) {
                intent.putStringArrayListExtra("descriptors", checkedList);
            }
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
