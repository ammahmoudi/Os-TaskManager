package com.amg.os.util;//package name

//imports

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

public class CustomOutputStream extends OutputStream {
    private TextArea terminal;

    public CustomOutputStream(TextArea terminal) {
        this.terminal = terminal;
    }

    @Override
    public void write(int b) throws IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                terminal.appendText( String.valueOf((char) b));
            }
        });

    }
}
