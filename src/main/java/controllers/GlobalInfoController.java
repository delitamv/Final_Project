package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import logic.CacheController;
import logic.ChannelData;
import logic.DataController;
import logic.Settings;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GlobalInfoController  {
    public TextField txtChannelId;
    public Button btnSearch;
    public Label lblRequestTime;
    public Button btnBack;
    public TableView table;
    public Label lblVideo;
    public Label lblViews;
    public Label lblSubscribers;
    public Label lblDate;
    public Label lblNameChannel;

    private logic.Settings settings;
    private CacheController cacheController;
    private DataController dataController;

    public void search(ActionEvent event) {
       settings = new Settings();
        cacheController = new CacheController(settings);
        dataController = new DataController(settings, cacheController);
            Runnable task = () -> {
                long startTime = System.nanoTime();
                String channelId = txtChannelId.getText();
                ChannelData channelData = dataController.getChannelData(channelId);
                 long runTime = (System.nanoTime() - startTime);
            double runTimeInSeconds = TimeUnit.MILLISECONDS.convert(runTime, TimeUnit.NANOSECONDS) / 1000.0;
            lblRequestTime.setText(String.valueOf(runTimeInSeconds));
                if (channelData == null) {
                    txtChannelId.setText("ID такого каналу не знайдено!");
                } else {
                    lblNameChannel.setText(channelData.getTitle());
                    lblDate.setText(channelData.getPublishedAt());
                    lblSubscribers.setText(Long.toString(channelData.getSubscriberCount()));
                    lblVideo.setText(Long.toString(channelData.getVideoCount()));
                    lblViews.setText(Long.toString(channelData.getViewCount()));
                }
            }; Platform.runLater(task);
    }

    public void moveToBack(ActionEvent event) {
        try {
            AppRunner.window.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxmls/analytics.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
