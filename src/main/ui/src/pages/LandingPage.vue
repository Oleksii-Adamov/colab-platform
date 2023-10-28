<template>
  <div></div>
</template>

<script>
import {bus, keycloak, router} from "@/main";
import {whenTokenNotUndefined} from "@/authUtils";
import {loginUser} from "@/services/userService";
export default {
  name: "LandingPage",
  mounted() {
    whenTokenNotUndefined().then(() => {
      localStorage.setItem("user-token", undefined);
      keycloak.updateToken(-1).then((refreshed) => {
        if (refreshed) {
          console.log("Token forcibly refreshed");
          localStorage.setItem("user-token", keycloak.token);
          loginUser();
          router.push({path: '/test'});
          bus.emit('unlocked');
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