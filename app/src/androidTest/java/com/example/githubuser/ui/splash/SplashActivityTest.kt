package com.example.githubuser.ui.splash

import androidx.test.core.app.ActivityScenario
import org.junit.Assert.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.githubuser.R
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SplashActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(SplashActivity::class.java)
    }

    @Test
    fun onCreate() {
        onView(withId(R.id.iv_splash)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_splash)).check(matches(isNotClickable()))
        onView(withText(R.string.app_name)).check(matches(isDisplayed()))
    }
}