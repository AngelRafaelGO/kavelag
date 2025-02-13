package org.kavelag.project.models


enum class NetworkIssueErrorResponses(val message: String) {
    DESTINATION_SERVER_DID_NOT_RESPOND("The destination server did not respond"),
    UNREACHABLE_DESTINATION_SERVER("No action was applied to network -> simulating unreachable destination server"),
    UNAVAILABLE_PORT("This port is closed or unavailable"),
    INVALID_URL("The url is invalid, must be in format : http:// or https://")


}
