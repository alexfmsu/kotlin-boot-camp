package io.rybalkinsd.kotlinbootcamp

import junit.framework.TestCase.assertTrue
import org.junit.Test

class DataAnalysisTest {

    @Test
    fun `check avg age`() {
        assertTrue(avgAge.isNotEmpty())
    }

    @Test
    fun `check grouped profiles`() {
        assertTrue(groupedProfiles.isNotEmpty())
    }

    @Test
    fun `test profile from data`() {
        getProfileFromData(RawProfile("""
            firstName=my_first_name,
            lastName=my_last_name,
            age=99,
            source=facebook
            """.trimIndent()
        )).also { println(it.firstName) }
    }
}
