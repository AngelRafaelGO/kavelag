package org.kavelag.project.parser

object RestRegex {
    val getRegex: Regex = "^GET\\b".toRegex()
    val postRegex: Regex = "^POST\\b".toRegex()
    val headerRegex: Regex = "^([a-zA-Z0-9-]+): (.+)\$\n".toRegex()
}
