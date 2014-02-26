## Elks Template
Client for using the [46 Elks API][] for sending and receiving SMS messages. Built
on Spring Framework using RestTemplate and Apache Http Components HttpClient to
communicate with their REST API, uses Jackson 2 for JSON serialization and SLF4J
for logging.

## Features

* Automatically sends long messages as multiple SMS
* Flash SMS
* Use text instead of number as sender
* Can send to 2000 recipients in a single call
* Delivery report as a HTTP callback
* Receive SMS with a HTTP callback
* Allocate and deallocate phone numbers
* Query SMS history for both incoming and outgoing messages
* Query account balance

## License

Released under version 3.0 of the [GNU General Public License][].

## Usage example

```java
ElksTemplate template = new ElksTemplate();
template.setUsername("...");
template.setPassword("...");

AccountDetails account = template.queryAccountDetails();
System.out.println("Balance is " + account.getBalance() + " " + account.getCurrency());

template.sendSms("PostOffice", TO, "Your parcel has been sent.");

template.sendFlashSms("MyService", TO, "12345678");

SmsHistory history = template.querySmsHistory();
for (SmsResponse entry : history.getResponses()) {
    System.out.println(entry.getId() + "  " + entry.getTo() + "  " + entry.getMessage());
}
```

[46 Elks API]: http://www.46elks.com/docs/
[GNU General Public License]: http://www.gnu.org/licenses/gpl.txt