<template>
  <nav class="nav">
    <input type="checkbox" id="nav-check">
    <div class="nav-links">
      <router-link :to="{ name: 'applications-page'}">Stats</router-link>
      <router-link :to="{ name: 'pending-contributions-page'}">Graphs</router-link>
    </div>
  </nav>
</template>

<script>
export default {
  name: "ProfileNavigation"
}
</script>

<style scoped>

</style>