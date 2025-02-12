package org.kavelag.project.models


enum class NetworkIssueResponses(val message: String){
    DESTINATION_SERVER_DO_NOT_RESPOND("The destination server did not respond"),
    UNREACHABLE_DESTINATION_SERVER("No action was applied to network -> simulating unreachable destination server"),
    UNAVAILABLE_PORT("this port is closed or unavailable")

}
