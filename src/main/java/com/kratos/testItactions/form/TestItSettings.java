package com.kratos.testItactions.form;


import com.intellij.openapi.actionSystem.AnActionEvent;
import com.kratos.generalactions.methodsactions.SetupMethod;
import com.kratos.generalactions.methodsactions.TearDownMethod;
import com.kratos.generalactions.methodsactions.TestMethod;
import com.kratos.generalactions.classactions.ActionWithClass;
import com.kratos.pluginDB.SQLiteAction;
import com.kratos.testItactions.testitclient.TestItClientImpl;
import com.kratos.testItactions.testitclient.model.TestCaseForCreateClass;

import javax.swing.*;
import java.awt.*;

public class TestItSettings extends JPanel {

    private final JFrame frame;
    private JRadioButton generateCodeRadioButton;
    private JTextField tokenField;
    private JTextField urlField;
    private JPanel testIt;
    private JButton generateButton;
    private JButton exitButton;
    private JTextField globalIdField;
    private JRadioButton saveToken;
    private JButton deleteSavedToken;

    public TestItSettings(JFrame frame, AnActionEvent event) {
        SQLiteAction db = new SQLiteAction(event);
        this.frame = frame;
        setLayout(new BorderLayout());
        setName("TestIT");
        add(testIt);
        db.createDBWithTableUsers();
        String token = db.selectDataFromTableUsers();
        tokenField.setText(token);
        exitButton.addActionListener(e -> handleExit());
        generateButton.addActionListener(g -> handleGenerate(event, db));
        deleteSavedToken.addActionListener(d -> handleDeleteFromDB(db));
    }

    private void handleExit() {
        frame.setVisible(false);
    }

    private void handleGenerate(AnActionEvent event, SQLiteAction db) {
        if (saveToken.isSelected()) {
            db.saveTokenToTableUsers(tokenField.getText());
        }
        ActionWithClass actionWithClass = new ActionWithClass();
        TestMethod testMethod = new TestMethod();
        SetupMethod setupMethod = new SetupMethod();
        TearDownMethod tearDownMethod = new TearDownMethod();
        TestItClientImpl testItImpl = new TestItClientImpl();
        TestCaseForCreateClass testcaseInfo = testItImpl.getTestCase(
                tokenField.getText(),
                urlField.getText() + globalIdField.getText()
        );
        actionWithClass.actionWithClass(event);
        setupMethod.addSetupMethod(event, testcaseInfo);
        tearDownMethod.addTearDownMethod(event, testcaseInfo);
        testMethod.createTestMethodFromTestIt(event, testcaseInfo);
        frame.setVisible(false);
    }

    private void handleDeleteFromDB(SQLiteAction db) {
        db.deleteDataFromTableUsers();
    }
}
