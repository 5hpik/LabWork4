package shpik.jmt3;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import java.io.IOException;


public class MainController {

    private IModel model;

    @FXML
    private Button PauseResume;

    @FXML
    private ProgressBar Pr;

    @FXML
    void PauseResume(ActionEvent event) {
        if(PauseResume.getText().equals("Pause"))
        {
            model.pause();
            PauseResume.setText("Resume");
        }
        else if(PauseResume.getText().equals("Resume"))
        {
            synchronized (System.out)
            {
                System.out.notify();
            }
            model.resume();
            PauseResume.setText("Pause");
        }

    }

    @FXML
    void Start(ActionEvent event) throws IOException {
        model.some_calc(new Updatable() {
            @Override
            public void update(double value) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Pr.setProgress(value);
                    }
                });
            }
        });
    }

    @FXML
    void Stop(ActionEvent event) {
        model.stop();
        Pr.setProgress(0);
        PauseResume.setText("Pause");

    }

    @FXML
    void initialize() {
        ModelFactory modelFactory = new ModelFactory();
        model = modelFactory.createInstance();
    }

}
