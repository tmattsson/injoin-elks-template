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

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Holds details on sent SMSes and the date used to query for the next page of history.
 *
 * @author Tobias Mattsson
 * @see ElksTemplate#querySmsHistory()
 * @see ElksTemplate#querySmsHistory(Date)
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsHistory {

    @JsonProperty("data")
    private List<SmsResponse> responses;
    private Date next;

    public List<SmsResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<SmsResponse> responses) {
        this.responses = responses;
    }

    public Date getNext() {
        return next;
    }

    public void setNext(Date next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "SmsHistory{" +
                "responses=" + responses +
                ", next=" + next +
                '}';
    }
}
