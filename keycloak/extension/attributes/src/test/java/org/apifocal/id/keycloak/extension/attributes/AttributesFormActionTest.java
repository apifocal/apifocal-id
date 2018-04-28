/*
 * Copyright (c) 2017-2018 apifocal LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apifocal.id.keycloak.extension.attributes;

import com.google.common.collect.ImmutableMap;
import org.jboss.resteasy.spi.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.Constants;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AttributesFormActionTest {

    public static final String FOO = "foo";
    public static final String FAR = "far";

    public static final String FOO_ATTRIBUTE = attributeName(FOO);
    public static final String FAR_ATTRIBUTE = attributeName(FAR);

    public static final List<String> TEST_VALUE = asList("bar");
    private AttributesFormAction formAction;

    private ValidationContext validationContext;

    private ArgumentCaptor<List<org.keycloak.models.utils.FormMessage>> errorCaptor = ArgumentCaptor.forClass(List.class);

    @BeforeEach
    void setup() {
        formAction = new AttributesFormAction();
        validationContext = mock(ValidationContext.class);
    }

    @Test
    void checkDefaultConfig() {
        formAction.validate(validationContext(
            config(false, "", "", true),
            form(Collections.emptyMap())
        ));

        verify(validationContext, times(1)).success();
    }

    @Test
    void checkDisabledAttributes() {
        MultivaluedMap<String, String> form = form(ImmutableMap.of(FOO_ATTRIBUTE, TEST_VALUE));
        formAction.validate(validationContext(
            config(true, "", "", true),
            form
        ));

        verifyOutcome(false, form, new FormMessage(Messages.USER_ATTRIBUTES_NOT_EXPECTED));
    }

    @Test
    void checkBlacklistedAttribute() {
        MultivaluedMap<String, String> form = form(ImmutableMap.of(
            FOO_ATTRIBUTE, TEST_VALUE,
            FAR_ATTRIBUTE, TEST_VALUE
        ));
        formAction.validate(validationContext(
            config(false, "", FOO, false),
            form
        ));

        verifyOutcome(false, form,
            new FormMessage(FOO_ATTRIBUTE, Messages.USER_ATTRIBUTE_NOT_EXPECTED),
            new FormMessage(FAR_ATTRIBUTE, Messages.USER_ATTRIBUTE_NOT_EXPECTED)
        );
    }

    @Test
    void checkBlacklistedAttributeWithOthers() {
        MultivaluedMap<String, String> form = form(ImmutableMap.of(
            FOO_ATTRIBUTE, TEST_VALUE,
            FAR_ATTRIBUTE, TEST_VALUE
        ));
        formAction.validate(validationContext(
            config(false, "", FOO, true),
            form
        ));

        verifyOutcome(false, form, new FormMessage(FOO_ATTRIBUTE, Messages.USER_ATTRIBUTE_NOT_EXPECTED));
    }


    @Test
    void checkWhitelistAttribute() {
        MultivaluedMap<String, String> form = form(ImmutableMap.of(
            FOO_ATTRIBUTE, TEST_VALUE,
            FAR_ATTRIBUTE, TEST_VALUE
        ));
        formAction.validate(validationContext(
            config(false, FOO, "", false),
            form
        ));

        verifyOutcome(false, form,
            new FormMessage(FOO_ATTRIBUTE, Messages.USER_ATTRIBUTE_NOT_EXPECTED),
            new FormMessage(FAR_ATTRIBUTE, Messages.USER_ATTRIBUTE_NOT_EXPECTED)
        );
    }

    @Test
    void checkWhitelistAttributeWithOthers() {
        MultivaluedMap<String, String> form = form(ImmutableMap.of(
            FOO_ATTRIBUTE, TEST_VALUE,
            FAR_ATTRIBUTE, TEST_VALUE
        ));
        formAction.validate(validationContext(
            config(false, FOO, "", true),
            form
        ));

        verifyOutcome(false, form, new FormMessage(FAR_ATTRIBUTE, Messages.USER_ATTRIBUTE_NOT_EXPECTED));
    }

    @Test
    void checkWhitelistAndBlackList() {
        MultivaluedMap<String, String> form = form(ImmutableMap.of(
            FOO_ATTRIBUTE, TEST_VALUE,
            FAR_ATTRIBUTE, TEST_VALUE
        ));

        formAction.validate(validationContext(
            config(false, FOO, FOO, true),
            form
        ));

        verifyOutcome(false, form,
            new FormMessage(FAR_ATTRIBUTE, Messages.USER_ATTRIBUTE_NOT_EXPECTED),
            new FormMessage(FOO_ATTRIBUTE, Messages.USER_ATTRIBUTE_NOT_EXPECTED)
        );
    }

    private static String attributeName(String name) {
        return Constants.USER_ATTRIBUTES_PREFIX + name;
    }

    private ValidationContext validationContext(Map<String, String> config, MultivaluedMap<String, String> form) {
        AuthenticatorConfigModel configModel = mock(AuthenticatorConfigModel.class);
        HttpRequest request = mock(HttpRequest.class);

        when(validationContext.getAuthenticatorConfig()).thenReturn(configModel);
        when(validationContext.getHttpRequest()).thenReturn(request);

        when(configModel.getConfig()).thenReturn(config);
        when(request.getDecodedFormParameters()).thenReturn(form);

        return validationContext;
    }

    private Map<String, String> config(boolean disable, String whitelist, String blacklist, boolean allowOthers) {
        Map<String, String> config = new HashMap<>();
        config.put(AttributesFormActionFactory.ATTRIBUTE_DISABLE, Boolean.toString(disable));
        config.put(AttributesFormActionFactory.ATTRIBUTE_WHITELIST, whitelist);
        config.put(AttributesFormActionFactory.ATTRIBUTE_BLACKLIST, blacklist);
        config.put(AttributesFormActionFactory.ATTRIBUTE_ALLOW_OTHERS, Boolean.toString(allowOthers));
        return config;
    }

    private MultivaluedMap<String, String> form(Map<String, List<String>> params) {
        return new MultivaluedHashMap(params);
    }

    private void verifyOutcome(boolean success, MultivaluedMap<String, String> form, FormMessage ... formMessages) {
        if (success) {
            verify(validationContext).success();
            return;
        } else {
            verify(validationContext, never()).success();
        }

        verify(validationContext).validationError(eq(form), errorCaptor.capture());

        List<org.keycloak.models.utils.FormMessage> errors = errorCaptor.getValue();

        assertThat(errors).hasSize(formMessages.length)
            .containsOnly(formMessages);
    }
}