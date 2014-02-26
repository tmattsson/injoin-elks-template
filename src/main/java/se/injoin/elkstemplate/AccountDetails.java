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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import se.injoin.elkstemplate.support.MoneyDeserializer;
import se.injoin.elkstemplate.support.TimestampDeserializer;

/**
 * Account details for an account with the 46 Elks service.
 *
 * @author Tobias Mattsson
 * @see se.injoin.elkstemplate.ElksTemplate#queryAccountDetails()
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDetails {

    private String id;
    @JsonProperty("displayname")
    private String displayName;
    private String currency;
    @JsonProperty("trialactivated")
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Date trialActivated;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal balance;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the currency in three-letter upper case format.
     */
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getTrialActivated() {
        return trialActivated;
    }

    public void setTrialActivated(Date trialActivated) {
        this.trialActivated = trialActivated;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "displayName='" + displayName + '\'' +
                ", id='" + id + '\'' +
                ", currency='" + currency + '\'' +
                ", trialActivated='" + trialActivated + '\'' +
                ", balance=" + balance +
                ", email='" + email + '\'' +
                '}';
    }
}
