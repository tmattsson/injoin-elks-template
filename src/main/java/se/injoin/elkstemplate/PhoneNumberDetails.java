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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import se.injoin.elkstemplate.support.BooleanStringDeserializer;

/**
 * Details about a phone number allocated with the 46 Elks service. Does not include the <code>voice_start</code>
 * attribute used when the phone number is called.
 *
 * @author Tobias Mattsson
 * @see se.injoin.elkstemplate.ElksTemplate#queryPhoneNumbers()
 * @see se.injoin.elkstemplate.ElksTemplate#queryPhoneNumber(String)
 * @see se.injoin.elkstemplate.ElksTemplate#allocatePhoneNumber(String, String)
 * @see se.injoin.elkstemplate.ElksTemplate#deallocatePhoneNumber(String)
 * @see se.injoin.elkstemplate.ElksTemplate#updatePhoneNumber(String, String)
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumberDetails {

    public static final String CAPABILITY_SMS = "sms";
    public static final String CAPABILITY_VOICE = "voice";

    private String id;
    @JsonDeserialize(using = BooleanStringDeserializer.class)
    private boolean active;
    private String country;
    private String number;
    private List<String> capabilities = new ArrayList<String>();
    @JsonProperty("sms_url")
    private String smsUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the country this number is allocated in as a two-letter lower-case code.
     */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Returns a list of the capabilities for this number. The capabilities are {@link #CAPABILITY_SMS} and {@link #CAPABILITY_VOICE}.
     */
    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public void addCapability(String capability) {
        if (!capabilities.contains(capability)) {
            capabilities.add(capability);
        }
    }

    public boolean hasCapability(String capability) {
        return capabilities.contains(capability);
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    @Override
    public String toString() {
        return "PhoneNumberDetails{" +
                "id='" + id + '\'' +
                ", active='" + active + '\'' +
                ", country='" + country + '\'' +
                ", number='" + number + '\'' +
                ", capabilities=" + capabilities +
                ", smsUrl='" + smsUrl + '\'' +
                '}';
    }
}
