package com.alex.rubyscope.rubyscope

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBar

class VisibilityStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId() = "RubyScope.StatusBar"

    override fun getDisplayName() = "Ruby Visibility Scope"

    override fun isAvailable(project: Project) = true

    override fun createWidget(project: Project): StatusBarWidget {
        return RubyScopeStatusBarWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {}

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}
