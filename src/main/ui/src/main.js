// import Vue, {createApp} from 'vue'
// import App from './App.vue'
// import Vue from 'vue'
// import VueRouter from "vue-router";
import { createApp } from 'vue';
import App from './App.vue';
import routes from "@/routes";
import { createRouter, createWebHashHistory } from 'vue-router';

// createApp(App).mount('#app')
// Vue.use(VueRouter)
//
// const router = new VueRouter({
//     routes: Routes,
//     mode: 'history'
// });
//
// new Vue({
//     router
// }).$mount('#app')
// const router = VueRouter.createRouter({
//     // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
//     history: VueRouter.createWebHashHistory(),
//     routes, // short for `routes: routes`
// })
const router = createRouter({
    history: createWebHashHistory(),
    routes,
});
// 5. Create and mount the root instance.
// const app = Vue.createApp({})
const app = createApp(App);
// Make sure to _use_ the router instance to make the
// whole app router-aware.
app.use(router)

app.mount('#app')