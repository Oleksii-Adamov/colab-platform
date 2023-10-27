
import { createApp } from 'vue';
import App from './App.vue';
import routes from "@/routes";
import { createRouter, createWebHashHistory} from 'vue-router';
import Keycloak from 'keycloak-js';
import {whenTokenNotUndefined} from "@/authUtils";

let keycloakInitOptions = {
    url: 'http://127.0.0.1:8080', realm: 'ColabPlatformRealm', clientId: 'colab-platform-client', onLoad:'login-required'
}

export const router = createRouter({
    history: createWebHashHistory (),
    routes,
});

const EventEmitter = require('events');
export const bus = new EventEmitter();

export const keycloak = new Keycloak(keycloakInitOptions);

const app = createApp(App);

// app.use(router)


//localStorage.setItem("user-token", keycloak.token);
//console.log(keycloak.token);

keycloak.init({ onLoad: 'login-required' }).then((auth) => {

    if (!auth) {
        window.location.reload();
    } else {
        console.log("Authenticated");
    }

    app.use(router)


    localStorage.setItem("user-token", keycloak.token);
    console.log(keycloak.token);

    bus.emit('unlocked');

    app.mount('#app')

    setInterval(() =>{
        whenTokenNotUndefined().then( () => {
            keycloak.updateToken(70).then((refreshed) => {
                if (refreshed) {
                    localStorage.setItem("user-token", keycloak.token);
                    console.log("Token refreshed");
                    console.log(keycloak.token);
                } else {
                    console.log('Token not refreshed, valid for '
                        + Math.round(keycloak.tokenParsed.exp + keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds');
                }
            }).catch(() => {
                console.error("Refresh token error");
            });
        });

    }, 60000);
    }).catch(() =>{
    console.error("Authentication Failed");
    });