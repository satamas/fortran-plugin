package org.jetbrains.fortran.ide.util

import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project

fun <T> Project.executeWriteCommand(name: String, groupId: Any? = null, command: () -> T): T {
    return executeCommand<T>(name, groupId) { runWriteAction(command) }
}

fun <T> Project.executeCommand(name: String, groupId: Any? = null, command: () -> T): T {
    @Suppress("UNCHECKED_CAST") var result: T = null as T
    CommandProcessor.getInstance().executeCommand(this, { result = command() }, name, groupId)
    @Suppress("USELESS_CAST")
    return result as T
}
