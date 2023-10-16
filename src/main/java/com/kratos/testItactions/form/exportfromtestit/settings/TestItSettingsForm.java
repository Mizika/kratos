package com.kratos.testItactions.form.exportfromtestit.settings;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.kratos.testItactions.form.exportfromtestit.TestItSettings;

import javax.swing.*;
import java.awt.*;

public class TestItSettingsForm extends JFrame {

    public TestItSettingsForm(AnActionEvent event) {

        TestItSettings testItSettings = new TestItSettings(this, event);

        setContentPane(testItSettings);
        setResizable(false);
        setSize(500, 250);
        setTitle("TestIT Settings");
        setLocationRelativeTo(null);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
    }
}