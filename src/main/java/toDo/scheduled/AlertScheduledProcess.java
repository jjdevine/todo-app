package toDo.scheduled;

import toDo.alert.Alert;
import toDo.gui.alert.AlertManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlertScheduledProcess implements TodoScheduledProcess{

    @Override
    public void run() {

        AlertManager alertManager = AlertManager.getInstance();
        Calendar calNow = Calendar.getInstance();

        //get alerts to fire
        List<Alert> alertsToFire = new ArrayList<>();

        for(Alert alert: alertManager.getAlertList()) {
            if(alert.getAlertTime().compareTo(calNow)<=0) {
                alertsToFire.add(alert);
            }
        }

        //process alerts that are due
        for(Alert alert: alertsToFire) {
            alertManager.fireAlert(alert);
            alertManager.deleteAlert(alert);
        }
    }
}
