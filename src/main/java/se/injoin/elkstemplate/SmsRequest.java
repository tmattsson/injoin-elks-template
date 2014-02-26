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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A request to send an SMS with the 46 Elks service.
 *
 * @author Tobias Mattsson
 * @see ElksTemplate#sendSms(SmsRequest)
 * @since 1.0
 */
public class SmsRequest {

    private String from;
    private List<String> recipients = new ArrayList<String>();
    private String message;
    private boolean flash;
    private String deliveryReportUrl;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public boolean addRecipient(String recipient) {
        return recipients.add(recipient);
    }

    public boolean addRecipients(Collection<String> recipients) {
        return recipients.addAll(recipients);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFlash() {
        return flash;
    }

    public void setFlash(boolean flash) {
        this.flash = flash;
    }

    public String getDeliveryReportUrl() {
        return deliveryReportUrl;
    }

    public void setDeliveryReportUrl(String deliveryReportUrl) {
        this.deliveryReportUrl = deliveryReportUrl;
    }

    @Override
    public String toString() {
        return "SmsRequest{" +
                "from='" + from + '\'' +
                ", recipients=" + recipients +
                ", message='" + message + '\'' +
                ", flash=" + flash +
                ", deliveryReportUrl='" + deliveryReportUrl + '\'' +
                '}';
    }
}
