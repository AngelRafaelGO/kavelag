package org.kavelag.project.utils

fun percentToUnit(requestsNumber: Int, percentage: Int ): Int{
    return (requestsNumber * percentage) / 100
}