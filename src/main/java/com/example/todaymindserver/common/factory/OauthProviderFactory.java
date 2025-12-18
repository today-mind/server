package com.example.todaymindserver.common.factory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.common.provider.OauthProvider;
import com.example.todaymindserver.common.util.OauthProviderType;

@Component
public class OauthProviderFactory {

    private final Map<OauthProviderType, OauthProvider> providerMap = new EnumMap<>(OauthProviderType.class);

    public OauthProviderFactory(List<OauthProvider> providers) {
        for (OauthProvider provider : providers) {
            providerMap.put(provider.getProviderType(), provider);
        }
    }

    public OauthProvider getProvider(OauthProviderType type) {
        return providerMap.get(type);
    }
}


