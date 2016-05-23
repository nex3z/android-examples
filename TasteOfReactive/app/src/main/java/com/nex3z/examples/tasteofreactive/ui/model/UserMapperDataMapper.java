package com.nex3z.examples.tasteofreactive.ui.model;

import com.nex3z.examples.tasteofreactive.rest.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapperDataMapper {

    public UserModel transform(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }

        UserModel userModel = new UserModel();
        userModel.setAvatarUrl(user.getAvatarUrl());
        userModel.setEventsUrl(user.getEventsUrl());
        userModel.setFollowersUrl(user.getFollowersUrl());
        userModel.setFollowingUrl(user.getFollowingUrl());
        userModel.setGistsUrl(user.getGistsUrl());
        userModel.setGravatarId(user.getGravatarId());
        userModel.setHtmlUrl(user.getHtmlUrl());
        userModel.setId(user.getId());
        userModel.setLogin(user.getLogin());
        userModel.setOrganizationsUrl(user.getOrganizationsUrl());
        userModel.setReceivedEventsUrl(user.getReceivedEventsUrl());
        userModel.setReposUrl(user.getReposUrl());
        userModel.setSiteAdmin(user.isSiteAdmin());
        userModel.setStarredUrl(user.getStarredUrl());
        userModel.setSubscriptionsUrl(user.getSubscriptionsUrl());
        userModel.setType(user.getType());
        userModel.setUrl(user.getUrl());

        return userModel;
    }

    public List<UserModel> transform(List<User> users) {
        if (users == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }

        List<UserModel> userModels = new ArrayList<UserModel>();
        for (User user : users) {
            UserModel userModel = new UserModel();
            userModel = transform(user);
            userModels.add(userModel);
        }

        return userModels;
    }
}
