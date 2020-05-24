package com.example.exoesqueletov1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.fragments.MenuFragment;
import com.example.exoesqueletov1.fragments.PairedDevisesFragment;
import com.github.clans.fab.FloatingActionButton;

import static com.example.exoesqueletov1.fragments.PairedDevisesFragment.*;

public class ControlActivity extends AppCompatActivity {

    private enum FragmentUse { UpAndDown, WalkSteps, WalkMinutes, Work, Menu } // type of variable

    private FragmentUse fragmentUse;

    private static FloatingActionButton actionButtonReport;
    private static FloatingActionButton actionButtonStop;
    private static FloatingActionButton actionButtonPause;
    private static FloatingActionButton actionButtonHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        String address = getIntent().getExtras().getString(DEVICE_ADDRESS);
        String typeUser = getIntent().getExtras().getString(TYPE_USER);
        Fragment fragment = new MenuFragment(address, typeUser);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commit();
    }

    public static void setActionButtonReport(FloatingActionButton actionButtonReport) {
        ControlActivity.actionButtonReport = actionButtonReport;
    }

    public static void setActionButtonStop(FloatingActionButton actionButtonStop) {
        ControlActivity.actionButtonStop = actionButtonStop;
    }

    public  static void setActionButtonPause(FloatingActionButton actionButtonPause) {
        ControlActivity.actionButtonPause = actionButtonPause;
    }

    public static void setActionButtonHelp(FloatingActionButton actionButtonHelp) {
        ControlActivity.actionButtonHelp = actionButtonHelp;
    }

    public static FloatingActionButton getActionButtonReport() {
        return actionButtonReport;
    }

    public  static FloatingActionButton getActionButtonStop() {
        return actionButtonStop;
    }

    public  static FloatingActionButton getActionButtonPause() {
        return actionButtonPause;
    }

    public  static FloatingActionButton getActionButtonHelp() {
        return actionButtonHelp;
    }
}
