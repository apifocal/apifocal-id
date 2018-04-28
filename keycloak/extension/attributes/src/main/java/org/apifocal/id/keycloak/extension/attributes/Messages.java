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

/**
 * Message keys which are returned from validation.
 */
public class Messages {

    /**
     * Message returned when user attributes processing is completely disabled.
     */
    public static final String USER_ATTRIBUTES_NOT_EXPECTED = "user.attributes.not_expected";

    /**
     * When given attribute is blacklisted or missing on white list - this message is set on it.
     */
    public static final String USER_ATTRIBUTE_NOT_EXPECTED = "user.attribute.not_expected";

}
