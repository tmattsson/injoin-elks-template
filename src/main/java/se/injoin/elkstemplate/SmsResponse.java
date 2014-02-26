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

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import se.injoin.elkstemplate.support.MoneyDeserializer;
import se.injoin.elkstemplate.support.TimestampDeserializer;

/**
 * Response from sending an SMS and as recorded in the SMS history.
 *
 * @author Tobias Mattsson
 * @see ElksTemplate#sendSms(SmsRequest)
 * @see ElksTemplate#querySmsHistory()
 * @see ElksTemplate#querySmsHistory(Date)
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsResponse {

    public static final String DIRECTION_OUTGOING = "outgoing";
    public static final String DIRECTION_INCOMING = "incoming";

    private String id;
    private String direction;
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Date created;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal cost;
    private String from;
    private String to;
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * The direction of the SMS, either {@link #DIRECTION_INCOMING} or {@link #DIRECTION_OUTGOING}.
     */
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SmsResponse{" +
                "id='" + id + '\'' +
                ", direction='" + direction + '\'' +
                ", created='" + created + '\'' +
                ", cost=" + cost +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
