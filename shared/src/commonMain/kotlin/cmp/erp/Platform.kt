package cmp.erp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform