package com.dhairy.athnetic

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Singleton object to hold the Supabase client instance.
 */
object Supabase {
    val client = createSupabaseClient(
        SupabaseKeys.URL, SupabaseKeys.KEY
    ) {
        install(Auth) {
            scheme = "athnetic"
            host = "login"
            autoLoadFromStorage = true
        }
        install(Postgrest)
    }
    val extras = {
        // Additional configurations (for later use)
    }
}