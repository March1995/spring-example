package com.wyb.jpa.config.jpa;

import com.wyb.jpa.mock.CurrUserContext;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author wyb
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(CurrUserContext.getCurrUser().getName());
    }
}
