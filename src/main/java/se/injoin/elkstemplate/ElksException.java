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

package se.injoin.elkstemplate;

import org.springframework.core.NestedRuntimeException;

/**
 * Exception thrown when operations on the 46 Elks service fail. Causes can be an error in the transport, on the server
 * or in the client.
 *
 * @author Tobias Mattsson
 * @see ElksTemplate
 * @since 1.0
 */
public class ElksException extends NestedRuntimeException {

    public ElksException(String msg) {
        super(msg);
    }

    public ElksException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
