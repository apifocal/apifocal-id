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

import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormContext;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.Constants;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apifocal.id.keycloak.extension.attributes.Messages.*;


public class AttributesFormAction implements FormAction {

    @Override
    public void buildPage(FormContext context, LoginFormsProvider form) {

    }

    @Override
    public void validate(ValidationContext context) {
        boolean disabled = getBooleanOption(context, AttributesFormActionFactory.ATTRIBUTE_DISABLE, false);
        boolean allowOthers = getBooleanOption(context, AttributesFormActionFactory.ATTRIBUTE_ALLOW_OTHERS, true);

        MultivaluedMap<String, String> formParameters = context.getHttpRequest().getDecodedFormParameters();
        Map<String, List<String>> attributes = getUserAttributesFromRequest(formParameters);

        List<FormMessage> errors = new ArrayList<>();
        if (disabled && !attributes.isEmpty()) {
            errors.add(new FormMessage(USER_ATTRIBUTES_NOT_EXPECTED));
            fail(context, errors);
            return;
        }

        List<String> whitelist = getListOption(context, AttributesFormActionFactory.ATTRIBUTE_WHITELIST, Collections.emptyList());
        List<String> blacklist = getListOption(context, AttributesFormActionFactory.ATTRIBUTE_BLACKLIST, Collections.emptyList());
        boolean whitelisting = !whitelist.isEmpty();
        boolean blacklisting = !blacklist.isEmpty();

        int permitted = 0;
        for (String key : attributes.keySet()) {
            String attributeName = key.substring(Constants.USER_ATTRIBUTES_PREFIX.length());
            if ((blacklisting && blacklist.contains(attributeName)) ||
                (whitelisting && !whitelist.contains(attributeName))) {
                errors.add(new FormMessage(key, USER_ATTRIBUTE_NOT_EXPECTED));
                continue;
            }

            if (!allowOthers) {
                errors.add(new FormMessage(key, USER_ATTRIBUTE_NOT_EXPECTED));
                continue;
            }
        }

        if (errors.size() > 0) {
            fail(context, errors);
            return;
        }

        context.success();
    }

    private void fail(ValidationContext context, List<? extends org.keycloak.models.utils.FormMessage> errors) {
        context.validationError(context.getHttpRequest().getDecodedFormParameters(), (List<org.keycloak.models.utils.FormMessage>) errors);
    }

    private Map<String, List<String>> getUserAttributesFromRequest(MultivaluedMap<String, String> formParameters) {
        return formParameters.entrySet().stream()
            .filter(e -> e.getKey().startsWith(Constants.USER_ATTRIBUTES_PREFIX))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean getBooleanOption(ValidationContext context, String name, boolean defaultValue) {
        Map<String, String> config = context.getAuthenticatorConfig().getConfig();

        return getDefaultValue(config, name, Boolean::parseBoolean, defaultValue);
    }

    private <T> T getDefaultValue(Map<String, String> config, String name, Function<String, T> mapper, T defaultValue) {
        return Optional.ofNullable(config.get(name))
            .map(mapper)
            .orElse(defaultValue);
    }

    private List<String> getListOption(ValidationContext context, String name, List<String> defaultValue) {
        Map<String, String> config = context.getAuthenticatorConfig().getConfig();

        Function<String, List<String>> mapper = value -> Arrays.stream(value.split(","))
            .map(String::trim)
            .filter(listElement -> !listElement.isEmpty())
            .collect(Collectors.toList());
        return getDefaultValue(config, name, mapper, defaultValue);
    }

    @Override
    public void success(FormContext context) {
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public void close() {

    }

}
