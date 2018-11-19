package com.retrofitsample.api.model;

import java.util.List;
/*
* A POJO Class
 */
public class RepositoriesModel {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public class Item {

        private String name;
        private String description;
        private String watchers;
        private Owner owner;
        private String html_url;

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getWatchers() {
            return watchers;
        }

        public Owner getOwner() {
            return owner;
        }

        public String getHtml_url() {
            return html_url;
        }

        public class Owner{

            private String login;
            private String avatar_url;

            public String getLogin() {
                return login;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

        }
    }
}
