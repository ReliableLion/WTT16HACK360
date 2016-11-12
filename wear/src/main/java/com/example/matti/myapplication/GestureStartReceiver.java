package wtt.wtt16hack;

/**
 * Created by danie on 12/11/2016.
 */
import watch.nudge.gesturelibrary.AppControllerReceiverService;

public class GestureStartReceiver extends AppControllerReceiverService {
    @Override
    protected Class getWatchActivityClass() {
        return MainWatchActivity.class;
    }
}