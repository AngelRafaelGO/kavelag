package org.kavelag.project

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import org.kavelag.project.models.AppliedNetworkAction
import org.kavelag.project.models.ProxySocketConfiguration
import java.util.prefs.Preferences
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class PreferenceSettings(
    val url: String,
    val ports: List<String>
)

class AppViewModel {

    val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    var Url by mutableStateOf("")
    var portValues = mutableStateListOf<String>().apply { repeat(1) { add("") } }
    var LatencyParam by mutableStateOf("")
    var PackageLossEnabled by mutableStateOf(false)
    var NetworkErrorEnabled by mutableStateOf(false)
    var number by mutableStateOf(1)
    var isProxyRunning by mutableStateOf(false)
    var FunctionAlreadySelected by mutableStateOf("")
    var showPortLengthError by mutableStateOf(false)
    var showSendError by mutableStateOf(false)
    var showPopUpHelp by mutableStateOf(false)
    var showPopUpPref by mutableStateOf(false)
    val requests = mutableStateListOf<String>()
    val responses = mutableStateListOf<String>()

    fun updateUrl(newUrl: String) {
        Url = newUrl
    }
    fun updatePorts(newPorts: List<String>) {
        portValues = newPorts.toMutableStateList()
    }

    init {
        viewModelScope.launch {
            snapshotFlow { LatencyParam }
                .collect { value ->
                    FunctionAlreadySelected = if (value.isNotEmpty()) "Latency" else ""
                }
        }

        viewModelScope.launch {
            snapshotFlow { PackageLossEnabled }
                .collect { isEnabled ->
                    FunctionAlreadySelected = if (isEnabled) "Random Fail" else ""
                }
        }

        viewModelScope.launch {
            snapshotFlow { NetworkErrorEnabled }
                .collect { isEnabled ->
                    FunctionAlreadySelected = if (isEnabled) "Network Error" else ""
                }
        }
    }

    fun onCleared() {
        viewModelScope.cancel()
    }


    suspend fun listenForRequests() {
        for (request in SetUserConfigurationChannel.incomingHttpData) {
            requests.add(request.httpIncomingData)
        }
    }

    suspend fun listenForResponses() {
        for (response in SetUserConfigurationChannel.destinationServerResponseData) {
            responses.add(response.httpDestinationServerResponse)
        }
    }

    fun deletePortSlot() {
        if (!isProxyRunning) {
            number--;
            portValues.removeLast();
            if (showPortLengthError == true) {
                showPortLengthError = false
            }
        }
    }

    fun addPortSlot() {
        if (!isProxyRunning) {
            if (number < 3) {
                portValues.add("")
                number++
                println("ntm compose")
            } else {
                showPortLengthError = true
            }
        }
    }

    fun toggleProxy(kavelagScope: CoroutineScope) {
        if (!isProxyRunning) {
            if (Url.isNotEmpty() && portValues.isNotEmpty() && FunctionAlreadySelected.isNotEmpty()) {
                if (portValues.all { it.isNotEmpty() }) {
                    viewModelScope.launch {
                        try {
                            if (LatencyParam.isNotEmpty()) {
                                val proxyConfig = ProxySocketConfiguration(
                                    Url, portValues.mapNotNull { it.toIntOrNull() }.toIntArray()
                                    , AppliedNetworkAction("latency", LatencyParam.toInt())
                                )
                                kavelagScope.launch { startServer(proxyConfig) }
                            }

                            if (PackageLossEnabled) {
                                val proxyConfig = ProxySocketConfiguration(
                                    Url, portValues.mapNotNull { it.toIntOrNull() }.toIntArray()
                                    , AppliedNetworkAction("1on2")
                                )
                                kavelagScope.launch { startServer(proxyConfig) }
                            }

                            if (NetworkErrorEnabled) {
                                val proxyConfig = ProxySocketConfiguration(
                                    Url, portValues.mapNotNull { it.toIntOrNull() }.toIntArray()
                                    , AppliedNetworkAction("noNetwork")
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
    }
    private val preferences: Preferences = Preferences.userRoot().node("org.kavelag.project")
    private val json = Json { prettyPrint = true }


    fun savePreferenceSettings(url: String, ports: List<String>) {
        val preferenceSettings = PreferenceSettings(url, ports)

        val currentListJson = preferences.get("preferenceSettingsList", "[]")
        val currentList = json.decodeFromString<List<PreferenceSettings>>(currentListJson).toMutableList()

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