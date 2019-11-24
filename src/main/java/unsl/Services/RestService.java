package unsl.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import unsl.Entities.Account;
import unsl.Entities.User;

import java.time.LocalDateTime;

@Service
public class RestService {
    private static Logger LOGGER = LoggerFactory.getLogger(RestService.class);


    @Retryable(maxAttempts = 4, backoff = @Backoff(1000))
    public Account getAccount(String url) throws Exception{
        LOGGER.info(String.format("BalanceAPI RETRY Ping to transfers/ping %d", LocalDateTime.now().getSecond()));
        RestTemplate restTemplate = new RestTemplate();
        Account account;

        try {
            account = restTemplate.getForObject(url, Account.class); // getForObject return an object of type Account
        }catch (Exception e){
            throw new Exception(buildMessageError(e));
        }
        return account;

    }


    public void updateAccount (String url, Account updatedAccount) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpEntity<Account> requestEntity = new HttpEntity<>(updatedAccount);
            restTemplate.put(url,requestEntity);
        }catch (Exception e){
            throw new Exception(buildMessageError(e));
        }
    }
    public User.Status getUserStatus (String url)throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        User user;

        try {
            user = restTemplate.getForObject(url, User.class); // getForObject return an object of type Account
        }catch (Exception e){
            throw new Exception(buildMessageError(e));
        }
        return user.getStatus();

    }
    private String buildMessageError(Exception e) {
        String msg = e.getMessage();
        if (e instanceof HttpClientErrorException) {
            msg = ((HttpClientErrorException) e).getResponseBodyAsString();
        } else if (e instanceof HttpServerErrorException) {
            msg =  ((HttpServerErrorException) e).getResponseBodyAsString();
        }
        return msg;
    }

}
