package cmp.erp.data.local

class InMemoryTokenStorage : TokenStorage {
    private var accessToken: String? = null
    private var refreshTokenValue: String? = null

    override fun saveToken(token: String) {
        this.accessToken = token
    }

    override fun getToken(): String? {
        return accessToken
    }

    override fun clearToken() {
        this.accessToken = null
    }

    override fun saveRefreshToken(token: String) {
        this.refreshTokenValue = token
    }

    override fun getRefreshToken(): String? {
        return refreshTokenValue
    }

    override fun clearRefreshToken() {
        this.refreshTokenValue = null
    }
}

