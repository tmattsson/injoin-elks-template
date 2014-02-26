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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import se.injoin.elkstemplate.support.TimestampUtils;

/**
 * Template class for using the 46 Elks service. Uses {@link RestTemplate} to communicate with their REST API. Has
 * functionality for sending SMSes, querying the SMS history, allocating and deallocating phone numbers and checking
 * the account balance. Does not include functionality for voice calls.
 *
 * @author Tobias Mattsson
 * @since 1.0
 */
public class ElksTemplate {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String DEFAULT_API_URL = "https://api.46elks.com/a1";
    public static final String ME_RESOURCE_PATH = "/Me";
    public static final String NUMBERS_RESOURCE_PATH = "/Numbers";
    public static final String SMS_RESOURCE_PATH = "/SMS";
    public static final int DEFAULT_BATCH_LIMIT = 2000;

    private String apiUrl = DEFAULT_API_URL;
    private int batchLimit = DEFAULT_BATCH_LIMIT;
    private String username;
    private String password;
    private RestTemplate restTemplate;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public int getBatchLimit() {
        return batchLimit;
    }

    public void setBatchLimit(int batchLimit) {
        this.batchLimit = batchLimit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public synchronized RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = createRestTemplate();
        }
        return restTemplate;
    }

    public synchronized void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Returns the account details including the account balance.
     *
     * @see AccountDetails
     */
    public AccountDetails queryAccountDetails() throws ElksException {
        try {
            AccountDetails accountDetails = getRestTemplate().getForObject(apiUrl + ME_RESOURCE_PATH, AccountDetails.class);
            if (logger.isDebugEnabled()) {
                logger.debug("Queried account details for account [" + accountDetails.getId() + "]");
            }
            return accountDetails;
        } catch (RestClientException e) {
            throw new ElksException("Could not query account details", e);
        }
    }

    /**
     * Returns a list of allocated phone numbers.
     *
     * @see PhoneNumberDetails
     */
    public List<PhoneNumberDetails> queryPhoneNumbers() throws ElksException {
        try {
            List<PhoneNumberDetails> numbers = getRestTemplate().getForObject(apiUrl + NUMBERS_RESOURCE_PATH, PhoneNumberList.class).getNumbers();
            if (logger.isDebugEnabled()) {
                logger.debug("Queried phone numbers, found [" + numbers.size() + "] numbers");
            }
            return numbers;
        } catch (RestClientException e) {
            throw new ElksException("Could not query phone numbers", e);
        }
    }

    /**
     * Returns details about a specific phone number.
     *
     * @param id id of the phone number to query details for
     * @return details for the phone number or null if the number does not exist
     * @see PhoneNumberDetails
     */
    public PhoneNumberDetails queryPhoneNumber(String id) throws ElksException {
        Assert.hasText(id);
        try {
            PhoneNumberDetails number = getRestTemplate().getForObject(apiUrl + NUMBERS_RESOURCE_PATH + "/" + id, PhoneNumberDetails.class);
            if (logger.isDebugEnabled()) {
                logger.debug("Queried phone number [" + id + "]");
            }
            return number;
        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException clientErrorException = (HttpClientErrorException) e;
                if (clientErrorException.getStatusCode() == HttpStatus.NOT_FOUND) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Phone number not found [" + id + "]");
                    }
                    return null;
                }
            }
            throw new ElksException("Could not query phone number details", e);
        }
    }

    /**
     * Allocates a new phone number. Does not support the <code>voice_start</code> parameter.
     * <p/>
     * The SMS URL is called when an SMS is sent to this number. It is called with the following parameters:
     * <ul>
     * <li>from</li>
     * <li>to</li>
     * <li>message</li>
     * </ul>
     * <p/>
     * Note that a phone number is not required when sending SMSes with an alphanumeric <code>from</code>.
     *
     * @param country two-digit lower-case country code
     * @param smsUrl null or a valid URL, optional
     */
    public PhoneNumberDetails allocatePhoneNumber(String country, String smsUrl) throws ElksException {
        Assert.hasText(country);
        try {
            LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
            parameters.set("country", country);
            if (StringUtils.hasText(smsUrl)) {
                parameters.set("sms_url", smsUrl);
            }
            PhoneNumberDetails number = getRestTemplate().postForObject(apiUrl + NUMBERS_RESOURCE_PATH, parameters, PhoneNumberDetails.class);
            if (logger.isDebugEnabled()) {
                logger.debug("Allocated phone number [" + number.getNumber() + "] with id [" + number.getId() + "]");
            }
            return number;
        } catch (RestClientException e) {
            throw new ElksException("Could not query account details", e);
        }
    }

    /**
     * Update the details for a phone number. The sms url can not be unset once set.
     *
     * @param id id of the number to update
     * @param smsUrl null or a valid URL, optional
     */
    public PhoneNumberDetails updatePhoneNumber(String id, String smsUrl) throws ElksException {
        Assert.hasText(id);
        try {
            LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
            if (StringUtils.hasText(smsUrl)) {
                parameters.set("sms_url", smsUrl);
            }
            PhoneNumberDetails number = getRestTemplate().postForObject(apiUrl + NUMBERS_RESOURCE_PATH + "/" + id, parameters, PhoneNumberDetails.class);
            if (logger.isDebugEnabled()) {
                logger.debug("Updated phone number [" + id + "]");
            }
            return number;
        } catch (RestClientException e) {
            throw new ElksException("Could not update account details", e);
        }
    }

    /**
     * Deallocates a a phone number. The number cannot be restored afterwards.
     *
     * @param id id of the number to deallocate
     */
    public PhoneNumberDetails deallocatePhoneNumber(String id) throws ElksException {
        Assert.hasText(id);
        try {
            LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
            parameters.set("active", "no");
            PhoneNumberDetails number = getRestTemplate().postForObject(apiUrl + NUMBERS_RESOURCE_PATH + "/" + id, parameters, PhoneNumberDetails.class);
            if (logger.isDebugEnabled()) {
                logger.debug("Updated phone number [" + id + "]");
            }
            return number;
        } catch (RestClientException e) {
            throw new ElksException("Could not update account details", e);
        }
    }

    /**
     * Sends an SMS to a single recipient.
     */
    public SmsResponse sendSms(String from, String recipient, String message) throws ElksException {
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setFrom(from);
        smsRequest.setMessage(message);
        smsRequest.addRecipient(recipient);
        return sendSms(smsRequest).get(0);
    }

    /**
     * Sends an SMS to multiple recipients. The response is only filled with <code>id</code> and <code>to</code>.
     */
    public List<SmsResponse> sendSms(String from, List<String> recipients, String message) throws ElksException {
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setFrom(from);
        smsRequest.setMessage(message);
        smsRequest.setRecipients(recipients);
        return sendSms(smsRequest);
    }

    /**
     * Sends a flash SMS to a single recipient.
     */
    public SmsResponse sendFlashSms(String from, String recipient, String message) throws ElksException {
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setFrom(from);
        smsRequest.setMessage(message);
        smsRequest.setFlash(true);
        smsRequest.addRecipient(recipient);
        return sendSms(smsRequest).get(0);
    }

    /**
     * Sends a flash SMS to multiple recipients. The response is only filled with <code>id</code> and <code>to</code>.
     */
    public List<SmsResponse> sendFlashSms(String from, List<String> recipients, String message) throws ElksException {
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setFrom(from);
        smsRequest.setMessage(message);
        smsRequest.setRecipients(recipients);
        smsRequest.setFlash(true);
        return sendSms(smsRequest);
    }

    /**
     * Sends an SMS to all recipients in the request. Messages longer than what fits in one SMS are automatically sent
     * as several. The number of recipients can be exceed the per-call-limit imposed by the service as this method will
     * send them batched in multiple calls. When sending to more than one recipient only <code>id</code> and
     * <code>to</code> is filled in the response.
     * <p/>
     * The <code>from</code> number can either be one of your previously allocated phone numbers or an alphanumeric
     * string containing only characters a-z, A-Z, and 0-9 and max 11 characters. No other characters can be used, and
     * the name has to begin with a letter.
     * <p/>
     * The request can specify a callback URL to be used for receiving a delivery report. It's called with parameters:
     * <ul>
     * <li>id</li>
     * <li>status - one of "sent", "delivered" or "failed"</li>
     * <li>delivered - time of delivery, only when status is "delivered"</li>
     * </ul>
     *
     * @see SmsRequest
     * @see SmsResponse
     */
    public List<SmsResponse> sendSms(SmsRequest smsRequest) throws ElksException {
        Assert.notNull(smsRequest);
        Assert.hasText(smsRequest.getFrom());
        Assert.hasText(smsRequest.getMessage());
        Assert.notNull(smsRequest.getRecipients());

        List<String> recipients = smsRequest.getRecipients();

        if (recipients.isEmpty()) {
            return Collections.emptyList();
        }

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.set("from", smsRequest.getFrom());
        parameters.set("message", smsRequest.getMessage());
        if (smsRequest.isFlash()) {
            parameters.set("flashsms", "yes");
        }
        if (smsRequest.getDeliveryReportUrl() != null) {
            parameters.set("whendelivered", smsRequest.getDeliveryReportUrl());
        }

        ArrayList<SmsResponse> responses = new ArrayList<SmsResponse>(recipients.size());
        try {

            int startIndex = 0;
            while (startIndex < recipients.size()) {
                int endIndex = Math.min(startIndex + batchLimit, recipients.size());
                List<String> batch = recipients.subList(startIndex, endIndex);
                parameters.set("to", StringUtils.collectionToCommaDelimitedString(batch));
                if (batch.size() == 1) {
                    // The service does not respond with an array when sending to only one number
                    responses.add(getRestTemplate().postForObject(apiUrl + SMS_RESOURCE_PATH, parameters, SmsResponse.class));
                } else {
                    responses.addAll(getRestTemplate().postForObject(apiUrl + SMS_RESOURCE_PATH, parameters, SmsBatchResponse.class));
                }
                startIndex = endIndex;
            }

        } catch (RestClientException e) {

            int total = smsRequest.getRecipients().size();
            int delivered = responses.size();
            if (total == 1) {
                throw new ElksException("Could not send text message to [1] recipient", e);
            }
            if (delivered == 0) {
                throw new ElksException("Could not send text message to [" + total + "] recipients", e);
            }
            throw new ElksException("Could not send text message to all [" + total + "] recipients, failed after delivering [" + delivered + "]", e);
        }

        if (logger.isDebugEnabled()) {
            if (smsRequest.isFlash()) {
                logger.debug("Sent flash SMS to [" + responses.size() + "] recipients");
            } else {
                logger.debug("Sent SMS to [" + responses.size() + "] recipients");
            }
        }

        return responses;
    }

    /**
     * Queries the SMS history returning the first 100 entries. Use {@link #querySmsHistory(Date)} to get more entries.
     */
    public SmsHistory querySmsHistory() {
        try {
            SmsHistory history = getRestTemplate().getForObject(apiUrl + SMS_RESOURCE_PATH, SmsHistory.class);
            if (logger.isDebugEnabled()) {
                logger.debug("Queried SMS history");
            }
            return history;
        } catch (RestClientException e) {
            throw new ElksException("Could not query message history", e);
        }
    }

    /**
     * Queries for a part of the SMS history using an offset. Returns 100 entries. Call this method again with the
     * <code>next</code> value returned.
     *
     * @param start an offset into the history
     * @see se.injoin.elkstemplate.SmsHistory#getNext()
     */
    public SmsHistory querySmsHistory(Date start) {
        if (start == null) {
            return querySmsHistory();
        }
        try {
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("start", TimestampUtils.format(start));
            SmsHistory history = getRestTemplate().getForObject(apiUrl + SMS_RESOURCE_PATH + "?start={start}", SmsHistory.class, parameters);
            if (logger.isDebugEnabled()) {
                logger.debug("Queried SMS history with start [" + start + "]");
            }
            return history;
        } catch (RestClientException e) {
            throw new ElksException("Could not query message history with start [" + start + "]", e);
        } catch (ParseException e) {
            throw new ElksException("Could not parse start date [" + start + "]", e);
        }
    }

    /**
     * Creates a REST template using HttpComponents' HttpClient configured to use basic HTTP AUTH.
     */
    protected RestTemplate createRestTemplate() {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        httpClient.setCredentialsProvider(credentialsProvider);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }

    private static class SmsBatchResponse extends ArrayList<SmsResponse> {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PhoneNumberList {

        @JsonProperty("data")
        private List<PhoneNumberDetails> numbers;

        public List<PhoneNumberDetails> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<PhoneNumberDetails> numbers) {
            this.numbers = numbers;
        }
    }
}
