<template>
  <UserNavigation></UserNavigation>
  <h2>Pending applications</h2>
  <ul>
    <li v-for="application in applications" :key="application.id">
      <router-link :to="{ name: 'user-page', params: { id: application.userId} }">
        {{ application.userFullName }}
      </router-link>
      <button @click="approve">Approve</button>
      <button @click="reject">Reject</button>
    </li>
  </ul>
</template>

<script>
import UserNavigation from "@/components/UserNavigation";
import ProjectAdminNavigation from "@/components/ProjectAdminNavigation";
export default {
  name: "ApplicationsPage",
  components: {UserNavigation, ProjectAdminNavigation},
  data() {
    return {

    }
  },
  methods: {
    approve() {

    },
    reject() {

    }
  },
  mounted() {

  }
}
</script>

<style scoped>

</style>