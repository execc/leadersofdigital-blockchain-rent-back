package com.wavesplatform.we.app.rent.config

import com.wavesplatform.vst.contract.factory.ContractAuthenticate
import com.wavesplatform.vst.node.config.NodeCredsKeysProperties
import com.wavesplatform.vst.security.commons.OAuth2TokenSupport
import com.wavesplatform.we.starter.contract.auth.ContractAuthProvider
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.stereotype.Component

@Order(0)
@Component
class Oauth2ContractAuthProvider(
    private val nodeCredentials: NodeCredsKeysProperties,
    private val oAuth2TokenSupport: OAuth2TokenSupport
) : ContractAuthProvider {

    override fun canAuth(): Boolean {
        val auth = SecurityContextHolder.getContext().authentication
        return auth.details is OAuth2AuthenticationDetails
    }

    override fun auth() = ContractAuthenticate.builder()
            .sender(oAuth2TokenSupport.currentUserPersonInfo.participantAddress)
            .password(
                    nodeCredentials.config.getValue(oAuth2TokenSupport.currentUserPersonInfo.nodeAlias).keyStorePassword
            ).build()!!
}
