import axios from "axios";
import {getCookie} from "./cookies";

export function getUser(username) {
    return axios.get(`${process.env.REACT_APP_API_BASE}/app/user/${username}`, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}

export function getUsers() {
    return axios.get(`${process.env.REACT_APP_API_BASE}/app/user`, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}

export function getCurrentUser() {
    return axios.get(`${process.env.REACT_APP_API_BASE}/app/user/me`, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}

export function getUserPosts(username, htmlBody) {
    return axios.get(`${process.env.REACT_APP_API_BASE}/app/posts/author/${username}?htmlBody=${htmlBody}`, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}

export function createPost(title, content) {
    return axios.post(`${process.env.REACT_APP_API_BASE}/app/posts`, {
        title: title, content: content
    }, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}

export function deletePost(id) {
    return axios.delete(`${process.env.REACT_APP_API_BASE}/app/posts/${id}`, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}

export function getUserFollowers(username) {
    return axios.get(`${process.env.REACT_APP_API_BASE}/app/followers/${username}`, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}

export function getUserFollowing(username) {
    return axios.get(`${process.env.REACT_APP_API_BASE}/app/followers/${username}/followed`, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}

export function followUser(username) {
    return axios.post(`${process.env.REACT_APP_API_BASE}/app/followers/${username}`, {}, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}

export function unfollowUser(username) {
    return axios.delete(`${process.env.REACT_APP_API_BASE}/app/followers/${username}`, {
        headers: {
            Authorization: `Bearer ${getCookie(process.env.REACT_APP_TOKEN_COOKIE_NAME)}`
        }
    });
}