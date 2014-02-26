/*
 * Copyright (C) 2014 Injoin AB.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.injoin.elkstemplate.support;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Deserializes boolean represented as "yes" and "no" in the API.
 *
 * @author Tobias Mattsson
 * @since 1.0
 */
public class BooleanStringDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String text = jsonParser.getText();
        if (text.equals("yes")) {
            return Boolean.TRUE;
        }
        if (text.equals("no")) {
            return Boolean.FALSE;
        }
        throw context.weirdStringException(text, Boolean.class, "only \"yes\" or \"no\" recognized");
    }
}
