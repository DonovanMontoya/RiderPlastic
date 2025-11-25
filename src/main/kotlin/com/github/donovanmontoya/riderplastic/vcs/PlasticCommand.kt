package com.github.donovanmontoya.riderplastic.vcs

enum class PlasticCommand(val cliSubcommand: String, val description: String) {
    CHECKOUT("checkout", "Checkout workspace changes"),
    COMMIT("checkin", "Commit pending changes"),
    SWITCH_BRANCH("switch", "Switch workspace to another branch"),
    MERGE("merge", "Merge changes from another branch"),
    DIFF("diff", "Show differences for a file or change set");
}
