package com.kratos.testItactions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.kratos.testItactions.form.exportfromtestit.settings.*;
import org.jetbrains.annotations.NotNull;

public class TestFromTestIt extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        TestItSettingsForm testItSettingsForm = new TestItSettingsForm(event);
        testItSettingsForm.setVisible(true);
    }
}
