package com.dhairy.athnetic

enum class Destination(
    val route: String,
    val label: String,
    val icon: Int,
    val contentDescription: String,
) {
    HOME(
        "home",
        icon = R.drawable.home_24px,
        label = "Home",
        contentDescription = "Go to Home Screen"
    ),
    PROFILE(
        "profile",
        icon = R.drawable.account_circle_24px,
        label = "Profile",
        contentDescription = "Go to Profile Screen"
    ),
    FEEDBACK(
        "feedback",
        icon = R.drawable.feedback_24px,
        label = "Feedback",
        contentDescription = "Go to Feedback Screen"
    );
}

