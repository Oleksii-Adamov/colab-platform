<template>
  <div></div>
</template>

<script>
import {bus, keycloak, router} from "@/main";
import {whenTokenNotUndefined} from "@/authUtils";
export default {
  name: "LandingPage",
  mounted() {
    whenTokenNotUndefined().then(() => {
      localStorage.setItem("user-token", undefined);
      keycloak.updateToken(-1).then((refreshed) => {
        if (refreshed) {
          console.log("Token forcibly refreshed");
          localStorage.setItem("user-token", keycloak.token);
          // this.$router.push({path: '/test'});
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