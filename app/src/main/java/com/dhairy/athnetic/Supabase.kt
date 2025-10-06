package com.dhairy.athnetic

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Singleton object to hold the Supabase client instance.
 */
object Supabase {
    val client = createSupabaseClient(
        supabaseUrl = "https://jcxjaoftbaslfnjapkdb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpjeGphb2Z0YmFzbGZuamFwa2RiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTcxMzQ2MzksImV4cCI6MjA3MjcxMDYzOX0.PmExwcl_fNAZdM1wvs9jXoWHLYaHTuqZwVJemLu8uOg"
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