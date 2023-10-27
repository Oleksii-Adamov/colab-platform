import {keycloak, bus} from "@/main";

export async function authorizationHeaders() {
    let headers = new Headers();
    await whenTokenNotUndefined();
    headers.append("Authorization", "Bearer " + localStorage.getItem('user-token'));
    console.log("Sending ", localStorage.getItem('user-token'));
    return headers;
}

export function logout() {
    keycloak.logout({redirectUri: 'http://localhost:8081/'});
}

export async function whenTokenNotUndefined() {
    if (localStorage.getItem('user-token') === undefined) {
        console.log("waiting")
        await new Promise(resolve => bus.once('unlocked', resolve));
    }
}