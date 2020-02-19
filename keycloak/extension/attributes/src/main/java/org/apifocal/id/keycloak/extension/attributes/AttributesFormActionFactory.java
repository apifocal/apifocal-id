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

import org.keycloak.Config;
import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormActionFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.List;

public class AttributesFormActionFactory implements FormActionFactory {

    public static final String ATTRIBUTE_DISABLE = "attribute.disableAttributes";
    public static final String ATTRIBUTE_WHITELIST = "attribute.whitelist";
    public static final String ATTRIBUTE_BLACKLIST = "attribute.blacklist";
    public static final String ATTRIBUTE_ALLOW_OTHERS = "attribute.allowOthers";

    private static final List<ProviderConfigProperty> CONFIG_PROPERTIES =ProviderConfigurationBuilder.create()
        .property().name(ATTRIBUTE_DISABLE)
            .label("Disable attributes")
            .helpText("Prevent attributes from being used at all.")
            .defaultValue(false)
            .type(ProviderConfigProperty.BOOLEAN_TYPE)
        .add().property().name(ATTRIBUTE_WHITELIST)
            .label("Whitelist attributes")
            .helpText("Attributes which are allowed to be specified by user (comma separated).")
            .type(ProviderConfigProperty.STRING_TYPE)
        .add().property().name(ATTRIBUTE_BLACKLIST)
            .label("Blacklist attributes")
            .helpText("Attributes which should be disallowed to be specified by user (comma separated)")
            .type(ProviderConfigProperty.STRING_TYPE)
        .add().property().name(ATTRIBUTE_ALLOW_OTHERS)
            .label("Allow other attributes")
            .helpText("Should other attributes, which are not blacklisted or whitelisted allowed. This flag is useful with blacklist, when you want to protect set of attributes but still allow other attributes to be present")
            .defaultValue(false)
            .type(ProviderConfigProperty.BOOLEAN_TYPE)
        .add().build();

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = new AuthenticationExecutionModel.Requirement[] {
        AuthenticationExecutionModel.Requirement.REQUIRED,
        AuthenticationExecutionModel.Requirement.CONDITIONAL,
        AuthenticationExecutionModel.Requirement.DISABLED
    };

    private static final String PROVIDER_ID = "attribute.verification";

    @Override
    public String getDisplayType() {
        return "Attribute verification";
    }

    @Override
    public String getReferenceCategory() {
        return "attribute.verification";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Additional validation of user attributes";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }

    @Override
    public FormAction create(KeycloakSession session) {
        return new AttributesFormAction();
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

}
