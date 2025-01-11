export function createCookie(name, value) {
    document.cookie = name + "=" + value + "; path=/";
}

export function getCookie(name) {
    if (document.cookie.length > 0) {
        let cookieStart = document.cookie.indexOf(name + "=");
        if (cookieStart !== -1) {
            cookieStart = cookieStart + name.length + 1;
            let cookieEnd = document.cookie.indexOf(";", cookieStart);
            if (cookieEnd === -1) {
                cookieEnd = document.cookie.length;
            }
            return unescape(document.cookie.substring(cookieStart, cookieEnd));
        }
    }
}

export function deleteCookie(name) {
    document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}