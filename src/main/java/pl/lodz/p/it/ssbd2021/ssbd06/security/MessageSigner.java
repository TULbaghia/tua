package pl.lodz.p.it.ssbd2021.ssbd06.security;

import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.ETagConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MessageSigner {

    @Inject
    private ETagConfig config;

}
