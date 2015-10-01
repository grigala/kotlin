/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager
import com.intellij.ide.plugins.PluginManagerMain
import com.intellij.ide.plugins.RepositoryHelper
import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.updateSettings.impl.PluginDownloader
import com.intellij.openapi.updateSettings.impl.UpdateChecker
import com.intellij.openapi.util.JDOMUtil
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.CharsetToolkit
import com.intellij.util.Alarm
import com.intellij.util.io.HttpRequests
import com.intellij.util.text.VersionComparatorUtil
import java.net.URLEncoder

class KotlinPluginUpdater(val propertiesComponent: PropertiesComponent) : Disposable {
    private val INITIAL_UPDATE_DELAY = 5000L
    private var updateDelay = 5000L
    private val alarm = Alarm(Alarm.ThreadToUse.POOLED_THREAD, this)
    private var checkQueued = false
    private val notificationGroup = NotificationGroup("Kotlin plugin updates",  NotificationDisplayType.STICKY_BALLOON, true)

    fun kotlinFileEdited() {
        val lastUpdateTime = java.lang.Long.parseLong(propertiesComponent.getValue(PROPERTY_NAME, "0"))
        if (lastUpdateTime == 0L || System.currentTimeMillis() - lastUpdateTime > 24 * 60 * 60 * 1000L) {
            queueUpdateCheck()
        }
    }

    private fun queueUpdateCheck() {
        if (!checkQueued) {
            checkQueued = true
            alarm.addRequest({ updateCheck() }, updateDelay)
            updateDelay *= 2 // exponential backoff
        }
    }

    private fun updateCheck() {
        try {
            checkQueued = false

            var newVersion = getPluginVersionFromMainRepository()
            var descriptorToInstall: IdeaPluginDescriptor? = null
            var hostToInstallFrom: String? = null

            for (host in RepositoryHelper.getPluginHosts().filterNotNull()) {
                val plugins = RepositoryHelper.loadPlugins(host, null)
                val kotlinPlugin = plugins.find { it.pluginId.toString() == "org.jetbrains.kotlin" }
                if (kotlinPlugin != null && VersionComparatorUtil.compare(kotlinPlugin.version, newVersion) > 0) {
                    newVersion = kotlinPlugin.version
                    descriptorToInstall = kotlinPlugin
                    hostToInstallFrom = host
                }
            }

            if (newVersion != null) {
                recordSuccessfulUpdateCheck()
                if (VersionComparatorUtil.compare(newVersion, JetPluginUtil.getPluginVersion()) > 0) {
                    ApplicationManager.getApplication().invokeLater {
                        notifyPluginUpdateAvailable(newVersion!!, descriptorToInstall, hostToInstallFrom)
                    }
                }
            }
            else {
                queueUpdateCheck()
            }
        }
        catch(e: Exception) {
            LOG.info("Kotlin plugin update check failed", e)
            checkQueued = false
            queueUpdateCheck()
        }
    }

    fun getPluginVersionFromMainRepository(): String? {
        val buildNumber = ApplicationInfo.getInstance().build.asString()
        val pluginVersion = JetPluginUtil.getPluginVersion()
        val os = URLEncoder.encode(SystemInfo.OS_NAME, CharsetToolkit.UTF8)
        val uid = UpdateChecker.getInstallationUID(propertiesComponent)
        val url = "https://plugins.jetbrains.com/plugins/list?pluginId=6954&build=$buildNumber&pluginVersion=$pluginVersion&os=$os&uuid=a$uid"
        val responseDoc = HttpRequests.request(url).connect {
            JDOMUtil.load(it.inputStream)
        }
        val version = responseDoc.getChild("category")?.getChild("idea-plugin")?.getChild("version")?.text
        if (version == null) {
            LOG.info("Couldn't find plugin version in repository response: ${JDOMUtil.writeElement(responseDoc, "\n")}")
        }
        return version
    }

    private fun recordSuccessfulUpdateCheck() {
        propertiesComponent.setValue(PROPERTY_NAME, System.currentTimeMillis().toString())
        updateDelay = INITIAL_UPDATE_DELAY
    }

    private fun notifyPluginUpdateAvailable(newVersion: String,
                                            descriptorToInstall: IdeaPluginDescriptor?,
                                            hostToInstallFrom: String?) {
        val notification = notificationGroup.createNotification(
                "Kotlin",
                "A new version $newVersion of the Kotlin plugin is available. <b><a href=\"#\">Install</a></b>",
                NotificationType.INFORMATION) { notification, event ->
            val descriptor = descriptorToInstall ?: PluginManager.getPlugin(PluginId.getId("org.jetbrains.kotlin"))
            if (descriptor != null) {
                val pluginDownloader = PluginDownloader.createDownloader(descriptor, hostToInstallFrom, null)
                ProgressManager.getInstance().run(object : Task.Backgroundable(null, "Downloading plugins", true) {
                    override fun run(indicator: ProgressIndicator) {
                        if (pluginDownloader.prepareToInstall(indicator)) {
                            val descriptor = pluginDownloader.getDescriptor()
                            if (descriptor != null) {
                                pluginDownloader.install()
                            }

                            ApplicationManager.getApplication().invokeLater {
                                notification.expire()
                                PluginManagerMain.notifyPluginsUpdated(null)
                            }
                        }

                    }
                })
            }
        }

        notification.notify(null)
    }

    override fun dispose() {
    }

    companion object {
        private val PROPERTY_NAME = "kotlin.lastUpdateCheck"
        private val LOG = Logger.getInstance(KotlinPluginUpdater::class.java)

        fun getInstance(): KotlinPluginUpdater = ServiceManager.getService(KotlinPluginUpdater::class.java)
    }
}
