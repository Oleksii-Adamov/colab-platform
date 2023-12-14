<template>
  <div></div>
</template>

<script>
import {bus, keycloak, router} from "@/main";
import {whenTokenNotUndefined} from "@/authUtils";
import {loginUser} from "@/services/userService";
import {jwtDecode} from "jwt-decode";
export default {
  name: "LandingPage",
  mounted() {
    whenTokenNotUndefined().then(() => {
      localStorage.setItem("user-token", undefined);
      keycloak.updateToken(-1).then((refreshed) => {
        if (refreshed) {
          console.log("Token forcibly refreshed");
          localStorage.setItem("user-token", keycloak.token);
          let decoded_user_token = jwtDecode(keycloak.token);
          console.log(decoded_user_token);
          if (decoded_user_token['realm_access']['roles'].includes('Admin')) {
            this.$router.push({path: '/admin-page'});
          } else {
            loginUser((response) => {
              let userId = response.data.userId;
              console.log("userId =", userId);
              localStorage.setItem("userId", userId);

              router.push({path: '/my-projects'});
              bus.emit('unlocked');
            });
          }
        } else {
          console.error('Token not forcibly refreshed');
          window.location.reload();
        }

      }).catch(() => {
        console.error("Force refresh token error");
        window.location.reload();
      });
    })
  }
}
</script>

<style scoped>

</style>