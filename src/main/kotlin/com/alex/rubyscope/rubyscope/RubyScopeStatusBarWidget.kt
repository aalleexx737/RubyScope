package com.alex.rubyscope.rubyscope

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidget.TextPresentation
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import java.awt.Component
import java.awt.event.MouseEvent
import com.intellij.util.Consumer

class RubyScopeStatusBarWidget(private val project: Project) :
    StatusBarWidget, TextPresentation, CaretListener {

    private var statusBar: StatusBar? = null
    private var scopeText = "Scope: public"

    init {
        // Listen for caret changes globally within the project
        EditorFactory.getInstance().eventMulticaster.addCaretListener(this, this)
    }

    override fun ID(): String = "RubyScopeWidget"
    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
        updateScope()
    }
    override fun dispose() {
        statusBar = null
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this
    override fun getText(): String = scopeText
    override fun getTooltipText(): String = "Current Ruby method visibility scope"
    override fun getClickConsumer(): Consumer<MouseEvent>? = null
    override fun getAlignment(): Float = Component.CENTER_ALIGNMENT

    override fun caretPositionChanged(event: CaretEvent) {
        updateScope()
    }

    private fun updateScope() {
        val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
        val offset = editor.caretModel.offset
        val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: return
        val element = file.findElementAt(offset) ?: return

        val visibility = findVisibilityModifier(element)
        scopeText = "Scope: $visibility"
        statusBar?.updateWidget(ID())
    }

    private fun findVisibilityModifier(element: PsiElement): String {
        var current: PsiElement? = element
        while (current != null) {
            val text = current.text.trim()
            val lower = text.lowercase()
            if (lower == "private" || lower == "protected" || lower == "public") {
                return lower
            }
            current = current.prevSibling ?: current.parent
        }
        return "public"
    }
}
