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

import java.util.Arrays;
import java.util.Objects;

/**
 * Custom form message which implements equals and hash code, because base class in keycloak doesn't.
 */
public class FormMessage extends org.keycloak.models.utils.FormMessage {

    public FormMessage(String field, String message, Object... parameters) {
        super(field, message, parameters);
    }

    public FormMessage(String message, Object... parameters) {
        this(GLOBAL, message, parameters);
    }

    public FormMessage(String field, String message) {
        super(field, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormMessage)) return false;
        FormMessage that = (FormMessage) o;
        return Objects.equals(getField(), that.getField()) &&
            Objects.equals(getMessage(), that.getMessage()) &&
            Arrays.equals(getParameters(), that.getParameters());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getField(), getMessage());
        result = 31 * result + Arrays.hashCode(getParameters());
        return result;
    }

}
