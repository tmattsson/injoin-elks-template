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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utilities for parsing and formatting timestamps. The API provides them as "2012-05-08T20:38:11.623000".
 *
 * @author Tobias Mattsson
 * @since 1.0
 */
public class TimestampUtils {

    private static final String GMT_ID = "GMT";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static Date parse(String source) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(GMT_ID));
        // Remove last 3 digits to make milliseconds 3 digits long
        String timestamp = source.substring(0, 23);
        return format.parse(timestamp);
    }

    public static String format(Date timestamp) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(GMT_ID));
        return format.format(timestamp) + "000";
    }
}
