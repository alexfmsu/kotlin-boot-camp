package io.rybalkinsd.kotlinbootcamp

class RawProfile(val rawData: String)

enum class DataSource {
    FACEBOOK,
    VK,
    LINKEDIN
}

sealed class Profile(
    var id: Long,
    var firstName: String? = null,
    var lastName: String? = null,
    var age: Int? = null,
    var dataSource: DataSource
)

/**
 * Task #1
 * Declare classes for all data sources
 */
class FacebookProfile(id: Long) : Profile(dataSource = DataSource.FACEBOOK, id = id)
class VKProfile(id: Long) : Profile(dataSource = DataSource.VK, id = id)
class LinkedInProfile(id: Long) : Profile(dataSource = DataSource.LINKEDIN, id = id)

class ProfileID {
    companion object {
        fun generate(): Long {
            IDs.add(counter)
            return counter++
        }

        private var counter: Long = 0
        private val IDs = mutableListOf<Long>()
    }
}

/**
 * Here are Raw profiles to analyse
 */
val rawProfiles = listOf(
    RawProfile("""
            fisrtName=alice,
            age=16,
            source=facebook
            """.trimIndent()
    ),
    RawProfile("""
            fisrtName=Dent,
            lastName=kent,
            age=88,
            source=linkedin
            """.trimIndent()
    ),
    RawProfile("""
            fisrtName=alla,
            lastName=OloOlooLoasla,
            source=vk
            """.trimIndent()
    ),
    RawProfile("""
            fisrtName=bella,
            age=36,
            source=vk
            """.trimIndent()
    ),
    RawProfile("""
            fisrtName=angel,
            age=I will not tell you =),
            source=facebook
            """.trimIndent()
    ),

    RawProfile(
        """
            lastName=carol,
            source=vk
            age=49,
            """.trimIndent()
    ),
    RawProfile("""
            fisrtName=bob,
            lastName=John,
            age=47,
            source=linkedin
            """.trimIndent()
    ),
    RawProfile("""
            lastName=kent,
            fisrtName=dent,
            age=88,
            source=facebook
            """.trimIndent()
    )
)

val profiles = getProfiles(rawProfiles)

fun getProfileFromData(input: RawProfile): Profile {
    val data = input.rawData.toLowerCase()
    val profile = when (data[input.rawData.indexOf("source=") + 7]) {
        'v' -> VKProfile(ProfileID.generate())
        'f' -> FacebookProfile(ProfileID.generate())
        else -> LinkedInProfile(ProfileID.generate())
    }
    for (x in data.replace("\n", "").split(',')) {
        when (x.substringBefore('=')) {
            "firstname" -> profile.firstName = x.substringAfter('=')
            "lastname" -> profile.lastName = x.substringAfter('=')
            "age" -> profile.age = try {
                x.substringAfter('=').toInt()
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
    return profile
}

fun getProfiles(input: List<RawProfile>): List<Profile> {
    val profiles = mutableListOf<Profile>()
    input.forEach { profiles += getProfileFromData(it) }
    return profiles
}

/**
 * Task #2
 * Find the average age for each datasource (for profiles that has age)
 */
fun DataSource.makeAvg(): Pair<DataSource, Double> {
    var aged_count = profiles.count { it.dataSource == this }
    var avg = 0
    for (profile in profiles) {
        if (profile.dataSource == this) {
            if (profile.age == null)
                aged_count--
            avg += profile.age ?: 0
        }
    }
    return Pair(this, avg.toDouble() / aged_count)
}

val avgAge: Map<DataSource, Double> = mapOf(
        DataSource.FACEBOOK.makeAvg(),
        DataSource.LINKEDIN.makeAvg(),
        DataSource.VK.makeAvg()
)

/**
 * Task #3
 * Group all user ids together with all profiles of this user.
 * We can assume users equality by : firstName & lastName & age
 */
val groupedProfiles: Map<Long, List<Profile>> =
        profiles.associate { it.id to profiles.fold(arrayListOf<Profile>()) { group, profile -> if (profile.id == it.id) group.add(profile); group } }
