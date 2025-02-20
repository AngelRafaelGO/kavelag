package org.kavelag.project.app

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.kavelag.project.SetUserConfigurationChannel
import org.kavelag.project.models.AppliedNetworkAction
import org.kavelag.project.models.ProxySocketConfiguration
import org.kavelag.project.models.ResponseItem
import org.kavelag.project.startServer
import org.kavelag.project.stopServer
import java.util.prefs.Preferences

@Serializable
data class PreferenceSettings(
    val url: String,
    val ports: List<String>
)

class AppViewModel {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    var url by mutableStateOf("")
    var portValues = mutableStateListOf<String>().apply { repeat(1) { add("") } }
    var latencyParam by mutableStateOf("")
    var latencyParam2 by mutableStateOf("")
    var packageLossEnabled by mutableStateOf(false)
    var networkErrorEnabled by mutableStateOf(false)
    var number by mutableStateOf(1)
    var isProxyRunning by mutableStateOf(false)
    var functionAlreadySelected by mutableStateOf("")
    var showPortLengthError by mutableStateOf(false)
    var showSendError by mutableStateOf(false)
    var showPopUpHelp by mutableStateOf(false)
    var showPopUpPref by mutableStateOf(false)
    val requests = mutableStateListOf<String>()
    val responses = mutableStateListOf<ResponseItem>()
    private val preferences: Preferences = Preferences.userRoot().node("org.kavelag.project")
    private val json = Json { prettyPrint = true }

    init {
        viewModelScope.launch {
            combine(
                snapshotFlow { latencyParam },
                snapshotFlow { latencyParam2 }
            ) { param1, param2 ->
                param1.isNotEmpty() || param2.isNotEmpty()
            }.collect { hasValue ->
                functionAlreadySelected = if (hasValue) "Latency" else ""
            }
        }

        viewModelScope.launch {
            snapshotFlow { packageLossEnabled }
                .collect { isEnabled ->
                    functionAlreadySelected = if (isEnabled) "Random Fail" else ""
                }
        }

        viewModelScope.launch {
            snapshotFlow { networkErrorEnabled }
                .collect { isEnabled ->
                    functionAlreadySelected = if (isEnabled) "Network Error" else ""
                }
        }
    }

    fun onCleared() {
        viewModelScope.cancel()
    }


    private suspend fun listenForRequests() {
        for (request in SetUserConfigurationChannel.incomingHttpDataChannel) {
            requests.add(request.httpIncomingData)
        }
    }

    private suspend fun listenForResponses() {
        for (response in SetUserConfigurationChannel.destinationServerResponseDataChannel) {
            responses.add(ResponseItem(response.httpDestinationServerResponse, null))
        }
    }

    private suspend fun listenForProxyGenericInfo() {
        for (response in SetUserConfigurationChannel.proxyGenericInfoChannel) {
            responses.add(ResponseItem(response.proxyGenericInfo, 0xFF4B0082))
        }
    }

    fun deletePortSlot() {
        if (!isProxyRunning) {
            number--;
            portValues.removeLast();
            if (showPortLengthError) {
                showPortLengthError = false
            }
        }
    }

    fun addPortSlot() {
        if (!isProxyRunning) {
            if (number < 3) {
                portValues.add("")
                number++
            } else {
                showPortLengthError = true
            }
        }
    }

    fun clearResponse() {
        if (responses.isNotEmpty()) {
            responses.clear()
        }
    }

    fun clearRequest() {
        if (requests.isNotEmpty()) {
            requests.clear()
        }
    }

    fun toggleProxy(kavelagScope: CoroutineScope) {
        if (!isProxyRunning) {
            if (url.isNotEmpty() && portValues.isNotEmpty() && functionAlreadySelected.isNotEmpty()) {
                if (portValues.all { it.isNotEmpty() }) {
                    viewModelScope.launch {
                        try {
                            if (latencyParam.isNotEmpty() || latencyParam2.isNotEmpty()) {
                                val connectParam = latencyParam.toIntOrNull() ?: 0
                                val readParam = latencyParam2.toIntOrNull() ?: 0
                                val proxyConfig = ProxySocketConfiguration(
                                    url,
                                    portValues.mapNotNull { it.toIntOrNull() }.toIntArray(),
                                    AppliedNetworkAction("latency", connectParam, readParam)
                                )
                                kavelagScope.launch { startServer(proxyConfig) }
                            }

                            if (packageLossEnabled) {
                                val proxyConfig = ProxySocketConfiguration(
                                    url,
                                    portValues.mapNotNull { it.toIntOrNull() }.toIntArray(),
                                    AppliedNetworkAction("randomrequestfailure")
                                )
                                kavelagScope.launch { startServer(proxyConfig) }
                            }

                            if (networkErrorEnabled) {
                                val proxyConfig = ProxySocketConfiguration(
                                    url,
                                    portValues.mapNotNull { it.toIntOrNull() }.toIntArray(),
                                    AppliedNetworkAction("noNetwork")
                                )
                                kavelagScope.launch { startServer(proxyConfig) }
                            }

                            isProxyRunning = true
                            listenForRequests()
                        } catch (e: Exception) {
                            println("Error: ${e.message}")
                        }
                    }
                } else {
                    showSendError = true
                }
            } else {
                showSendError = true
            }
        } else {
            isProxyRunning = false
            runBlocking { stopServer() }
        }
        kavelagScope.launch { listenForResponses() }
        kavelagScope.launch { listenForProxyGenericInfo() }
    }


    fun savePreferenceSettings(url: String, ports: List<String>) {
        val preferenceSettings = PreferenceSettings(url, ports)

        val currentListJson = preferences.get("preferenceSettingsList", "[]")
        val currentList = json.decodeFromString<List<PreferenceSettings>>(currentListJson).toMutableList()

        if (currentList.any { it.url == url && it.ports == ports }) {
            return
        }

        currentList.add(preferenceSettings)

        val updatedListJson = json.encodeToString(currentList)
        preferences.put("preferenceSettingsList", updatedListJson)

        println(preferences.get("preferenceSettingsList", null))
    }

    fun getAllPreferenceSettings(): List<PreferenceSettings> {
        val listJson = preferences.get("preferenceSettingsList", "[]")
        return json.decodeFromString(listJson)
    }

    fun removePreferenceSettings(selectedItems: List<PreferenceSettings>) {
        val currentListJson = preferences.get("preferenceSettingsList", "[]")
        val currentList = json.decodeFromString<List<PreferenceSettings>>(currentListJson).toMutableList()

        currentList.removeAll { it in selectedItems }

        val updatedListJson = json.encodeToString(currentList)
        preferences.put("preferenceSettingsList", updatedListJson)
    }

    fun loadPreferenceSettings(selectedItem: PreferenceSettings): Pair<String, List<String>>? {
        val currentListJson = preferences.get("preferenceSettingsList", "[]")
        val currentList = json.decodeFromString<List<PreferenceSettings>>(currentListJson)

        val preference = currentList.find { it == selectedItem }

        return if (preference != null) {
            Pair(preference.url, preference.ports)
        } else {
            null
        }
    }

}