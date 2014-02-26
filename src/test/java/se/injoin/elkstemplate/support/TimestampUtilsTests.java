/**
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

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class TimestampUtilsTests {

    @Test
    public void testParseAndFormat() throws ParseException {

        // GMT
        String source = "2012-05-08T20:38:11.623000";

        // In default TimeZone
        Date date = TimestampUtils.parse(source);

        // Format as CET and make sure its +2 hours
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("CET"));
        assertEquals("2012-05-08 22:38:11", format.format(date));

        // Format back to GMT
        assertEquals(source, TimestampUtils.format(date));
    }
}
