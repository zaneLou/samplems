package com.phn.base.component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.jupport.manager.CacheContainer;
import org.jupport.utils.CopyFromNotNullBeanUtilsBean;
import org.jupport.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.phn.base.model.user.User;
import com.phn.base.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserStoreImpl implements UserStore {

    public static int defaultTimeout = 60 * 60 * 2;

    public static String userIdPrefix = "phn:userId:";
    public static String userNamePrefix = "phn:username:";
    public static String userTokenPrefix = "phn:userToken:";
    @Autowired
    protected CacheContainer cacheContainer;

    @Autowired
    protected UserRepository<User> userRepository;

    @Autowired
    protected PhnAuthenticator authenticator;

    @PostConstruct
    public void postConstruct() {
        log.info("++++UserStoreImpl++++");
        craeteSampleUsers();
        //cacheContainer.setObjectValue("key", "大麦号😜".getBytes(), 0);
        //log.info("getObject {}", new String(cacheContainer.getObject("key")));
    }

    /*
     * Sample
     */
    private void craeteSampleUsers() {
        //craeteSampleUser(PhnConstants.TestUserName1, 6l, PhnConstants.TestPassword);
        //craeteSampleUser(PhnConstants.TestUserName2, 7l, PhnConstants.TestPassword);
        craeteSampleUser(PhnConstants.TestUserName1, 1l, PhnConstants.TestPassword);
        craeteSampleUser(PhnConstants.TestUserName2, 2l, PhnConstants.TestPassword);
    }

    private void craeteSampleUser(String username, long orgiId, String password) {
        User test1User = userRepository.findUserByLowcaseUsername(username);
        if (test1User == null) {
            test1User = new User();
            User orgiUser = userRepository.getOneByUid(orgiId);
            try {
                CopyFromNotNullBeanUtilsBean.getInstance().copyProperties(test1User, orgiUser);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            test1User.setUid(null);
            test1User.setUsername(username);
            authenticator.updatehashPassword(test1User, password);
            saveUser(test1User, true);
        }
    }

    @Override
    public UserRepository<User> getUserRepository() {
        return userRepository;
    }

    @Override
    @Transactional
    public void saveUser(User user, boolean cached) throws DataAccessException {
        userRepository.saveOrUpdate(user);
    }

    @Override
    public User getUserAndCreateIfNotExists(String openId, String nickname, User referee) {
        User user = userRepository.findUserByWeiXinOpenId(openId);
        Date now = new Date();
        if (user == null) {
            user = new User();
            user.setWxOpenId(openId);
            user.setCreateDate(now);
            userRepository.saveOrUpdate(user);
        }
        return user;
    }

    public void setCacheUser(User user) {
        String key = userIdPrefix + user.getUid();
        cacheContainer.setObjectValue(key, JSONUtil.mapToJsonString(user).getBytes(), defaultTimeout);
    }

    public User getCacheUser(long userId) {
        String key = userIdPrefix + userId;
        byte[] values = cacheContainer.getObject(key);
        if (values != null) {
            try {
                return JSONUtil.getObjectMapper().readValue(new String(values), User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User getCacheUser(String username) {
        String key = userNamePrefix + username;
        byte[] values = cacheContainer.getObject(key);
        if (values != null) {
            long userId = Long.parseLong(new String(values));
            return getCacheUser(userId);
        }
        return null;
    }

    @Override
    @Transactional
    public User findUserByLowcaseUsername(String username, boolean cached) {
        User user = getCacheUser(username);
        if (user == null) {
            user = userRepository.findUserByLowcaseUsername(username);
        }
        if (user!=null && cached) {
            cacheContainer.setObjectValue(userNamePrefix + username, ("" + user.getUid()).getBytes(), defaultTimeout);
            setCacheUser(user);
        }
        return user;
    }
    // Login Limit, Request Limit, One Time Password Login
    // limits the number of login attempts for specific user accounts to prevent brute-forcing users' passwords.
    @Override
    public void increLoginFail(String username){
        String key = userNamePrefix + username;
        cacheContainer.incrBy(key, 1, defaultTimeout);
    }
    
    @Override
    public boolean hasReachLoginFailLimit(String username){
        String key = userNamePrefix + username;
        byte[] value = cacheContainer.getObject(key);
        if(value!=null){
            String svalue = new String(value);
            return Integer.parseInt(svalue) > 6;
        }
        return false;
    }

    @Override
    public String getUserToken(long userId, String device) {
        return cacheContainer.getMapFeild(userTokenPrefix + userId, device);
    }

    @Override
    public String updateUserToken(User user, String device, boolean cached) {
        String key = userTokenPrefix + user.getUid();
        Map<String, String> tokensMap = cacheContainer.getMap(key);
        if (tokensMap == null) {
            tokensMap = new HashMap<String, String>();
        }
        String token = UUID.randomUUID().toString();
        tokensMap.put(device, token);
        cacheContainer.setMapFeild(key, device, token, false);
        user.setTokens(JSONUtil.mapToJsonString(tokensMap));
        if (cached) {
            setCacheUser(user);
        }
        userRepository.saveOrUpdate(user);
        return token;
    }


}
