package wine.cmsc436.wine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ui.ParseLoginBuilder;
import com.parse.ui.ParseLoginDispatchActivity;

import parse.subclasses.User;


public class InitActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return WineActivity.class;
    }

}
