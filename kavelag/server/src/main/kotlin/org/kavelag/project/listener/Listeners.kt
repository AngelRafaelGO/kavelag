package org.kavelag.project.listener

//suspend fun listenerForParams(): AppliedNetworkAction? {
//    try {
//        val data = SetUserConfigurationChannel.appliedNetworkAction.receive()
//        if (data != null) {
//            try {
//                return AppliedNetworkAction(data.appliedNetworkAction, data.params)
//            } catch (e: Exception) {
//                println("Error while recovering user input data: ${e.message}")
//                return null
//            }
//        }
//    } catch (e: ClosedReceiveChannelException) {
//        println("The channel is empty or closed. No more configurations to process.")
//    }
//    return null
//}
