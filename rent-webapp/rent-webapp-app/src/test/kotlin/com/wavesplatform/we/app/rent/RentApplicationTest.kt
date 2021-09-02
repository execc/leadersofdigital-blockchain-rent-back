package com.wavesplatform.we.app.rent

import com.wavesplatform.vst.security.commons.OAuth2TokenSupport
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
internal class RentApplicationTest {

    @MockBean
    lateinit var tokenStore: TokenStore

    @MockBean(name = "oauth2TokenSupport")
    lateinit var tokenSupport: OAuth2TokenSupport

    @Test
    fun contextLoads() {
    }

}
