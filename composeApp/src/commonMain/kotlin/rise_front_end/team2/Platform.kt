package rise_front_end.team2

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform